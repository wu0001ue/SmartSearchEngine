/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author maywu
 */
@WebServlet(name = "SearchEngineServlet",
        urlPatterns = {"/SearchEngineServlet"})
public class SearchEngineServlet extends HttpServlet {
    LuceneTester lt = null;
    @Override
    public void init(){
        lt = new LuceneTester();
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
        
        String nextView = "";
        if (searchTerm == null ) {
            nextView = "searchPage.jsp";
            request.setAttribute("testTerm", "Nothing has been entered");
        } else {
            try {
                //lt.search(searchTerm);
                ArrayList<String> results = lt.testSearch(searchTerm);
                request.setAttribute("searchResult", results); 
                request.setAttribute("searchTest", searchTerm); //newly added line
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
