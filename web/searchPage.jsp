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
   
  <form action="SearchEngineServlet">
  <select class="form-control" name="role">
                            <option value="Role" selected='selected'> Role </option>
                            <option value="Architect"> Architect </option>
                            <option value="Designer"> Designer </option>
                            <option value="Tester"> Tester </option>
  </select>
  
  <br>
  <br>
  <div class="row">
  <div class="col-lg-6">
  <div class="input-group">
  <span class="input-group-addon" id="param1">param1</span>
  <input type="text" class="form-control" placeholder="weight1" aria-describedby="basic-addon1" name="param1">
</div>
</div>

<div class="col-lg-6">
<div class="input-group">
  <span class="input-group-addon" id="param2">param2</span>
  <input type="text" class="form-control" placeholder="weight2" aria-describedby="basic-addon1" name="param2">
</div>
</div>
</div>
     
    <br>
	<br>
        
        <select class="form-control" name="layer">
                            <option value="Layer" selected='selected'> Layer </option>
                            <option value="Configuration Layer Relative to Full Chip"> Configuration Layer Relative to Full Chip </option>
                            <option value="Fuse Info Layer for Test Boundary"> Fuse Info Layer for Test Boundary </option>
                            <option value="Tester Precondition"> Tester Precondition </option>
                            <option value="State Equation Computation Layer"> State Equation Computation Layer</option>
                            <option value="IP Configuration Layer"> IP Configuration Layer</option>
                            <option value="Algorithm/Structured Data Layer"> Algorithm/Structured Data Layer</option>
                            <option value="Observability Layer"> Observability Layer</option>
                            <option value="Output Management"> Output Management</option>
                            <option value="Checking: Parametric or Fixed Value"> Checking: Parametric or Fixed Value</option>
        </select>
        <br>
        <br>
        
	
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