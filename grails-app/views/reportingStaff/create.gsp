

<%@ page import="edu.umn.ncs.ReportingStaff" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'reportingStaff.label', default: 'ReportingStaff')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportingStaffInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportingStaffInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
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
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
