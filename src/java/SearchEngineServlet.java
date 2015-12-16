/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author maywu
 */
@WebServlet(name = "SearchEngineServlet", urlPatterns = {"/SearchEngineServlet"})
public class SearchEngineServlet extends HttpServlet {
    static final String CONFIG_FILE = "config.txt";
    LuceneTester lt = null;
    HashMap<String, String> params = new HashMap<String, String>();
    @Override
    public void init(){
        lt = new LuceneTester();
        Path currentPath = Paths.get("");
        String s = currentPath.toAbsolutePath().toString();
        System.out.println("Current path: " + s);
        readConfigFile();
    }
    
    private void readConfigFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
            String line = null;
            while ((line = br.readLine()) != null){
                int d = line.indexOf(":");
                String field = line.substring(0, d);
                String value = line.substring(d + 1);
                params.put(field, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String param1 = request.getParameter("param1"); // Place holder
        String param2 = request.getParameter("param2"); // Place holder
        String searchTerm = request.getParameter("search_term");
        String role = request.getParameter("role");
        String layer = request.getParameter("layer");
        String textOnly = request.getParameter("text_only");
        
        String nextView = "";
        if (searchTerm == null ) {
            nextView = "searchPage.jsp";
            request.setAttribute("testTerm", "Nothing has been entered");
        } else {
            try {
                //lt.search(searchTerm);
                boolean buildIndex = params.get("rebuildIndex").equalsIgnoreCase("true");
                ArrayList<String> results = lt.testSearch(searchTerm, buildIndex, textOnly);
                if (textOnly != null && textOnly.equalsIgnoreCase("true")) {
                    PrintWriter pw = response.getWriter();
                    for (String t : results) {
                        pw.write(t);
                        pw.write('\n');
                    }
                    pw.flush();
                    pw.close();
                }
                request.setAttribute("searchResult", results); 
                request.setAttribute("searchTest", searchTerm); //newly added line
                request.setAttribute("role",role);
                request.setAttribute("layer",layer);
                request.setAttribute("param1",param1);
                request.setAttribute("param2",param2);
                nextView = "resultPage.jsp";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                    
        // Transfer control over the the correct "view"
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response); 
          
    }

}
