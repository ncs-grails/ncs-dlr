<%@ page contentType="text/html;charset=UTF-8" %>

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
		<title>NCS DLR Application Management</title>
  	</head>

  	<body>

		<sec:ifAnyGranted roles="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE">

	    	<!-- NAVIGATION -->
	    	<p class="breadcrumbs">
	      		<g:link controller="main" action="index">DLR Home</g:link>
	      			&nbsp;&nbsp;&gt&nbsp;&nbsp;
	      		<span class="fontMaroon">Application Management</span>
	    	</p>
	      
	    	<h1>DLR Application Management</h1>
	
	    	<!-- MENU SELECTION -->
	    	<dl>
	    		<!-- effort assignment -->
	      		<dt class="basicMenu">
	      			<g:link 
	      				controller="assignEffort" 
	      				action="show"
	      			>Effort Assignment
	      			</g:link> - enter assigned effort for reporting period
	      		</dt>
	    		<!-- reference invoice number -->
	      		<dt class="basicMenu">
	      			<g:link 
	      				controller="reportingPeriod" 
	      				action="list"
	      			>Reference Invoice Number
	      			</g:link> - enter the RIN for each reporting period
	      		</dt>
	    		<!-- report generation -->
	      		<dt class="basicMenu">
	      			<g:link 
	      				action="report"
	      			>Report Generation
	      			</g:link> - generate effort reports for dissemination
	      		</dt>
	    		<!-- staff information -->
	      		<dt class="basicMenu">
	      			<g:link 
	      				controller="reportingStaff" 
	      				action="list"
	      			>Staff Information
	      			</g:link> - enter staff information
	      		</dt>
	    	</dl>
	    	
		</sec:ifAnyGranted>

    	<div class="pageSpacing"> </div>

  	</body>

</html>
