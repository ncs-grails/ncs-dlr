package edu.umn.ncs
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE', 'ROLE_NCS_DLR'])
class MainController {

    def authenticateService
    def laborService
	def debug = grailsApplication.config.console.debugging
	
    def index = {
        
		if (debug) { 
			println "MAIN CONTROLLER > INDEX ---------------------------------------"        
			println "=> params: ${params}"
        }        
        
        redirect(action: "show")

    }    
    
    def show = {
		
        if (debug) { 
			println "MAIN CONTROLLER > SHOW ----------------------------------------"        
			println "=> params: ${params}"
        }        
        
        // REPORTING STAFF
        def principal = authenticateService.principal()                         
        if (debug) {  println "=> principal: ${principal}" }
		            
        def reportingStaffInstance = laborService.getReportingStaff(principal)
		if (debug) { println "=> reportingStaffInstance: ${reportingStaffInstance}" }

        // REPORTING PERIOD
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        if (debug) { println "=> reportingPeriodInstance: ${reportingPeriodInstance}" }            

        // ASSIGNED EFFORT instance
		def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)
		if (debug) {  println "=> assignedEffortInstance: ${assignedEffortInstance}"
			
			if ( assignedEffortInstance ) {
				println "=> if ( assignedEffortInstance ) = TRUE"
				println "=> assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"
			}
			
		}

        [
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]

    } //def show            
	
} 
