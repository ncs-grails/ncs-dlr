<!-- if there is assigned effort that has not been committed yet, then user must complete dlr -->
<g:if test="${assignedEffortInstance && ! assignedEffortInstance?.dateCommitted }">

  <g:form name="reportedEffort-main" method="post" class="form">

    <!-- display (assignedEffort.show): 1) message boxes: ASSIGNED, REPORTED COMMITED message and 2) EFFORT REPORTED so far -->
    <g:include controller="assignedEffort" action="show" id="${assignedEffortInstance.id}" params="${[isForm: true]}" />

    <!-- display ADD, DELETE, EDIT and COMMIT buttons -->
    <div class="clearCenterPadding">
      <g:submitToRemote class="buttonBasic" url="${[controller:'reportedEffort',action:'create' ]}" value="ADD" update="remoteFormContainer" />
    </div>

  </g:form>

</g:if>
