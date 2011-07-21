

<%@ page import="edu.umn.ncs.EtdlrTask" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'etdlrTask.label', default: 'EtdlrTask')}" />
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
            <g:hasErrors bean="${etdlrTaskInstance}">
            <div class="errors">
                <g:renderErrors bean="${etdlrTaskInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="etdlrTask.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: etdlrTaskInstance, field: 'name', 'errors')}">
                                    <g:textArea name="name" cols="40" rows="5" value="${etdlrTaskInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="obsolete"><g:message code="etdlrTask.obsolete.label" default="Obsolete" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: etdlrTaskInstance, field: 'obsolete', 'errors')}">
                                    <g:checkBox name="obsolete" value="${etdlrTaskInstance?.obsolete}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="userCreated"><g:message code="etdlrTask.userCreated.label" default="User Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: etdlrTaskInstance, field: 'userCreated', 'errors')}">
                                    <g:textField name="userCreated" value="${etdlrTaskInstance?.userCreated}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="appCreated"><g:message code="etdlrTask.appCreated.label" default="App Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: etdlrTaskInstance, field: 'appCreated', 'errors')}">
                                    <g:textField name="appCreated" value="${etdlrTaskInstance?.appCreated}" />
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
