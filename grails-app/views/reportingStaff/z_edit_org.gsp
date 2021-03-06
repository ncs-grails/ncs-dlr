

<%@ page import="edu.umn.ncs.ReportingStaff" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'reportingStaff.label', default: 'ReportingStaff')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportingStaffInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportingStaffInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${reportingStaffInstance?.id}" />
                <g:hiddenField name="version" value="${reportingStaffInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="username"><g:message code="reportingStaff.username.label" default="Username" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'username', 'errors')}">
                                    <g:textField name="username" value="${reportingStaffInstance?.username}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="firstName"><g:message code="reportingStaff.firstName.label" default="First Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'firstName', 'errors')}">
                                    <g:textField name="firstName" value="${reportingStaffInstance?.firstName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="middleInit"><g:message code="reportingStaff.middleInit.label" default="Middle Init" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'middleInit', 'errors')}">
                                    <g:textField name="middleInit" value="${reportingStaffInstance?.middleInit}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="lastName"><g:message code="reportingStaff.lastName.label" default="Last Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'lastName', 'errors')}">
                                    <g:textField name="lastName" value="${reportingStaffInstance?.lastName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="laborCategory"><g:message code="reportingStaff.laborCategory.label" default="Labor Category" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'laborCategory', 'errors')}">
                                    <g:select name="laborCategory.id" from="${edu.umn.ncs.LaborCategory.list()}" optionKey="id" value="${reportingStaffInstance?.laborCategory?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="email"><g:message code="reportingStaff.email.label" default="Email" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${reportingStaffInstance?.email}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="isTestAccount"><g:message code="reportingStaff.isTestAccount.label" default="Is Test Account" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'isTestAccount', 'errors')}">
                                    <g:checkBox name="isTestAccount" value="${reportingStaffInstance?.isTestAccount}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="reportsEffort"><g:message code="reportingStaff.reportsEffort.label" default="Reports Effort" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'reportsEffort', 'errors')}">
                                    <g:checkBox name="reportsEffort" value="${reportingStaffInstance?.reportsEffort}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="userCreated"><g:message code="reportingStaff.userCreated.label" default="User Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'userCreated', 'errors')}">
                                    <g:textField name="userCreated" value="${reportingStaffInstance?.userCreated}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="appCreated"><g:message code="reportingStaff.appCreated.label" default="App Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'appCreated', 'errors')}">
                                    <g:textField name="appCreated" value="${reportingStaffInstance?.appCreated}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="assignedEfforts"><g:message code="reportingStaff.assignedEfforts.label" default="Assigned Efforts" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingStaffInstance, field: 'assignedEfforts', 'errors')}">
                                    
<ul>
<g:each in="${reportingStaffInstance?.assignedEfforts?}" var="a">
    <li><g:link controller="assignedEffort" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="assignedEffort" action="create" params="['reportingStaff.id': reportingStaffInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'assignedEffort.label', default: 'AssignedEffort')])}</g:link>

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
