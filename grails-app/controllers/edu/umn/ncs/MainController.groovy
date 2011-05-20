package edu.umn.ncs
import org.joda.time.*
//import org.joda.time.format.*
//import java.text.DateFormat


// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class MainController {

    def authenticateService
    def laborService

    def index = {

        // REPORTING STAFF
        def principal = authenticateService.principal()                         // principal is whoever is logged in
        def reportingStaffInstance = laborService.getReportingStaff(principal)
        println "PRINTLN MainController.index.reportingStaffInstance: ${reportingStaffInstance}"

        // REPORTING PERIOD
        def reportingPeriodInstance
        if (params?.reportingPeriod?.id) {
            // optional pull from params.reportingPeriod.id
            reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod?.id)
        } else {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        }
        println "PRINTLN MainController.index.reportingPeriodInstance: ${reportingPeriodInstance}"

        // ASSIGNED EFFORT FOR PERIOD
        def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, reportingStaffInstance)
        println "PRINTLN MainController.index.assignedEffortInstance: ${assignedEffortInstance}"

        // EFFORT COMMITTED DATE FOR PERIOD
        def committedDateInstance = assignedEffortInstance.dateCommitted
        println "PRINTLN MainController.index.committedDateInstance: ${committedDateInstance}"
        
        [
            reportingPeriodInstance: reportingPeriodInstance,
            reportingStaffInstance: reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance,
            committedDateInstance: committedDateInstance,
        ]

    } //def index

    def show = {
                
        // Get REPORTING PERIOD instance
        def reportingPeriodInstance
        if (params?.period_id) {
            reportingPeriodInstance = ReportingPeriod.read(params?.period_id)
            println "PRINTLN MainController.show.reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        
        // Get REPORTING STAFF instance
        def reportingStaffInstance
        if (params?.staff_id) {
            reportingStaffInstance = ReportingStaff.read(params?.staff_id)
            println "PRINTLN MainController.show.reportingStaffInstance: ${reportingStaffInstance}"
        }

        // Get ASSSIGNED EFFORT instance
        def assignedEffortInstance
        if (params?.assigned_effort_id) {
            assignedEffortInstance = AssignedEffort.read(params?.assigned_effort_id)
            println "PRINTLN MainController.show.assignedEffortInstance: ${assignedEffortInstance}"
        }
        
        // Get EFFORT COMMITTED DATE
        def committedDateInstance
        if ( assignedEffortInstance ) {
            committedDateInstance = assignedEffortInstance.dateCommitted
            println "PRINTLN MainController.show.committedDateInstance: ${committedDateInstance}"            
        }

        [
            reportingPeriodInstance:reportingPeriodInstance, 
            reportingStaffInstance:reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance,
            committedDateInstance: committedDateInstance,
        ]
       
    } //def show

    def add = {
        
        def reportingPeriodInstance
        if (params?.period_id) {
            reportingPeriodInstance = ReportingPeriod.read(params?.period_id)
            println "PRINTLN MainController.add.reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        
        // Get REPORTING STAFF instance
        def reportingStaffInstance
        if (params?.staff_id) {
            reportingStaffInstance = ReportingStaff.read(params?.staff_id)
            println "PRINTLN MainController.add.reportingStaffInstance: ${reportingStaffInstance}"
        }

        // Get ASSSIGNED EFFORT instance
        def assignedEffortInstance
        if (params?.assigned_effort_id) {
            assignedEffortInstance = AssignedEffort.read(params?.assigned_effort_id)
            println "PRINTLN MainController.add.assignedEffortInstance: ${assignedEffortInstance}"
        }
        
        // Get EFFORT COMMITTED DATE
        def committedDateInstance
        if ( assignedEffortInstance ) {
            committedDateInstance = assignedEffortInstance.dateCommitted
            println "PRINTLN MainController.add.committedDateInstance: ${committedDateInstance}"            
        }

        // Get STUDY ACTIVITY
        def csa = StudyActivity.createCriteria()
        
        def studyActivityInstanceList = csa.list{
            eq("obsolete", false)
            order("name", "asc")
        }
        println "PRINTLN MainController.add.studyActivityInstanceList: ${studyActivityInstanceList}"                    
        studyActivityInstanceList.each{
            println "PRINTLN MainController.add.studyActivityInstanceList.name: ${it.name}"                    
        }
        
        def studyActivitySelectionList = []
        
        studyActivityInstanceList.each{
            studyActivitySelectionList.add([id:it.id, name:it.name])
        }
        println "PRINTLN MainController.add.studyActivitySelectionList: ${studyActivitySelectionList}"                    
        
        // Get STUDY TASK
        def cst = StudyTask.createCriteria()
        
        def studyTaskInstanceList = cst.list{
            eq("obsolete", false)
            order("name", "asc")
        }
        println "PRINTLN MainController.add.studyTaskInstanceList: ${studyTaskInstanceList}"                    
        studyTaskInstanceList.each{
            println "PRINTLN MainController.add.studyTaskInstanceList.name: ${it.name}"                    
        }
        
        def studyTaskSelectionList = []
        
        studyTaskInstanceList.each{
            studyTaskSelectionList.add([id:it.id, name:it.name])
        }
        println "PRINTLN MainController.add.studyTaskSelectionList: ${studyTaskSelectionList}"                    

        // reported effort
        //def reportedEffortInstanceList

        // past committed effort
        //def previouslyReportedEffortInstanceList

        [
            reportingPeriodInstance: reportingPeriodInstance, 
            reportingStaffInstance: reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance,
            committedDateInstance: committedDateInstance, 
            studyActivitySelectionList: studyActivitySelectionList, 
            studyTaskSelectionList: studyTaskSelectionList
        ]

    } //def add
    

} //class MainController
