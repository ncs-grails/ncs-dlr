<!-- display ASSIGNED, REPORTED & COMMITTED effort status -->
<g:if test="${assignedEffortInstance && !committedDateInstance}">
  
  <p class="fontMaroon">Please report the effort you accrued this month and then commit it.</p>

  <div class="clearCenterPadding">
    <span class="messageBoxOrange">Assigned: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/></span>          
    <span class="messageBoxOrange">Reported: <g:formatNumber number="${reportedEffortTotal}" type="percent" maxFractionDigits="2"/></span>
    <span class="messageBoxRed">Committed: 
      <g:if test="${!committedDateInstance}">0%</g:if>
    </span>
  </div>    
  
</g:if>

<!-- display currently REPORTED EFFORT -->      
<g:if test="${assignedEffortInstance && reportedEffortTotal && !committedDateInstance}">                    
  
  <div class="clearCenterPadding">
    
    <table>
      
      <thead>
        <tr>
          <th class="basic">Select to <br/>DELETE or EDIT</th>
          <th class="basic">Study Activity</th>
          <th class="basic">Task</th>
          <th class="basic">Percent Effort</th>
          <th class="basic">Date Reported</th>
        </tr>
      </thead>
      
      <tbody>
        <g:each var="re" in="${reportedEffortList}" >
          <tr>
            <td class="basic" style="text-align:center;">
              <input type="checkbox" name="reportedEffortId-${re.reportedEffortId}"/>
            </td>
            <td class="basic">${re.studyActivity}</td>
            <td class="basic">${re.studyTask}</td>
            <td class="basic" style="text-align:right;">
              <g:formatNumber number="${re.percentEffort}" type="percent" maxFractionDigits="2"/>
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