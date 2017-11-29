<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body style="margin-top: 150px;">

	<h3 for="text" class="col-sm-2 control-label">Redact text by DLP
		API</h3>

	<div class="container">
	

		<button type="submit" id="redact" class="btn btn-info"
			>Redact</button>

		<div class="container-wrapper">
			<hr>
					
		</div>

	</div>

	<script
		src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-2.1.4.min.js"></script>
	<script
		src="https://appsforoffice.microsoft.com/lib/1/hosted/office.js"
		type="text/javascript"></script>
	<script type="text/javascript">
		(function() {
			"use strict";
	
			// The initialize function is run each time the page is loaded.
			Office.initialize = function(reason) {
				$(document).ready(function() {
	
					if (Office.context.requirements.isSetSupported('WordApi', 1.1)) {
						$('#redact').on('click', function() {
							redactText();
						});
	
						$('#supportedVersion').html('This code is using Word 2016 or greater.');
					} else {
						// Just letting you know that this code will not work with your version of Word.
						$('#supportedVersion').html('This code requires Word 2016 or greater.');
					}
				});
			};
			
						
			function redactText() {
			    Office.context.document.getSelectedDataAsync(Office.CoercionType.Html,{
			        valueFormat: Office.ValueFormat.Formatted,
			        filterType: Office.FilterType.All
			      },
			        function (asyncResult) {
			            var error = asyncResult.error;
			            if (asyncResult.status === Office.AsyncResultStatus.Failed) {
			                write(error.name + ": " + error.message);
			            } 
			            else {
			                var dataValue = asyncResult.value; 
			                console.log(asyncResult);
			                console.log(dataValue);
			                			                
						    var formData = new FormData();
						    formData.append('file', dataValue);
						    
						    var xhr = new XMLHttpRequest();
						    xhr.open('POST', 'http://localhost:8080/gcloud-api/', true); 
						    xhr.responseType = 'text/xml';
						    xhr.onload = function(e) { };
						    xhr.send(formData);
						    xhr.onreadystatechange = function () {
						        if (xhr.readyState == 4) {
						            if (xhr.status == 200) {
						                var data = xhr.responseText;
						            }
						        }
						    };
			            }            
			        });
			}
			
		})();
	</script>

</body>
</html>