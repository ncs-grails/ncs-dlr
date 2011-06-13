<!-- if there is assigned effort that has not been committed yet, then user must complete dlr -->
<g:if test="${assignedEffortInstance && ! assignedEffortInstance?.dateCommitted }">

  <g:form name="reportedEffort-main" method="post">

    <g:hiddenField name="reportingStaffId" value="${reportingStaffInstance.id}" />
    <g:hiddenField name="reportingPeriodId" value="${reportingPeriodInstance.id}" />
    <g:hiddenField name="assignedEffortId" value="${assignedEffortInstance.id}" />

    <!-- CURRENT REPORTED EFFORT STATUS (assignedEffort.showCurrent) -->
    <g:include 
      controller="assignedEffort" 
      action="showCurrent" 
      id="${assignedEffortInstance.id}" 
      params="${[isForm: true]}" 
    />

    <div class="clearCenterPadding">
      
      <!-- ADD button -->
      <g:submitToRemote 
        class="buttonBasic" 
        value="ADD" 
        url="${[controller:'reportedEffort',action:'add' ]}" 
        update="remoteFormContainer" 
      />
      
      <!-- DELETE button -->
      <g:submitToRemote 
        class="buttonBasic" 
        value="DELETE" 
        url="${[controller:'reportedEffort',action:'delete' ]}" 
        update="remoteFormContainer" 
      />

      <!-- EDIT button -->
      <g:submitToRemote 
        class="buttonBasic" 
        value="EDIT" 
        url="${[controller:'reportedEffort',action:'edit' ]}" 
        update="remoteFormContainer" 
      />
      
      <!-- COMMIT button -->
      <g:submitToRemote 
        class="buttonBasic" 
        value="COMMIT" 
        url="${[controller:'reportedEffort',action:'commit' ]}" 
        update="remoteFormContainer" 
      />

    </div>

  </g:form>

</g:if>
