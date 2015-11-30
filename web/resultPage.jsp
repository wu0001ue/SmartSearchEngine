<%-- 
    Document   : searchPage
    Created on : Nov 3, 2015, 8:59:06 AM
    Last Modified: Nov 8, 2015
    Author     : maywu, yinting
--%>


<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>CMU Capstone Project</title>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<style>
</style>
<body>
<div class="row">
  <div class="col-md-2"></div>
  <div class="col-md-8">
  <div class="jumbotron">
  <div class="container">
  <h1>Intel Search Engine</h1>
  <p>CMU MISM Capstone Project</p>
   
  
  <select class="form-control" name="role">
                            <option value="0" selected='selected'> Role </option>
                            <option value="1"> Architect </option>
                            <option value="2"> Designer </option>
                            <option value="3"> Tester </option>
  </select>
  
  <br>
  <br>
  <div class="row">
  <div class="col-lg-6">
  <div class="input-group">
  <span class="input-group-addon" id="param1">param1</span>
  <input type="text" class="form-control" placeholder="weight1" aria-describedby="basic-addon1">
</div>
</div>

<div class="col-lg-6">
<div class="input-group">
  <span class="input-group-addon" id="param2">param2</span>
  <input type="text" class="form-control" placeholder="weight2" aria-describedby="basic-addon1">
</div>
</div>
</div>
     
    <br>
	<br>
	<form action="SearchEngineServlet">
	Search Term: <input name="search_term" value=""><br>
	<br>
	<!--<input type="submit" value="Enter Search Term">-->
	<button type="submit" class="btn btn-success">Enter Key Word</button>
	</form>
	<br>
	</div>
	</div>
  
	<div class="panel panel-default">
                <div class="panel-heading"><b>Files Returned</b></div>                
                <div class="panel-body"> 
                    <% String searchTerm=(String) request.getAttribute("searchTest");%> 
                    <% ArrayList<String> results = (ArrayList<String>) request.getAttribute("searchResult"); %>  
                    <% StringBuilder sb = new StringBuilder();
                    sb.append("Search Term: "+searchTerm+"\n");
                    sb.append("Documents Retrieved: ");%>
                    <% for (String r:results) { %>
                    <tr>
                        <td><a href="<%= r.split(",")[0]%>"><%=r.split(",")[0]%> </a></td> <br> 
                        <td><%= r.split(",")[1] %></td> <br>
                    </tr> 
                   
                    <% sb.append(r.split(",")[0]);} %>                          
                <br>
		<p><b>Which are the top 3 most relevant articles?</b></p>
			
                <form class="form-inline" action="Feedback">
                    <div class="form-group">
                        <div class="col-xs-4">
                        <label for="article1">Rank 1</label>
                        <input type="text" class="form-control" id="doc1" name="doc1" placeholder="(article number)">
                        </div>
                        <div class="col-xs-4">
                        <label for="article2">Rank 2</label>
                        <input type="text" class="form-control" id="doc2" name="doc2" placeholder="(article number)">
                        </div>
                        <div class="col-xs-4">
                        <label for="article3">Rank 3</label>
                        <input type="text" class="form-control" id="doc3" name="doc3" placeholder="(article number)">
                        </div>
		<br><br>
                <!--button type="submit" class="btn btn-primary" onclick = "genFile()" id="submitBtn">Send Feedback</button>
                <p id="demo"></p>
                <script>
                    function genFile() {
                        document.getElementById("demo").innerHTML = "Hello World";
                    }
                </script-->
                <button type="submit" class="btn btn-primary"  name="feedbackbut" value="<%=sb.toString()%>">Send Feedback</button>
                </div>
		</form>
        </div>
  </div>
  <div class="col-md-2"></div>
</div>
</head>
<body>
</body>
</html>
