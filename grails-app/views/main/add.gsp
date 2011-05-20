<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS Direct Labor Reporting</title>
  </head>

  <body>
    
    <h1>Direct Labor Reporting (ADD)</h1>
    
    <g:form name="directLaborReporting" method="post" controller="main">

      <!-- FIELDSET -->
      <fieldset class="roundRectSilver">					
        
        <!-- LEGEND -->
        <legend class="legendMaroon"><g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></legend>

        <!-- if there are ASSIGNED effort that has not been committed yet, user must complete dlr -->
        <g:if test="${assignedEffortInstance && !committedDateInstance}">
          <div class="clearCenterPadding">
            <span class="messageBoxOrange">Assigned: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/></span>          
            &nbsp;&nbsp;&nbsp;
            <span class="messageBoxOrange">Reported: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/></span>
            &nbsp;&nbsp;&nbsp;
            <span class="messageBoxRed">Committed: 
              <g:if test="${!committedDateInstance}">0%</g:if>
            </span>
          </div>
        </g:if>        
        
        <div class="clearCenterPadding">
          <span class="fontPurpleBold">Study Activity</span>&nbsp;
            <g:select class="basic"
              name="studyActivityInstance.id"
              from="${studyActivitySelectionList}"
              optionKey="id"
              optionValue="name"
              value="${studyActivitySelectionList.id}" 
              noSelection="${['null':'-- Choose --']}"/>
          <!--
          Task&nbsp;&nbsp;
          Effort
          -->
              
          
        </div>
        
        
        
        
        
      </fieldset>
            
    </g:form>      
      
      <!-- TODO: display previous effort reported -->
      <h2>Past COMMITTED Effort Reported</h2>

  </body>
  
</html>
