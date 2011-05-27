<%@ page contentType="text/html;charset=UTF-8" %>
<html>  
  
  <head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>NCS Direct Labor Reporting</title>
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'custom.css')}" />
    <g:javascript src="dlr.js" />
    
  </head>

  <body>

      <!-- navigation -->
      <p class="breadcrumbs">
        <g:link controller="applicationManagement" action="index">Application Management</g:link>
      </p>
    
      <h1>Direct Labor Reporting</h1>

      <!-- Greeting -->
      <span class="fontMaroon">Welcome, <strong>${reportingStaffInstance.firstName} ${reportingStaffInstance.lastName}</strong>!</span>

      <!-- logic for displaying various messages -->

      <h2><g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /> Effort Report</h2>
      
      <!-- if there is NO assigned effort for this period, then user does not need to complete dlr -->
      <g:if test="${!assignedEffortInstance}">
        <p>You do not have to report your effort for <strong>
        <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>.</p>              
      </g:if>
      
      <!-- if this period's assigned effort is already COMMITTED, then user is done with effort reporting -->
      <g:if test="${assignedEffortInstance && committedDateInstance}">
        <p>
          You have completed your effort reporting for this <strong>
          <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>. Thank you!
        </p>              
      </g:if>
      
      <!-- if there is assigned effort that has not been committed yet, then user must complete dlr -->
      <g:if test="${assignedEffortInstance && !committedDateInstance}">        
                  
          <!-- form for ADD (Create) page -->
          <g:form name="reportedEffort-create" method="post" controller="reportedEffort" action="create">
          </g:form>
          
          <!-- form for EDIT page -->
          <g:form name="reportedEffort-edit" method="post" controller="reportedEffort" action="edit" />
          
                    
          <!-- form for SHOW page -->
          <g:form name="directLaborReporting" method="post" controller="main">

            <!-- include: to display ASSIGNED, REPORTED COMMITED message boxes, and effort reported so far -->
            <g:include controller="assignedEffort" action="show" id="${assignedEffortInstance.id}" />              


            <!-- display ADD, DELETE, EDIT and COMMIT buttons to display on SHOW page only -->
            <div id="showControls" class="clearCenterPadding">
              <button id="buttonAdd" class="buttonBasic" >ADD</button>
              &nbsp;&nbsp;&nbsp;&nbsp;
              <g:actionSubmit class="buttonBasic" value="DELETE" action="delete"/>
              &nbsp;&nbsp;&nbsp;&nbsp;
              <button id="buttonEdit" class="buttonBasic">EDIT</button>
              &nbsp;&nbsp;&nbsp;&nbsp;
              <g:actionSubmit class="buttonBasic" value="COMMIT" action="commit"/>
            </div>          

          </g:form>      
          
          <!-- placeholder for ADD or EDIT sections -->
          <div id="addOrEditForm"></div>
        
      </g:if>        

      <!-- display Past COMMITTED -->
      <g:include controller="assignedEffort" action="showPast" id="${reportingStaffInstance.id}" />              

    <div class="pageSpacing"> </div>
 
  </body>
  
</html>
