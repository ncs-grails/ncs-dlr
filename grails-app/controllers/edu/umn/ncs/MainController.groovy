package edu.umn.ncs
import org.joda.time.*
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE', 'ROLE_NCS_DLR'])
class MainController {

    def springSecurityService
    def laborService
	def debug = true
	
    def index = {
        
		if (debug) { 
			log.debug "MAIN CONTROLLER > INDEX ---------------------------------------"        
			log.debug "=> params: ${params}"
        }        
        
        redirect(action: "show")

    }    
    
    def show = {
		
        if (debug) { 
			log.debug "MAIN CONTROLLER > SHOW ----------------------------------------"        
			log.debug "=> params: ${params}"
        }        
        
        // REPORTING STAFF
        def principal = springSecurityService.principal                         
        if (debug) {  log.debug "=> principal: ${principal}" }
		            
        def reportingStaffInstance = laborService.getReportingStaff(principal)
		if (debug) { log.debug "=> reportingStaffInstance: ${reportingStaffInstance}" }

        // REPORTING PERIOD
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        if (debug) { log.debug "=> reportingPeriodInstance: ${reportingPeriodInstance}" }            

        // ASSIGNED EFFORT instance
		def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)
		if (debug) {  log.debug "=> assignedEffortInstance: ${assignedEffortInstance}"
			
			if ( assignedEffortInstance ) {
				log.debug "=> if ( assignedEffortInstance ) = TRUE"
				log.debug "=> assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"
			}
			
		}

        [
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]

    } //def show            
	
} 
