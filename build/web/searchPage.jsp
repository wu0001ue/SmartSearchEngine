<%-- 
    Document   : searchPage
    Created on : Nov 3, 2015, 8:59:06 AM
    Author     : maywu
--%>

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
        
        <select class="form-control" name="layer">
                            <option value="0" selected='selected'> Layer </option>
                            <option value="1"> Configuration Layer Relative to Full Chip </option>
                            <option value="2"> Fuse Info Layer for Test Boundary </option>
                            <option value="3"> Tester Precondition </option>
                            <option value="4"> State Equation Computation Layer</option>
                            <option value="5"> IP Configuration Layer</option>
                            <option value="6"> Algorithm/Structured Data Layer</option>
                            <option value="7"> Observability Layer</option>
                            <option value="8"> Output Management</option>
                            <option value="9"> Checking: Parametric or Fixed Value</option>
  </select>
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
                    <%= request.getAttribute("testTerm")%>
		</div>
        </div>
  </div>
  <div class="col-md-2"></div>
</div>
</head>
<body>
</body>
</html>
