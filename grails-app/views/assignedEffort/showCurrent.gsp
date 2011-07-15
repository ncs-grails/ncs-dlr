<!-- ASSIGNED, REPORTED & COMMITTED message boxes -->
<g:if test="${assignedEffortInstance && !assignedEffortInstance.dateCommitted}">
  
  <p class="fontMaroon">Please report the effort you accrued this month and then commit it.</p>

  <div class="clearCenterPadding">
    <!-- Assigned Effort -->
    <span class="messageBoxOrange">
      Assigned: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/>
    </span>          
    <!-- Reported Effort -->
    <span class="messageBoxOrange">
      Reported: 
      <g:if test="${reportedEffortTotalConverted}">
      	<g:formatNumber number="${reportedEffortTotalConverted}" type="number" maxFractionDigits="3"/>%
      </g:if>      
      <g:else>
      	0%
      </g:else>
    </span>
    <!-- Not Reported Effort -->
    <span class="messageBoxRed">Remaining: 
      <g:if test="${notReportedEffortConverted}">
      	<g:formatNumber number="${notReportedEffortConverted}" type="number" maxFractionDigits="3"/>%
      </g:if>
      <g:else>
      	0%
      </g:else>      
    </span>
  </div>    
  
</g:if>

<!-- REPORTED EFFORT for current period -->      
<g:if test="${assignedEffortInstance && reportedEffortTotalConverted && !assignedEffortInstance.dateCommitted}">
  
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
              <g:formatNumber number="${re.percentEffortConverted}" type="number" maxFractionDigits="3"/>%
            </td>
            <td class="basic" style="text-align:center;">
              <g:formatDate date="${re.dateCreated}" format="MM-dd-yyyy"/>
            </td>                  
          </tr>
        </g:each>
      </tbody>
    </table>
  </div>
  
</g:if>