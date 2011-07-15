package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

//@Secured(['ROLE_NCS_DLR_MANAGE'])
@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class ApplicationManagementController {

    def authenticateService
    def laborService

    def index = {
		redirect(action: "list", params: params)
    }

	// display APPLICATION MANAGEMENT page
    def list = {     
		
        println "PRINTLN APPLICATION MANAGEMENT CONTROLLER > INDEX -------------"
        println "PRINTLN ApplicationManagementController.list.params: ${params}"
		                
    }
    
	def report = {
        
        println "PRINTLN APPLICATION MANAGEMENT CONTROLLER > REPORT -------------"
        println "PRINTLN ApplicationManagementController.reports.params: ${params}"

		// REPORTING PERIOD
				
		def c = ReportingPeriod.createCriteria()
		def periodList = c.list {
			
		} 
		
        println "PRINTLN ApplicationManagementController.reports.reportingPeriodInstanceList: ${periodList}"
		
		def reportingPeriodInstanceList = []
        periodList.each{
            reportingPeriodInstanceList.add(g.formatDate(date:it.periodDate, format:'MMMM yyyy'))
        }
		println "PRINTLN ApplicationManagementController.reports.reportingPeriodInstanceList: ${reportingPeriodInstanceList}"

		// REPORT FORMAT		
		def reportFormats = ExportController.allowedFormats
        println "PRINTLN ApplicationManagementController.reports.reportFormats: ${reportFormats}"
		
		[ 
            reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportFormats: reportFormats 
        ]
	} //def reports

} //class ApplicationManagementController

