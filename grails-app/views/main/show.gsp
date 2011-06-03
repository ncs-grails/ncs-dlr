<%@ page contentType="text/html;charset=UTF-8" %>
<html>  
  
  <head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
    <title>NCS Direct Labor Reporting</title>
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

      <h2>
        <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /> Effort Report
      </h2>

      <!-- logic for displaying various messages -->
      
      <!-- if there is NO assigned effort for this period, let user know -->
      <g:if test="${!assignedEffortInstance}">
        <p>You have not been assigned effort to report for <strong>
          <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>.
        </p>              
      </g:if>
      
      <!-- if this period's assigned effort is already COMMITTED, then user is done with effort reporting -->
      <g:if test="${assignedEffortInstance && committedDateInstance}">
        <p>You have completed your effort reporting for this <strong>
          <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>. Thank you!
        </p>              
      </g:if>
      
      <!-- if there is assigned effort that has not been committed yet, then user must complete dlr -->
      <g:if test="${assignedEffortInstance && !committedDateInstance}">        
                  
          <!-- reportedEffort.create (ADD) -->
          <g:form name="reportedEffort-create" method="post" controller="reportedEffort" action="create" />
          
          <!-- reportedEffort.edit -->
          <g:form name="reportedEffort-edit" method="post" controller="reportedEffort" action="edit" />
                                        
          <!-- main.show -->
          <g:form name="directLaborReporting" method="post" controller="main">

            <!-- display (assignedEffort.show) 
                - message boxes: ASSIGNED, REPORTED COMMITED message 
                - EFFORT REPORTED so far 
            -->
            <div id="showAssignedEffortContainer">
              <g:include controller="assignedEffort" action="show" id="${assignedEffortInstance.id}" />                            
            </div>
            
            <g:hiddenField name="assignedEffort.id" value="${assignedEffortInstance?.id}" />

            <!-- display ADD, DELETE, EDIT and COMMIT buttons -->
            <div id="addDeleteEditCommitControls" class="clearCenterPadding">
                            
              <button id="buttonAdd" class="buttonBasic" >ADD</button>
              <g:submitToRemote class="buttonBasic" url="${[controller:'reportedEffort',action:'delete']}" value="DELETE" />
              <button id="buttonEdit" class="buttonBasic">EDIT</button>
              <g:actionSubmit class="buttonBasic" value="COMMIT" action="commit"/>
              
            </div>          

          </g:form>      
          
          <!-- placeholder for ADD/EDIT sections -->
          <div id="addOrEditForm"></div>
        
      </g:if>        

      <!-- Past COMMITTED EFFORT-->
      <g:include controller="assignedEffort" action="showPast" id="${reportingStaffInstance.id}" />              

    <div class="pageSpacing"> </div>
 
  </body>
  
</html>
