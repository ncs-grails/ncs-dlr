<g:form name="assignedEffort" method="post">
  
  <g:hiddenField name="id" value="${assignedEffortInstance.id}" />

  <!-- CURRENT REPORTED EFFORT STATUS -->
  <g:include 
    controller="assignedEffort" 
    action="showCurrent" 
    id="${assignedEffortInstance.id}" 
    params="${[isForm: true]}" 
  />

  <!-- BUTTON CONTROLS -->
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
