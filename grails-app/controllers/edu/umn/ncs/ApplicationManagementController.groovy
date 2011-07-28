package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class ApplicationManagementController {

    def authenticateService
    def laborService

    def index = {
		
        println "PRINTLN APPLICATION MANAGEMENT CONTROLLER > INDEX -------------"
        println "PRINTLN params: ${params}"
		
		redirect(action: "list", params: params)
		
    }

    def list = {     
		
        println "PRINTLN APPLICATION MANAGEMENT CONTROLLER > LIST --------------"
        println "PRINTLN params: ${params}"
		                
    }
    
	def report = {
        
        println "PRINTLN APPLICATION MANAGEMENT CONTROLLER > REPORT -------------"
        println "PRINTLN params: ${params}"

		// REPORTING PERIOD
				
		def c = ReportingPeriod.createCriteria()
		def periodList = c.list {			
			order("periodDate", "desc")
		} 
        println "PRINTLN periodList: ${periodList}"
		
		def reportingPeriodInstanceList = []
        periodList.each{
            reportingPeriodInstanceList.add(g.formatDate(date:it.periodDate, format:'MMMM yyyy'))
        }
		println "PRINTLN reportingPeriodInstanceList: ${reportingPeriodInstanceList}"

		// REPORT FORMAT		
		def reportFormats = ExportController.allowedFormats
        println "PRINTLN reportFormats: ${reportFormats}"
		
		[ 
            reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportFormats: reportFormats 
        ]
		
	} //def reports

} //class ApplicationManagementController

