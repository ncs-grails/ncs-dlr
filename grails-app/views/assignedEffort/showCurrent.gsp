<!-- ASSIGNED, REPORTED & COMMITTED message boxes -->
<g:if test="${assignedEffortInstance && !committedDateInstance}">
  
  <p class="fontMaroon">Please report the effort you accrued this month and then commit it.</p>

  <div class="clearCenterPadding">
    <!-- Assigned Effort -->
    <span class="messageBoxOrange">
      Assigned: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/>
    </span>          
    <!-- Reported Effort -->
    <span class="messageBoxOrange">
      Reported: 
      <g:if test="${!reportedEffortTotal}">
        0%
      </g:if>      
      <g:else>
        <g:formatNumber number="${reportedEffortTotal}" type="percent" maxFractionDigits="2"/>        
      </g:else>
    </span>
    <!-- Committed Effort -->
    <span class="messageBoxRed">Committed: 
      <g:if test="${!committedDateInstance}">0%</g:if>
    </span>
  </div>    
  
</g:if>

<!-- REPORTED EFFORT for current period -->      
<g:if test="${assignedEffortInstance && reportedEffortTotal && !committedDateInstance}">                    
  
  <div class="clearCenterPadding">
    <table>
      <thead>
        <tr>
          <g:if test="${isForm}">
            <th class="basic">Select to <br/>DELETE or EDIT</th>
          </g:if>
          <th class="basic">Study Activity</th>
          <th class="basic">Task</th>
          <th class="basic">Percent Effort</th>
          <th class="basic">Date Reported</th>
        </tr>
      </thead>
      <tbody>
        <g:each var="re" in="${reportedEffortList}" >
          <tr>
            <g:if test="${isForm}">
              <td class="basic" style="text-align:center;">
                <input type="radio" name="reportedEffort.id" value="${re.reportedEffortId}"/>
              </td>
            </g:if>
            <td class="basic">${re.studyActivity}</td>
            <td class="basic">${re.studyTask}</td>
            <td class="basic" style="text-align:right;">
              <g:formatNumber number="${re.percentEffort}" type="percent" maxFractionDigits="3"/>
            </td>
            <td class="basic" style="text-align:center;">
              <g:formatDate date="${re.dateCreated}" format="MM-dd-yyyy"/>
            </td>                  
          </tr>
        </g:each>
      </tbody>
    </table>
  </div>

  <g:if test="${flash.message}">
    <div class="flashMessage">${flash.message}</div>
  </g:if>
  
</g:if>