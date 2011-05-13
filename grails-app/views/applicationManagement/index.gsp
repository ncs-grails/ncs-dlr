<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS DLR Application Management</title>
  </head>

  <body>

    <p class="breadcrumbs">
        <g:link controller="main" action="index">DLR Home</g:link>
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        <span class="fontMaroon">Application Management</span>
      </p>
      
      <h1>DLR Application Management</h1>

      <dl>
        <dt class="basicMenu">
          Staff - enter staff information
        </dt>
        <dt class="basicMenu">
          <g:link controller="assignEffort" action="show">Effort Assignment</g:link> - enter assigned effort for reporting period
        </dt>
        <dt class="basicMenu">
          Report Generation - generate effort reports for submission
        </dt>
        <dt class="basicMenu">
          Report Archive - view past reported effort that have been generated
        </dt>
      </dl>

      <div class="pageSpacing"> </div>

  </body>

</html>
