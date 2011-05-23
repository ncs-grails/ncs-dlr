      <!-- display effort status -->
      <g:if test="${assignedEffortInstance && !committedDateInstance}">
        <div class="clearCenterPadding">
          <span class="messageBoxOrange">Assigned: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/></span>          
          &nbsp;&nbsp;&nbsp;
          <span class="messageBoxOrange">Reported: <g:formatNumber number="${reportedEffortTotal}" type="percent" maxFractionDigits="2"/></span>
          &nbsp;&nbsp;&nbsp;
          <span class="messageBoxRed">Committed: 
            <g:if test="${!committedDateInstance}">0%</g:if>
          </span>
        </div>        
      </g:if>        
      
      <!-- if there are ASSIGNED effort that has not been committed yet, user must complete dlr -->
      <g:if test="${assignedEffortInstance && !reportedEffortTotal && !committedDateInstance}">
        <p class="clearCenterPadding">                         
          <div class="messageBoxPowderblue">Please submit the effort you accrued this month.</div>
        </p>          
      </g:if>

      <!-- display reported effort table -->      
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