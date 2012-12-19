<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
		<title>Reference Invoice Number</title>
		<g:javascript src="reportingPeriod.js" />
	</head>
  
	<body>
    
		<sec:ifAnyGranted roles="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE">

	    	<!-- Navigation -->
			<p class="breadcrumbs">
				<g:link controller="main" action="index">DLR Home</g:link>
				&nbsp;&nbsp;&gt&nbsp;&nbsp;
				<g:link controller="applicationManagement" action="list">Application Management</g:link>      
				&nbsp;&nbsp;&gt&nbsp;&nbsp;
				<span class="fontMaroon">Reference Invoice Number</span>		
			</p>
		
			<!-- Page Title -->
			<h1>Reference Invoice Number</h1>
	    
	            <g:if test="${flash.message}">
		            <div class="message">${flash.message}</div>
	            </g:if>
	            
	            <div id="successMessage" style="display:none;"></div>
				<table>
			
					<thead>
						<tr>
							<th class="basic">Period Id</th>
							<th class="basic">Period Description</th>
							<th class="basic">RIN</th>
							<th class="basic">Report Prepared Date</th>
						</tr>
					</thead>
			
			        <tbody>
						<g:each var="rp" in="${reportingPeriodInstance}">
							<tr>
								<td class="basic" style="text-align:right;">${rp.id}</td>
								<td class="basic" style="text-align:right;"><g:formatDate date="${rp.periodDate}" format="MMMM yyyy" /></td>
								<td class="basic" style="text-align:right;">
									<g:if test="${rp.preparedDate}" >${rp.referenceInvoiceNumber}</g:if>
									<g:else>
										<g:remoteField 
											class="textfieldBasic alignRight" 
											action="updateRin" 
											controller="reportingPeriod" 
											update="successMessage" 
											onSuccess="saveSuccess('referenceInvoiceNumber-${rp.id}');"
											onFailure="saveFail('referenceInvoiceNumber-${rp.id}');"
											name="referenceInvoiceNumber-${rp.id}" 
											id="${rp.id}" 
											size="12"
											paramName="referenceInvoiceNumber" 
											value="${rp.referenceInvoiceNumber}"/>
											
									</g:else>
								</td>
								<td class="basic" style="text-align:right;"><g:formatDate date="${rp.preparedDate}" format="MM-dd-yyyy" /></td>
							</tr>
						</g:each>
			        </tbody>
			
				</table>
				
			</sec:ifAnyGranted>

		<div class="pageSpacing"> </div>

	</body>

  
</html>
