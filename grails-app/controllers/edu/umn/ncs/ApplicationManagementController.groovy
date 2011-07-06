package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ApplicationManagementController {

    def authenticateService
    def laborService

    // display APPLICATION MANAGEMENT page
    def index = {     
        println "PRINTLN APPLICATION MANAGEMENT CONTROLLER > INDEX -------------"
        println "PRINTLN ApplicationManagementController.index.params: ${params}"                
    }
    
	def report = {
        
        println "PRINTLN APPLICATION MANAGEMENT CONTROLLER > REPORT -------------"
        println "PRINTLN ApplicationManagementController.reports.params: ${params}"

		def reportingPeriodInstanceList = ReportingPeriod.list()
        println "PRINTLN ApplicationManagementController.reports.reportingPeriodInstanceList: ${reportingPeriodInstanceList}"

		def reportFormats = ExportController.allowedFormats
        println "PRINTLN ApplicationManagementController.reports.reportFormats: ${reportFormats}"
		
		[ 
            reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportFormats: reportFormats 
        ]
        
	} //def reports

} //class ApplicationManagementController

