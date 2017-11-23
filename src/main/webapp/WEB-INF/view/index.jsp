<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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

	<div class="container">
		<form:form modelAttribute="api" id="redactform">
			<div class="form-group">
				<label for="text" class="col-sm-2 control-label">Redact text by DLP API</label>
				<div class="col-sm-10">
					<form:textarea path="text" class="form-control" rows="10" id="redacttext" style="display:none;"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">					
				</div>
			</div>
		</form:form>
		
		<button type="submit" id="redact" class="btn btn-default">Redact</button>
		

	</div>
	
	<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-2.1.4.min.js"></script>
    <script src="https://appsforoffice.microsoft.com/lib/1/hosted/office.js" type="text/javascript"></script>
    <script type="text/javascript">
    
    (function () {
        "use strict";

        // The initialize function is run each time the page is loaded.
        Office.initialize = function (reason) {
            $(document).ready(function () {

                if (Office.context.requirements.isSetSupported('WordApi', 1.1)) {
                 $('#redact').on('click',function() {
                        excuteRedact();
                    });
                    
                    $('#submit').on('click',function() {
                    	submitform();
                    });
                    
                    $('#supportedVersion').html('This code is using Word 2016 or greater.');
                }
                else {
                    // Just letting you know that this code will not work with your version of Word.
                    $('#supportedVersion').html('This code requires Word 2016 or greater.');
                }
            });
        };

        function submitform(){
        	  $('#redactform').submit();
        }
        
        function excuteRedact() {
            Word.run(function (context) {
            	 var documentBody = context.document.body;
            	    context.load(documentBody);
            	    return context.sync()
            	    .then(function(){
            	        console.log(documentBody.text);
            	        var myTextArea = $('#redacttext');
            	        myTextArea.text(documentBody.text);
            	        $('#redactform').submit();
            	    })
            	    $('#redactform').submit();
            })
            
        }
    })();
    </script>

</body>
</html>