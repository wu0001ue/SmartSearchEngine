/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yinting
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@WebServlet(urlPatterns = {"/Feedback"})
public class Feedback extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>User Input</title>");            
            out.println("</head>");
            out.println("<body>");
            String doc1 = request.getParameter("doc1");
            String doc2 = request.getParameter("doc2");
            String doc3 = request.getParameter("doc3");
            String results= request.getParameter("feedbackbut");
            writeFeedback(doc1,doc2,doc3,results);
            out.println("User Input Captured!<br>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    public void writeFeedback(String doc1,String doc2,String doc3,String results){
        try{
        UUID uid=UUID.randomUUID();
        //PrintWriter out = new PrintWriter("C:\\Users\\lorraine\\Desktop\\myfile.txt");
        PrintWriter out= new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\lorraine\\Desktop\\querylog.txt", true)));
        PrintWriter out2= new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\lorraine\\Desktop\\rating.txt", true)));
        //append to the existing feedback doc or open a new file
	out.println("uuid: "+uid);
        out.println(results);
        out2.println("uuid: "+uid);
        out2.println("Rank 1: "+ doc1);
        out2.println("Rank 2: "+doc2);
        out2.println("Rank 3: "+doc3);
        out.close();
        out2.close();
        }catch (IOException e){
        System.out.println("IO Error");
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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
