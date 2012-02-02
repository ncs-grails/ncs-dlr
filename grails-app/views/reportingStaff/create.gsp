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

		<sec:ifAnyGranted roles="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE">
    
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
				<g:link controller="reportingStaff" action="listDoesNotReport">Does Not Report Effort</g:link>&nbsp;&nbsp;::&nbsp;&nbsp; 
				<span class="fontMaroon">Add</span>
				&nbsp;]
			</p>
	        
			<h1>Reporting Staff: Add</h1>
	
	        <div class="body">
	        
				
	        	<!--  ERROR MESSAGES -->
	            <g:if test="${flash.message}">
	            	<div class="message">${flash.message}</div>
	            </g:if>
	            <g:hasErrors bean="${reportingStaffInstance}">
		            <div class="errors">
	    	            <g:renderErrors bean="${reportingStaffInstance}" as="list" />
	        	    </div>
	            </g:hasErrors>
	            
	            <!-- begin FORM -->
	            <g:form action="save" >
	            
	                <div class="dialog">
	                
	                    <table>
	                    
	                        <tbody>
	
								<!-- username -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="username"><g:message code="reportingStaff.username.label" default="Username" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'username', 'errors')}">
	                                    <g:textField name="username" value="${reportingStaffInstance?.username}" />
	                                </td>
	                            </tr>
	                        
								<!-- first name -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="firstName"><g:message code="reportingStaff.firstName.label" default="First Name" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'firstName', 'errors')}">
	                                    <g:textField name="firstName" value="${reportingStaffInstance?.firstName}" />
	                                </td>
	                            </tr>
	                        
								<!-- middle initial -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="middleInit"><g:message code="reportingStaff.middleInit.label" default="Middle Init" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'middleInit', 'errors')}">
	                                    <g:textField name="middleInit" value="${reportingStaffInstance?.middleInit}" />
	                                </td>
	                            </tr>
	                        
								<!-- last name -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="lastName"><g:message code="reportingStaff.lastName.label" default="Last Name" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'lastName', 'errors')}">
	                                    <g:textField name="lastName" value="${reportingStaffInstance?.lastName}" />
	                                </td>
	                            </tr>
	                        
								<!-- labor category -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="laborCategory"><g:message code="reportingStaff.laborCategory.label" default="Labor Category" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'laborCategory', 'errors')}">
	                                    <g:select name="laborCategory.id" from="${edu.umn.ncs.LaborCategory.findAllWhere(obsolete: false)}" optionKey="id" value="${reportingStaffInstance?.laborCategory?.id}" noSelection="['null': '']" />
	                                </td>
	                            </tr>
	                        
								<!-- email -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="email"><g:message code="reportingStaff.email.label" default="Email" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'email', 'errors')}">
										<g:textField name="email" value="${reportingStaffInstance?.email}" />
	                                </td>
	                            </tr>
	                                                
								<!-- reports effort -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="reportsEffort"><g:message code="reportingStaff.reportsEffort.label" default="Reports Effort" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'reportsEffort', 'errors')}">
	                                    <g:checkBox name="reportsEffort" value="${reportingStaffInstance?.reportsEffort}" />
	                                </td>
	                            </tr>
	
								<!-- isTestAccount -->                        
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="isTestAccount"><g:message code="reportingStaff.isTestAccount.label" default="Is Test Account" /></label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'isTestAccount', 'errors')}">
	                                    <g:checkBox name="isTestAccount" value="${reportingStaffInstance?.isTestAccount}" />
	                                </td>
	                            </tr>
	                                                                                                    
	                        </tbody>
	                        
	                    </table>
	                    
	                </div>
	                
	                <!-- SAVE button -->
	                <div class="buttons">
	                    <span class="button">
	                    	<g:submitButton name="save" class="save" value="SAVE" />
	                    </span>
	                </div>
	                
	            <!-- end FORM -->
	            </g:form>
	            
			</sec:ifAnyGranted>
            
			<div class="pageSpacing"> </div>

        </div>        

    </body>
    
</html>
