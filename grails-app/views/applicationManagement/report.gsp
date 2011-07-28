<%@ page contentType="text/html;charset=UTF-8" %>

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
		<title>Report Generation</title>
	</head>

	<body>

		<g:ifAnyGranted role="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE">
		
			<!-- NAVIGATION -->
	    	<p class="breadcrumbs">
	      		<g:link controller="main" action="index">DLR Home</g:link>
	      		&nbsp;&nbsp;&gt&nbsp;&nbsp;
	      		<g:link controller="applicationManagement" action="list">Application Management</g:link>      
	      		&nbsp;&nbsp;&gt&nbsp;&nbsp;
	      		<span class="fontMaroon">Report Generation</span>
	    	</p>
	      
			<h1>Report Generation</h1>
	
			<fieldset>            
	      
				<g:form controller="export" action="reportingPeriod">
	
	        		<div class="clearCenterPadding">
	            
	          			<span class="controlBackground">              
	
							<!--  REPORTING PERIOD -->
							<span class="spacing">
							  	<label for="id">Reporting Period</label>          
							  	<g:select 
							 		class="basic" 
							   		name="id" 
							   		from="${reportingPeriodInstanceList}" 
							 	/>                
							</span>
	                         
							<!--  OUTPUT FORMAT -->
							<span class="spacing">
							  	<label for="format">Output Format</label>
							  	<g:select 
							 		class="basic" 
							   		name="format" 
							   		from="${reportFormats }" 
							 	/>
							</span>              
	
							<!-- GENERATE BUTTON -->
	            			<span class="spacing">
	              				<g:submitButton class="buttonBasic" name="submit" value="GENERATE" />
	            			</span>              
	
	          			</span>
	            
	       			</div>
	
				</g:form>
	      
			</fieldset>

		</g:ifAnyGranted>
    
		<div class="pageSpacing"> </div>

	</body>

</html>