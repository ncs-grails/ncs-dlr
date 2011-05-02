package edu.umn.ncs
import org.joda.time.*
import org.joda.time.format.*
//import java.text.DateFormat


// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class MainController {

    def authenticateService
    def laborService

    def index = {

        // STAFF
        def principal = authenticateService.principal()                         // principal is whoever is logged in
        def reportingStaffInstance = laborService.getReportingStaff(principal)
        // println "PRINTLN MainController.reportingStaffInstance: ${reportingStaffInstance}"

        // PERIOD
        def reportingPeriodInstance
        if (params?.reportingPeriod?.id) {
            // optional pull from params.reportingPeriod.id
            reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod?.id)
        } else {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        }

        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)

        // EFFORT COMMITTED DATE
        def committedDateInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)
        // println "PRINTLN MainController.committedDateInstance: ${committedDateInstance}"
        
        [
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance,
            committedDateInstance: committedDateInstance,
        ]

    } //def index

    def show = {

        // activity
        //def studyActivityInstanceList = StudyActivity.findAllByObsolete(false)

        // task
        //def studyTaskInstanceList = StudyTask.findAllByObsolete(false)

        // reported effort
        //def reportedEffortInstanceList

        // past committed effort
        //def previouslyReportedEffortInstanceList

    } //def show



} //class MainController
