<h2>Previous Effort Reported </h2>

<table>

  <thead>
    <tr>
      <th class="basic">Reported Month</th>
      <th class="basic">Study Activity</th>
      <th class="basic">Task</th>
      <th class="basic">Percent Effort</th>
    </tr>
  </thead>

  <tbody>
    <g:each var="re" in="${reportedEffortList}" >
      <tr>
        <td class="basic">
          <div class="nowrap">
            <g:formatDate date="${re.period}" format="MMMM yyyy" />
          </div>
        </td>
        <td class="basic">
          <div class="nowrap">
            ${re.activity}
          </div>
        </td>        
        <td class="basic">
          ${re.task}
        </td>
        <td class="basic" style="text-align:right;">
          <div class="nowrap">
            <g:formatNumber number="${re.reportedEffort}" type="percent" maxFractionDigits="3"/>
          </div>
        </td>
      </tr>
    </g:each>
  </tbody>

</table>
