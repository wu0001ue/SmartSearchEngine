// GT_HelloWorldWin32.cpp
// compile with: /D_UNICODE /DUNICODE /DWIN32 /D_WINDOWS /c

#include <windows.h>
#include <stdlib.h>
#include <string.h>
#include <tchar.h>
#include <winhttp.h>
#include <shlobj.h>
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>

using namespace std;

// Global variables

// The main window class name.
static TCHAR szWindowClass[] = _T("win32app");

// The string that appears in the application's title bar.
static TCHAR szTitle[] = _T("Connect to Smart Search");

HINSTANCE hInst;

// Forward declarations of functions included in this code module:
LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);

std::string configFileName(".\\SmartSearch.ini");

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

void getValueFromLine(const std::string line,
	std::string & value)
{
	size_t beg = 1 + line.find("=");
	size_t len = line.length() - beg;
	value = line.substr(beg, len);
}

bool readConfigFile(const std::string & filePath)
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
		return true;
	}
	else {
		return false;
	}
}

void openBrowser(const std::string & layer,
	const std::string & component,
	const std::string & searchTerms)
{
	std::ostringstream ostr;
	std::string param1 = "my param1";
	std::string param2 = "my param2";

	ostr << "http://" << server << ":" << port << "/" << servlet << "?param1=" << param1 << "&param2=" << param2 << "&search_term=" << searchTerms << "&role=" << role;

	ShellExecute(NULL, "open", (LPCSTR)ostr.str().c_str(), NULL, NULL, SW_SHOWDEFAULT);
}

void smartSearch(HWND & hWnd)
{
	HGLOBAL   hglb;
	LPTSTR    lptstr;
	HWND hwnd;
	std::string searchTerms;

	if (!IsClipboardFormatAvailable(CF_TEXT))
	{
		return;
	}
	if (!OpenClipboard(hWnd))
	{
		return;
	}

	hglb = GetClipboardData(CF_TEXT);
	if (hglb != NULL)
	{
		lptstr = (LPTSTR)GlobalLock(hglb);
		if (lptstr != NULL)
		{
			// Call the application-defined ReplaceSelection 
			// function to insert the text and repaint the 
			// window. 

			searchTerms = lptstr;
			GlobalUnlock(hglb);
		}
	}
	CloseClipboard();

	openBrowser("my layer", "my component", searchTerms);

	// ShellExecute(NULL, "open", "http://localhost:8080/GuestBook/guest?param1=neis&param2=buis&search_term=oldu yav muis&role=roleis", NULL, NULL, SW_SHOWDEFAULT);
}

int WINAPI WinMain(HINSTANCE hInstance,
	HINSTANCE hPrevInstance,
	LPSTR lpCmdLine,
	int nCmdShow)
{
	WNDCLASSEX wcex;

	wcex.cbSize = sizeof(WNDCLASSEX);
	wcex.style = CS_HREDRAW | CS_VREDRAW;
	wcex.lpfnWndProc = WndProc;
	wcex.cbClsExtra = 0;
	wcex.cbWndExtra = 0;
	wcex.hInstance = hInstance;
	wcex.hIcon = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_APPLICATION));
	wcex.hCursor = LoadCursor(NULL, IDC_ARROW);
	wcex.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	wcex.lpszMenuName = NULL;
	wcex.lpszClassName = szWindowClass;
	wcex.hIconSm = LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_APPLICATION));

	if (!RegisterClassEx(&wcex))
	{
		MessageBox(NULL,
			_T("Call to RegisterClassEx failed!"),
			_T("Win32 Guided Tour"),
			NULL);

		return 1;
	}

	hInst = hInstance; // Store instance handle in our global variable

					   // The parameters to CreateWindow explained:
					   // szWindowClass: the name of the application
					   // szTitle: the text that appears in the title bar
					   // WS_OVERLAPPEDWINDOW: the type of window to create
					   // CW_USEDEFAULT, CW_USEDEFAULT: initial position (x, y)
					   // 500, 100: initial size (width, length)
					   // NULL: the parent of this window
					   // NULL: this application does not have a menu bar
					   // hInstance: the first parameter from WinMain
					   // NULL: not used in this application
	HWND hWnd = CreateWindow(
		szWindowClass,
		szTitle,
		WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, CW_USEDEFAULT,
		500, 100,
		NULL,
		NULL,
		hInstance,
		NULL
		);

	if (!hWnd)
	{
		MessageBox(NULL,
			_T("Call to CreateWindow failed!"),
			_T("Win32 Guided Tour"),
			NULL);

		return 1;
	}

	// The parameters to ShowWindow explained:
	// hWnd: the value returned from CreateWindow
	// nCmdShow: the fourth parameter from WinMain
	ShowWindow(hWnd,
		nCmdShow);
	UpdateWindow(hWnd);

	if (!readConfigFile(configFileName))
	{
		MessageBox(NULL,
			_T("Make sure SmartSearch.ini file is installed."),
			_T("Cannot read the configuration file!"),
			NULL);

		return 1;
	}

	smartSearch(hWnd);

	/* No main message loop:
	MSG msg;
	while (GetMessage(&msg, NULL, 0, 0))
	{
	TranslateMessage(&msg);
	DispatchMessage(&msg);
	}

	return (int)msg.wParam;
	*/
	return 0;

}

//
//  FUNCTION: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  PURPOSE:  Processes messages for the main window.
//
//  WM_PAINT    - Paint the main window
//  WM_DESTROY  - post a quit message and return
//
//
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	PAINTSTRUCT ps;
	HDC hdc;
	TCHAR greeting[] = _T("Thanks for using smart search");

	switch (message)
	{
	case WM_PAINT:
		hdc = BeginPaint(hWnd, &ps);

		// Here your application is laid out.
		// For this introduction, we just print out "Hello, World!"
		// in the top left corner.
		TextOut(hdc,
			5, 5,
			greeting, _tcslen(greeting));
		// End application-specific layout section.

		EndPaint(hWnd, &ps);
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hWnd, message, wParam, lParam);
		break;
	}

	return 0;
}