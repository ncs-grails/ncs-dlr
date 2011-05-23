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
        redirect(action:'show')        
    }

    def show = {
                
        // REPORTING STAFF
        def principal = authenticateService.principal()                         
        def reportingStaffInstance = laborService.getReportingStaff(principal)
        println "PRINTLN MainController.show.reportingStaffInstance: ${reportingStaffInstance}"

        // REPORTING PERIOD
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        println "PRINTLN MainController.show.reportingPeriodInstance: ${reportingPeriodInstance}"

        // ASSIGNED EFFORT FOR PERIOD
        def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, reportingStaffInstance)
        println "PRINTLN MainController.show.assignedEffortInstance: ${assignedEffortInstance}"

        // EFFORT COMMITTED DATE FOR PERIOD
        def committedDateInstance = assignedEffortInstance.dateCommitted
        println "PRINTLN MainController.show.committedDateInstance: ${committedDateInstance}"
        
        [
            reportingPeriodInstance: reportingPeriodInstance,
            reportingStaffInstance: reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance,
            committedDateInstance: committedDateInstance,
        ]
       
    } //def show
    
    
    
} //class MainController
