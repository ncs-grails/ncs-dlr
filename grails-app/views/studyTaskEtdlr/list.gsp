
<%@ page import="edu.umn.ncs.StudyTaskEtdlr" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'studyTaskEtdlr.id.label', default: 'Id')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${studyTaskEtdlrInstanceList}" status="i" var="studyTaskEtdlrInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${studyTaskEtdlrInstance.id}">${fieldValue(bean: studyTaskEtdlrInstance, field: "id")}</g:link></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${studyTaskEtdlrInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
