<%@ page contentType="text/html;charset=UTF-8" %>

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
		<title>Report Generation</title>
	</head>

	<body>

		<sec:ifAnyGranted roles="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE">
		
			<!-- NAVIGATION -->
	    	<p class="breadcrumbs">
	      		<g:link controller="main" action="index">DLR Home</g:link>
	      		&nbsp;&nbsp;&gt&nbsp;&nbsp;
	      		<g:link controller="applicationManagement" action="list">Application Management</g:link>      
	      		&nbsp;&nbsp;&gt&nbsp;&nbsp;
	      		<span class="fontMaroon">Report Generation</span>
	    	</p>
	      
			<!--  PAGE TITLE -->	      
			<h1>Report Generation</h1>
	
			<fieldset>            
	      
				<!-- BEGIN FORM -->
				<g:form controller="export" action="generateReport">
	
	        		<div class="clearCenterPadding">
	            
	          			<span class="controlBackground">              
	
							<!--  REPORT TYPE -->
						  	<label for="report_type_id">Report</label>          
						  	<g:select 
						 		class="basic" 
						   		name="report_type_id"
						   		optionKey="report_type_id"
						   		optionValue="name"  
						   		from="${reportTypeInstanceList}"
						 	/>                

							<!--  OUTPUT FORMAT -->
						  	<label for="format">Output</label>
						  	<g:select 
						 		class="basic" 
						   		name="format" 
						   		from="${reportFormatList}" 
						 	/>

							<!--  REPORTING PERIOD -->
						  	<label for="reporting_period_id">Period</label>          
						  	<g:select 
						 		class="basic" 
						   		name="reporting_period_id"
						   		optionKey="reporting_period_id"
						   		optionValue="name"  
						   		from="${reportingPeriodInstanceList}"
						 	/>                

							<!-- GENERATE BUTTON -->
              				<g:submitButton 
              					class="buttonBasic" 
              					name="submit" 
              					value="GENERATE" 
              				/>
	              					
	          			</span>
	            
	       			</div>
	
				</g:form>
				<!-- END FORM -->
	      
			</fieldset>

		</sec:ifAnyGranted>
    
		<!-- FOOTER SPACING -->
		<div class="pageSpacing"> </div>

	</body>

</html>