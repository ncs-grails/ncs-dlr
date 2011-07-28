<%@ page import="edu.umn.ncs.ReportingStaff" %>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
        <g:set var="entityName" value="${message(code: 'reportingStaff.label', default: 'ReportingStaff')}" />
		<title>DLR Reporting Staff</title>		
    </head>
    
    <body>
    
		<g:ifAnyGranted role="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE">

			<!-- NAVIGATION -->
			<p class="breadcrumbs">
				<g:link controller="main" action="index">DLR Home</g:link>
					&nbsp;&nbsp;&gt&nbsp;&nbsp;
				<g:link controller="applicationManagement" action="index">Application Management</g:link>
					&nbsp;&nbsp;&gt&nbsp;&nbsp;
				Reporting Staff
					&nbsp;
				[&nbsp;
				<g:link controller="reportingStaff" action="list">Reports Effort</g:link>&nbsp;&nbsp;::&nbsp;&nbsp; 
				<span class="fontMaroon">Does Not Report Effort</span>&nbsp;&nbsp;::&nbsp;&nbsp; 
				<g:link controller="reportingStaff" action="create">Add</g:link>
				&nbsp;]
			</p>
	
			<h1>Reporting Staff: Does Not Report Effort</h1>
	
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			
			<table>	    
				<thead>
				    <tr>
				    	<th class="basic" colspan="7"><strong>Staff Total = ${doesNotReportEffortStaffInstanceCount}</strong></th>
				    </tr>
				    <tr>
						<g:sortableColumn class="basic" property="id" title="${message(code: 'reportingStaff.id.label', default: 'Id')}" />						
						<g:sortableColumn class="basic" property="lastName" title="${message(code: 'reportingStaff.lastName.label', default: 'Full Name')}" />
						<g:sortableColumn class="basic" property="laborCategory" title="${message(code: 'reportingStaff.laborCategory.label', default: 'Labor Category')}" />
						<g:sortableColumn class="basic" property="email" title="${message(code: 'reportingStaff.email.label', default: 'Email')}" />
						<g:sortableColumn class="basic" property="dateCreated" title="${message(code: 'reportingStaff.dateCreated.label', default: 'Created')}" />			    
				    </tr>
				</thead>			
		        <tbody>
			        <g:each in="${doesNotReportEffortStaffInstanceList}" status="i" var="doesNotReportEffortStaffInstanceList">
			        	<g:if test="${doesNotReportEffortStaffInstanceList.isTestAccount}">
			            	<tr class="backgroundColorGainsboro">		        	
			        	</g:if>
			        	<g:else>
				            <tr>
			        	</g:else>
			                <td class="basic" style="text-align:right;">
			                	${fieldValue(bean: doesNotReportEffortStaffInstanceList, field: "id")}
			                </td>
			                <td class="basic">
			                	<span class="nowrap">
			                		<g:link action="edit" id="${doesNotReportEffortStaffInstanceList.id}">${fieldValue(bean: doesNotReportEffortStaffInstanceList, field: "fullNameLFM")}</g:link>
			                	</span>
			                </td>
			                <td class="basic">${fieldValue(bean: doesNotReportEffortStaffInstanceList, field: "laborCategory")}</td>
			                <td class="basic">${fieldValue(bean: doesNotReportEffortStaffInstanceList, field: "email")}</td>
			                <td class="basic">
			                	<g:formatDate date="${fieldValue(bean: doesNotReportEffortStaffInstanceList, field: "dateCreated")}" format="MM-dd-yyyy" />&nbsp;(${fieldValue(bean: doesNotReportEffortStaffInstanceList, field: "userCreated")})
			                </td>
			            </tr>
			        </g:each>		        
		        </tbody>
		    </table>
		    <div class="tableFooterNote">Table row highlighting scheme: <span class="backgroundColorGainsboro">is a TEST ACCOUNT</span>.</div>

		</g:ifAnyGranted>

	    <div class="pageSpacing"> </div>

	</body>

</html>
