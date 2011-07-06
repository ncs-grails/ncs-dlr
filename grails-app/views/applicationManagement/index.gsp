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

    <!-- NAVIGATION -->
    <p class="breadcrumbs">
      <g:link controller="main" action="index">DLR Home</g:link>
      &nbsp;&nbsp;&gt&nbsp;&nbsp;
      <span class="fontMaroon">Application Management</span>
    </p>
      
    <h1>DLR Application Management</h1>

    <!-- MENU selection -->
    <dl>
      <dt class="basicMenu">
        <g:link controller="assignEffort" action="show">Effort Assignment</g:link> - enter assigned effort for reporting period
      </dt>
      <dt class="basicMenu">
        <g:link action="report">Report Generation</g:link> - generate effort reports for distribution
      </dt>
      <dt class="basicMenu">
        Staff - enter staff information
      </dt>
    </dl>

    <div class="pageSpacing"> </div>

  </body>

</html>
