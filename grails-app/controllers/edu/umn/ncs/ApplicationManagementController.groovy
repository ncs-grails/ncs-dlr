package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class ApplicationManagementController {

    def authenticateService
    def laborService

    def index = {
		
		if (debug) {
	        println "APPLICATION MANAGEMENT CONTROLLER > INDEX ---------------------"
	        println "=> params: ${params}"
		}
		
		redirect(action: "list", params: params)
		
    }

    def list = {    
		 
		if (debug) {
	        println "APPLICATION MANAGEMENT CONTROLLER > LIST ----------------------"
	        println "=> params: ${params}"
		}
		                
    }
    
	def report = {
        
		if (debug) {
		    println "APPLICATION MANAGEMENT CONTROLLER > REPORT ---------------------"
	        println "=> params: ${params}"
		}

		// REPORTING PERIOD
				
		def c = ReportingPeriod.createCriteria()
		def periodList = c.list {			
			order("periodDate", "desc")
		} 
        if (debug) { println "=> periodList: ${periodList}" }
		
		def reportingPeriodInstanceList = []
        periodList.each{
            reportingPeriodInstanceList.add(g.formatDate(date:it.periodDate, format:'MMMM yyyy'))
        }
		if (debug) { println "=> reportingPeriodInstanceList: ${reportingPeriodInstanceList}" }

		// REPORT FORMAT		
		def reportFormats = ExportController.allowedFormats
        if (debug) { println "=> reportFormats: ${reportFormats}" }
		
		[ 
            reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportFormats: reportFormats 
        ]
		
	} 

} //class ApplicationManagementController

