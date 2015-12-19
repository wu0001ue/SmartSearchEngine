//this file is part of notepad++
//Copyright (C)2003 Don HO <donho@altern.org>
//
//This program is free software; you can redistribute it and/or
//modify it under the terms of the GNU General Public License
//as published by the Free Software Foundation; either
//version 2 of the License, or (at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software
//Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

#include <string>
#include <sstream>
#include <iostream>
#include <fstream>
#include <shlwapi.h>
#include <windows.h>
#include <winhttp.h>
#include <regex>
#include <shlobj.h>

using namespace std;

#include "PluginDefinition.h"
#include "menuCmdID.h"

const TCHAR configFileName[] = TEXT("SmartSearch.ini");

const char *serverTag = "server";
const char *portTag = "port";
const char *servletTag = "servlet";
const char *usernameTag = "username";
const char *roleTag = "role";

std::string server;
INTERNET_PORT port;
std::string servlet;
std::string username;
std::string role;

// C++ regex for searching the layer in text. The pattern is to be specified in the ECMAScript grammar. See http://www.cplusplus.com/reference/regex/ECMAScript/
// Following regular expressions match patterns of "## Header for xxx Layer" for layer and "## Header for xxx" pattern for component
std::regex layerAndComponentPattern("#+[ ]+(header for)[ ]+.+");
std::regex layerPattern("#+[ ]+(header for)[ ]+.+[ ]+(layer)");

//
// The plugin data that Notepad++ needs
//
FuncItem funcItem[nbFunc];

//
// The data of Notepad++ that you can use in your plugin commands
//
NppData nppData;

TCHAR iniFilePath[MAX_PATH];

//
// Initialize your plugin data here
// It will be called while plugin loading   
void pluginInit(HANDLE hModule)
{
}

//
// Here you can do the clean up, save the parameters (if any) for the next session
//
void pluginCleanUp()
{
}

// forward declarations
void connectToSmartsearch(const std::string & layer,
	const std::string & component,
	const std::string & searchTerms,
	std::string & outStr);
void openInBrowser(const std::string & layer,
	const std::string & component,
	const std::string & searchTerms);

// utility functions
void processLayer(const std::string & line,
	std::string & layer)
{
	size_t beg = 1 + line.find("header for");
	beg = line.find_first_not_of(" ", beg + strlen("header for"));
	size_t end = line.find(" layer");
	layer = line.substr(beg, (end - beg));
}

void processComponent(const std::string & line,
	std::string & component)
{
	size_t beg = 1 + line.find("header for");
	beg = line.find_first_not_of(" ", beg + strlen("header for"));
	component = line.substr(beg);
}

void getValueFromLine(const std::string line,
	std::string & value)
{
	size_t beg = 1 + line.find("=");
	size_t len = line.length() - beg;
	value = line.substr(beg, len);
}

void getYesNoFromLine(const std::string line,
	bool & value)
{
	size_t beg = 1 + line.find("=");
	size_t len = line.length() - beg;
	std::string yes_no = line.substr(beg, len);
	if (yes_no == "yes") {
		value = true;
	}
	else {
		value = false;
	}
}

void readConfigFile(TCHAR *filePath)
{
	string line;
	ifstream myfile(filePath);
	if (myfile.is_open())
	{
		while (getline(myfile, line))
		{
			if (line.find(serverTag) == 0) {
				getValueFromLine(line, server);
			}
			else if (line.find(portTag) == 0) {
				std::string portStr;
				getValueFromLine(line, portStr);
				port = std::stoi(portStr);
			}
			else if (line.find(servletTag) == 0) {
				getValueFromLine(line, servlet);
			}
			else if (line.find(usernameTag) == 0) {
				getValueFromLine(line, username);
			}
			else if (line.find(roleTag) == 0) {
				getValueFromLine(line, role);
			}
		}
		myfile.close();
	}
}

void tolower(std::string & str)
{
	for (size_t i = 0; i < str.length(); i++) {
		str.at(i) = tolower(str.at(i));
	}
}

//
// Initialization of your plugin commands
// You should fill your plugins commands here
void commandMenuInit()
{

	// get the parameters from config
	// parameters include
	// username
	// user role: architect, designer, tester
	// Layer tag
	// Component tag

	// get path of plugin configuration
	::SendMessage(nppData._nppHandle, NPPM_GETPLUGINSCONFIGDIR, MAX_PATH, (LPARAM)iniFilePath);

	// if config path doesn't exist, we create it
	if (PathFileExists(iniFilePath) == FALSE)
	{
		::CreateDirectory(iniFilePath, NULL);
	}

	// make your plugin config file full file path name
	PathAppend(iniFilePath, configFileName);

	readConfigFile(iniFilePath);

	//--------------------------------------------//
	//-- CUSTOMIZE YOUR PLUGIN COMMANDS --//
	//--------------------------------------------//
	// with function :
	// setCommand(int index,                      // zero based number to indicate the order of command
	//            TCHAR *commandName,             // the command name that you want to see in plugin menu
	//            PFUNCPLUGINCMD functionPointer, // the symbol of function (function pointer) associated with this command. The body should be defined below. See Step 4.
	//            ShortcutKey *shortcut,          // optional. Define a shortcut to trigger this command
	//            bool check0nInit                // optional. Make this menu item be checked visually
	//            );
	setCommand(0, TEXT("Output in Notepad++"), displayInNotepad, NULL, false);
	setCommand(1, TEXT("Output in Browser"), displayInBrowser, NULL, false);
}

//
// Here you can do the clean up (especially for the shortcut)
//
void commandMenuCleanUp()
{
	// Don't forget to deallocate your shortcut here
}


//
// This function help you to initialize your plugin commands
//
bool setCommand(size_t index, TCHAR *cmdName, PFUNCPLUGINCMD pFunc, ShortcutKey *sk, bool check0nInit)
{
	if (index >= nbFunc)
		return false;

	if (!pFunc)
		return false;

	lstrcpy(funcItem[index]._itemName, cmdName);
	funcItem[index]._pFunc = pFunc;
	funcItem[index]._init2Check = check0nInit;
	funcItem[index]._pShKey = sk;

	return true;
}

bool getTextFromNotepad(std::string & layer,
	std::string & component,
	std::string & searchTerms,
	HWND & curScintilla)
{
	int curLineNum = ::SendMessage(nppData._nppHandle, NPPM_GETCURRENTLINE, 0, 0);

	// Get the current scintilla
	int which = -1;
	::SendMessage(nppData._nppHandle, NPPM_GETCURRENTSCINTILLA, 0, (LPARAM)&which);
	if (which == -1) {
		return false;
	}
	curScintilla = (which == 0) ? nppData._scintillaMainHandle : nppData._scintillaSecondHandle;

	// read the selected text for search terms, if any
	char line[MAX_PATH];
	::SendMessage(curScintilla, SCI_GETSELTEXT, NULL, (LPARAM)line);
	if (line[0] == NULL) {
		// no selection
		// read the current line for search terms
		int len = ::SendMessage(curScintilla, SCI_GETLINE, curLineNum, (LPARAM)line);
		if (len < 2) {
			return false;
		}
		line[len - 2] = 0; // len-2 is for eliminating newline characters at the end
	}

	searchTerms = line;
	tolower(searchTerms);

	// read earlier lines to find layer and component
	bool layerProcessed(false);
	bool componentProcessed(false);

	while (curLineNum >= 0) {
		int len = ::SendMessage(curScintilla, SCI_GETLINE, curLineNum, (LPARAM)line);
		if (len < 2) {
			return false;
		}
		line[len - 2] = 0; // len-2 is for eliminating newline characters at the end
		string linestr(line);
		tolower(linestr);

		if (!componentProcessed) {
			if (std::regex_match(linestr, layerAndComponentPattern)) {
				// A match found. Check if this is a match for layer
				if (std::regex_match(linestr, layerPattern)) {
					// A match for layer. Process it if not already processed before
					if (!layerProcessed) {
						processLayer(linestr, layer);
						layerProcessed = true;
					}
				}
				else {
					// This is a match for component. Process it.
					processComponent(linestr, component);
					componentProcessed = true;
				}
			}
		}
		if (!layerProcessed) {
			if (std::regex_match(linestr, layerPattern)) {
				// A match for layer. Process it
				processLayer(linestr, layer);
				layerProcessed = true;
			}
		}
		if (layerProcessed && componentProcessed) {
			break;
		}

		curLineNum--;
	}
	return true;
}

void displayInNotepad()
{
	std::string layer;
	std::string component;
	std::string searchTerms;
	HWND curScintilla;

	bool gotText = getTextFromNotepad(layer, component, searchTerms, curScintilla);
	if (!gotText)
	{
		return;
	}

	std::ostringstream ostr;

	ostr << "Server: " << server << "\nServlet: " << servlet << "\nUser: " << username << "\nSearch:" << searchTerms << "\nLayer: " << layer << "\nComponent: " << component;

	std::string outStr;

	connectToSmartsearch(layer, component, searchTerms, outStr);
	ostr << "\nResults from server Smart Search Server: " << outStr;

	// Open a new document
	::SendMessage(nppData._nppHandle, NPPM_MENUCOMMAND, 0, IDM_FILE_NEW);

	// Scintilla control has no Unicode mode, so we use (char *) here
	::SendMessage(curScintilla, SCI_APPENDTEXT, ostr.str().length(), (LPARAM)ostr.str().c_str());
}

void displayInBrowser()
{
	std::string layer;
	std::string component;
	std::string searchTerms;
	HWND curScintilla;

	bool gotText = getTextFromNotepad(layer, component, searchTerms, curScintilla);
	if (!gotText)
	{
		return;
	}
	openInBrowser(layer, component, searchTerms);
}

// connect to a web server using WinHttp library routines
void connect(const std::wstring & hostname,
	INTERNET_PORT port,
	const std::wstring & resource,
	DWORD flags,
	std::string & outStr)
{
	DWORD dwSize = 0;
	DWORD dwDownloaded = 0;
	LPSTR pszOutBuffer;
	BOOL  bResults = FALSE;
	HINTERNET  hSession = NULL,
		hConnect = NULL,
		hRequest = NULL;

	// Use WinHttpOpen to obtain a session handle.
	hSession = WinHttpOpen(L"WinHTTP Example/1.0",
		WINHTTP_ACCESS_TYPE_DEFAULT_PROXY,
		WINHTTP_NO_PROXY_NAME,
		WINHTTP_NO_PROXY_BYPASS, 0);

	// Specify an HTTP server.
	if (hSession) {
		hConnect = WinHttpConnect(hSession,
			hostname.c_str(),
			port, 0);
	}
	else {
		outStr = "Connect to server failed";
		return;
	}

	// Create an HTTP request handle.
	if (hConnect) {
		hRequest = WinHttpOpenRequest(hConnect,
			L"GET",
			(resource.length() != 0) ? resource.c_str() : NULL,
			NULL,
			WINHTTP_NO_REFERER,
			WINHTTP_DEFAULT_ACCEPT_TYPES,
			flags);
	}
	else {
		outStr = "Open request to server failed";
		return;
	}

	// Send a request.
	if (hRequest) {
		bResults = WinHttpSendRequest(hRequest,
			WINHTTP_NO_ADDITIONAL_HEADERS, 0,
			WINHTTP_NO_REQUEST_DATA, 0,
			0, 0);
	}
	else {
		outStr = "Send request to server failed";
		return;
	}

	// End the request.
	if (bResults) {
		bResults = WinHttpReceiveResponse(hRequest, NULL);
	}
	else {
		outStr = "Receive response from server failed";
		return;
	}

	// Keep checking for data until there is nothing left.
	if (bResults)
	{
		std::ostringstream ostr;
		do
		{
			// Check for available data.
			dwSize = 0;
			if (!WinHttpQueryDataAvailable(hRequest, &dwSize)) {
				outStr = "Error occurred in data query available.\n";
				return;
			}

			// Allocate space for the buffer.
			pszOutBuffer = new char[dwSize + 1];
			if (!pszOutBuffer)
			{
				outStr = "Out of memory\n";
				dwSize = 0;
				return;
			}
			else
			{
				// Read the data.
				ZeroMemory(pszOutBuffer, dwSize + 1);

				if (!WinHttpReadData(hRequest, (LPVOID)pszOutBuffer,
					dwSize, &dwDownloaded)) {
					outStr = "Error in read data from server";
					return;
				}
				else {
					// add buffer to output
					ostr << pszOutBuffer;
				}

				// Free the memory allocated to the buffer.
				delete[] pszOutBuffer;
			}
		} while (dwSize > 0);
		outStr = ostr.str();
	}


	// Report any errors.
	if (!bResults) {
		outStr = "Error has occurred.";
	}

	// Close any open handles.
	if (hRequest) WinHttpCloseHandle(hRequest);
	if (hConnect) WinHttpCloseHandle(hConnect);
	if (hSession) WinHttpCloseHandle(hSession);

}

void connectToSmartsearch(const std::string & layer,
	const std::string & component,
	const std::string & searchTerms,
	std::string & outStr)
{
	// compose the httpserver string from server, username, role, layer, component, searchTerms
	std::wstring httpserver = std::wstring(server.begin(), server.end());

	std::ostringstream ostr;
	// TODO actual search query should in the form below.
	//ostr << "username=" << username << "&role=" << role << "&layer=" << layer << "&component=" << component << "&search_term=" << searchTerms;
	// for testing, use param1 and param2.
	std::string param1 = layer;
	std::string param2 = component;

	ostr << servlet << "?param1=" << param1 << "&param2=" << param2 << "&search_term=" << searchTerms << "&role=" << role << "&text_only=true";

	std::string resourcestr = ostr.str();
	std::wstring resource = std::wstring(resourcestr.begin(), resourcestr.end());

	connect(httpserver,
		port,
		resource,
		WINHTTP_FLAG_REFRESH,
		outStr);
}

void openInBrowser(const std::string & layer,
	const std::string & component,
	const std::string & searchTerms)
{
	std::ostringstream ostr;
	std::string param1 = layer;
	std::string param2 = component;

	ostr << "http://" << server << ":" << port << "/" << servlet << "?param1=" << param1 << "&param2=" << param2 << "&search_term=" << searchTerms << "&role=" << role;
	std::string resourcestr = ostr.str();
	std::wstring resource = std::wstring(resourcestr.begin(), resourcestr.end());
	std::wstring command = L"open";
	ShellExecute(NULL, command.c_str(), resource.c_str(), NULL, NULL, SW_SHOWDEFAULT);

}