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
    
		<!-- NAVIGATION -->
		<p class="breadcrumbs">
			<g:link controller="main" action="index">DLR Home</g:link>
				&nbsp;&nbsp;&gt&nbsp;&nbsp;
			<g:link controller="applicationManagement" action="index">Application Management</g:link>
				&nbsp;&nbsp;&gt&nbsp;&nbsp;
			Reporting Staff
				&nbsp;
			[&nbsp;
			<span class="fontMaroonBold">Reports Effort</span>&nbsp;&nbsp;&nbsp;::&nbsp;&nbsp;&nbsp;
			<g:link controller="reportingStaff" action="listDoesNotReport">Does Not Report Effort</g:link>&nbsp;&nbsp;&nbsp;::&nbsp;&nbsp;&nbsp; 
			<g:link controller="reportingStaff" action="create">Add New Staff</g:link>
			&nbsp;]
		</p>

		<h1>Reporting Staff</h1>

		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
				
		<h2>Reports Effort</h2>
		<table>	    
			<thead>
			
			    <tr>
			    	<th class="basic" colspan="7"><strong>Staff Total = ${reportsEffortStaffInstanceCount}</strong></th>
			    </tr>
			    <tr>
					<g:sortableColumn class="basic" property="id" title="${message(code: 'reportingStaff.id.label', default: 'Id')}" />						
					<g:sortableColumn class="basic" property="lastName" title="${message(code: 'reportingStaff.lastName.label', default: 'Last Name')}" />
					<g:sortableColumn class="basic" property="firstName" title="${message(code: 'reportingStaff.firstName.label', default: 'First Name')}" />
					<g:sortableColumn class="basic" property="middleInit" title="${message(code: 'reportingStaff.middleInit.label', default: 'Middle Init')}" />					
					<g:sortableColumn class="basic" property="laborCategory" title="${message(code: 'reportingStaff.laborCategory.label', default: 'Labor Category')}" />
					<g:sortableColumn class="basic" property="email" title="${message(code: 'reportingStaff.email.label', default: 'Email')}" />
					<g:sortableColumn class="basic" property="dateCreated" title="${message(code: 'reportingStaff.dateCreated.label', default: 'Created')}" />			    
			    </tr>
			</thead>			
	        <tbody>
		        <g:each in="${reportsEffortStaffInstanceList}" status="i" var="reportsEffortStaffInstanceList">
		        	<!-- highlight rows in which account is a test account -->
		        	<g:if test="${reportsEffortStaffInstanceList.isTestAccount}"><tr bgcolor="silver"></g:if>
		        	<g:else><tr></g:else>
		                <td class="basic" style="text-align:right;">
		                	<g:link action="edit" id="${reportsEffortStaffInstanceList.id}">
		                		${fieldValue(bean: reportsEffortStaffInstanceList, field: "id")}
		                	</g:link>
		                </td>
		                <td class="basic">
		                	<span class="nowrap">${fieldValue(bean: reportsEffortStaffInstanceList, field: "lastName")}</span>
		                </td>
		                <td class="basic">
		                	<span class="nowrap">${fieldValue(bean: reportsEffortStaffInstanceList, field: "firstName")}</span>
		                </td>
		                <td class="basic">${fieldValue(bean: reportsEffortStaffInstanceList, field: "middleInit")}</td>
		                <td class="basic">${fieldValue(bean: reportsEffortStaffInstanceList, field: "laborCategory")}</td>
		                <td class="basic">${fieldValue(bean: reportsEffortStaffInstanceList, field: "email")}</td>
		                <td class="basic">
		                	<g:formatDate date="${fieldValue(bean: reportsEffortStaffInstanceList, field: "dateCreated")}" format="MM-dd-yyyy" />&nbsp;(${fieldValue(bean: reportsEffortStaffInstanceList, field: "userCreated")})
		                </td>
		            </tr>
		        </g:each>		        
	        </tbody>
	    </table>
	    <div class="tableFooterNote">Grayed-out row indicates account is a TEST ACCOUNT.</div>
	    
	    <div class="pageSpacing"> </div>

	</body>

</html>
