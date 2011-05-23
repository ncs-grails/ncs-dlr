<%@ page contentType="text/html;charset=UTF-8" %>
<g:form name="reportedEffort-create" method="post" controller="reportedEffort">
        
  <div class="clearCenterPadding">

      <div class="fontPurple">

        <!--Study Activity -->
        Study Activity&nbsp;
          <g:select class="basic"
            name="studyActivityInstance.id"
            from="${studyActivitySelectionList}"
            optionKey="id"
            optionValue="name"
            value="${reportedEffortInstance?.activity?.id}" 
            noSelection="${['null':'-- Choose --']}"/>&nbsp;&nbsp;&nbsp;&nbsp;

        <!--Study Task -->
        Study Task&nbsp;
          <g:select class="basic"
            name="studyTaskInstance.id"
            from="${studyTaskSelectionList}"
            optionKey="id"
            optionValue="name"
            value="${reportedEffortInstance?.task?.id}" 
            noSelection="${['null':'-- Choose --']}"/>&nbsp;&nbsp;&nbsp;&nbsp;

        <!-- Effort -->
        <span class="nowrap">Effort&nbsp;
          <g:textField 
            name="effort" 
            class="textfieldBasic" 
            style="text-align:right;"
            size="1" 
            value="${reportedEffortInstance?.percentEffort}"
          />&nbsp;%
        </span>

      </div>

    </div>

    <div class="clearCenterPadding">
      <div class="clearBoth">
        <g:actionSubmit class="buttonBasic" value="ADD" constructor="main" action="show" />          
        &nbsp;&nbsp;&nbsp;
        <button class="buttonBasic" onClick="return resetForm();">CANCEL</button>
      </div>            
    </div>
        
</g:form>      
