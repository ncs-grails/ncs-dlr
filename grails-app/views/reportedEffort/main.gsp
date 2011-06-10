<!-- if there is assigned effort that has not been committed yet, then user must complete dlr -->
<g:if test="${assignedEffortInstance && ! assignedEffortInstance?.dateCommitted }">

  <g:form name="reportedEffort-main" method="post">

    <g:hiddenField name="reportingStaff.id" value="${reportingStaffInstance?.id}" />
    <g:hiddenField name="reportingPeriod.id" value="${reportingPeriodInstance?.id}" />
    <g:hiddenField name="assignedEffort.id" value="${assignedEffortInstance?.id}" />

    <!-- REPORTED EFFORT STATUS (assignedEffort.show) -->
    <g:include 
      controller="assignedEffort" 
      action="show" 
      id="${assignedEffortInstance.id}" 
      params="${[isForm: true]}" 
    />

    <div class="clearCenterPadding">
      
      <!-- ADD button -->
      <g:submitToRemote 
        class="buttonBasic" 
        value="ADD" 
        url="${[controller:'reportedEffort',action:'create' ]}" 
        update="remoteFormContainer" 
      />
      
      <!-- DELETE button -->
      <g:submitToRemote 
        class="buttonBasic" 
        value="DELETE" 
        url="${[controller:'reportedEffort',action:'delete' ]}" 
        update="remoteFormContainer" 
      />

    </div>


  </g:form>

</g:if>
