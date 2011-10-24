package edu.umn.ncs.dlr

import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import edu.umn.ncs.ProjectInfo
import edu.umn.ncs.ReportingPeriod
import edu.umn.ncs.ReportType
import edu.umn.ncs.AssignedEffort
import org.joda.time.format.DateTimeFormat
import grails.converters.*

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ExportController {
	
	def laborService
	def debug = grailsApplication.config.console.debugging
	//def debug = true
		
	//static def allowedFormats = [ 'csv', 'pdf', 'xml' ] as Set
	static def allowedFormats = [ 'csv', 'pdf'] as Set
	static def defaultFormat = "csv"
		
	//GENERATE REPORTS (csv, pdf, xml)
    def generateReport = {

        if (debug) { 
			println "EXPORT CONTROLLER > GENERATE REPORT _----------------------" 
			println "=> params: ${params}"
		}
		
		// REPORT TYPE
		def reportTypeInstance = ReportType.read(params?.report_type_id)
		if (debug) { 
			println "=> reportTypeInstance.id: ${reportTypeInstance.id}" 
			println "=> reportTypeInstance: ${reportTypeInstance}" 
		}
		
        // OUTPUT FORMAT
		def format = params?.format
		if ( !format ) {
			format = defaultFormat
		}
		if (debug) { println "=> format: ${format}" }
		
		// REPORTING PERIOD
		def reportingPeriodInstance = ReportingPeriod.read(params?.reporting_period_id)
		if (debug) { 
			println "=> reportingPeriodInstance.id: ${reportingPeriodInstance.id}" 
			println "=> reportingPeriodInstance: ${reportingPeriodInstance}" 
		}
		
		// if Reporting Period exist in database
        if (reportTypeInstance && format && reportingPeriodInstance) {
			
			if (debug) { println "=> if(reportTypeInstance && format && reportingPeriodInstance) = TRUE" }
			
			// file name
			def fileName = "${reportingPeriodInstance.year}-${reportingPeriodInstance.month}.${format}"
			if (debug) { println "=> fileName: ${fileName}" }
			
			// Render to Format ------------------------------------------------
            
            // CSV (use our own CVS renderer)
			if (format == "csv") {
                
				if (debug) { println "=> if (format == csv) = TRUE" }	
							
                def recordSet = laborService.getReportingPeriodDataForCsv(reportTypeInstance, reportingPeriodInstance)				
				renderAsCsv recordSet, false, fileName, response
				render ""
				return
				
			// PDF (use rendering plugin to generate a PDF)
			} else if (format == "pdf") {                
				
				if (debug) { println "=> if (format == pdf) = TRUE" }
				
				if (reportTypeInstance.abbreviation == 'SFR') {
					
					if (debug) { println "=> if (reportTypeInstance.abbreviation == 'SFR') = TRUE" }
				
					// PROJECT INFO
					def projectInfoInstance = ProjectInfo.findByPrincipalInvestigatorIsNotNull()
					if (debug) { println "=> projectInfoInstance: ${projectInfoInstance}" }
					
															
					// ASSIGNED EFFORT list
					def assignedEffortInstanceList = reportingPeriodInstance?.assignedEfforts.sort{ it.reportingStaff.fullNameLFM }
					//assignedEffortInstanceList = assignedEffortInstanceList.sort{ it.reportedEffort.activity.name }
					//assignedEffortInstanceList = assignedEffortInstanceList.sort{ it.reportedEffort.percentEffort }
					//assignedEffortInstanceList = assignedEffortInstanceList.sort{ it.reportingStaff.fullNameLFM }
					if (debug) { println "=> assignedEffortList: ${assignedEffortInstanceList}" }
					
					def model = [
							projectInfoInstance: projectInfoInstance,
							reportingPeriodInstance: reportingPeriodInstance,
							assignedEffortInstanceList: assignedEffortInstanceList
					] 
					
					def templatePath = "/pdfs/reportingPeriod"
					
					renderPdf(template: templatePath, model: model, filename: fileName)
					
				}
				
				return
                
    		// XML (use Grails Converter)
			} else if (format == "xml") {
                
				if (debug) { println "=> if (format == xml) = TRUE" }				
				XML.use("deep") {
					render reportingPeriodInstance as XML
				}                
                
            // other?    
			} else {
                
				if (debug) { println "=> if (format == other?) = TRUE" }				
				flash.message = "Unknown format: ${formatDateTime}. Please choose from ${allowedFormats}"
				redirect(action:'list')
                
			}
			
		} else {
            
			if (debug) { println "=> if(reportTypeInstance && format && reportingPeriodInstance) = FALSE" }

			flash.message = "Reporting Period ID: '${params?.id}' not found."
			redirect(action:'list')
            
		} 
        
    } 
	
	// Convert list of maps to CSV
	private void renderAsCsv(recordset, headerRow, fileName, outputStream) {
        
        if (debug) { 
			println "EXPORT CONTROLLER > renderAsCsv METHOD ----------------"
			println "=> params: ${params}"
        }
		
		/*
		if (debug) {
			println "=> recordset: ${recordset}"
			println "=> headerRow: ${headerRow}"
			println "=> fileName: ${fileName}"			
			println "=> outputStream: ${outputStream}"
		}
		*/
		
		def formatDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
		//if (debug) { println "=> formatDateTime: ${formatDateTime}" }
		
		response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"");
		response.contentType = "text/csv"
		
        // Render output data as CSV, if there is a recordset
		if (recordset) {
            
			//if (debug) { println "=> if(recordset) = TRUE (START)" }
		
			// field list (row 1)
			def firstRow = recordset[0]
			def columnNames = firstRow.collect{ it.key }

			//if (debug) { println "=> firstRow: ${firstRow}" }
			//if (debug) { println "=> columnNames: ${columnNames}" }
			
			if (headerRow) {
				
				//if (debug) { println "=> if (headerRow) = TRUE" }
				
				// write header column ("ID","FirstName","MiddleName","LastName","Suffix")
				columnNames.eachWithIndex{ col, i ->
					if (i > 0) {
						outputStream << ","
					}
					outputStream << ("\"" + col.replace("\"", "\"\"") + "\"")
				}
	            
				// Using \r\n for MS Windows
				outputStream << "\r\n"
				//if (debug) { println "=> outputStream: ${outputStream}" }
			}
			
			// write data
			recordset.each{ rowOfData ->
                
				//if (debug) { println "recordset.each{ rowOfData -> (BEGIN)" }
				
				columnNames.eachWithIndex{ col, i ->

					//if (debug) { println "columnNames.eachWithIndex{ col, i -> (BEGIN)" }
				
					def columnValue = ""
                    
					if (rowOfData[col] != null) {
                        
						//if (debug) { println "if (rowOfData[col] != null) = TRUE" }

						def columnContent = "" 

						if (rowOfData[col].class == java.util.Date) {
							
							//if (debug) { println "if (rowOfData[col].class == java.util.Date) = TRUE" }
														
							Date refDate = rowOfData[col]
							columnContent = formatDateTime.print(refDate.time)
							//if (debug) {println "refDate: ${refDate}" }
							if (debug) {println "columnContent: ${columnContent}" }
							
 						} else {                            
                            
							//if (debug) { println "if (rowOfData[col].class == java.util.Date) = FALSE" }
														
							columnContent = rowOfData[col].toString().replace('"', '""')                            
							if (debug) { println "columnContent: ${columnContent}" }
							
						}
												 
						columnValue = '"' + columnContent  + '"'
						//if (debug) { println "columnValue: ${columnValue}" }
						
						// print a comma if this is not the first field
						if (i > 0) {
							outputStream << ","
						}
						//if (debug) { println "outputStream: ${outputStream}" }
						
					} //if (rowOfData[col] != null)

					outputStream << columnValue
					
					//if (debug) { println "outputStream: ${outputStream}" }
					//if (debug) { println "columnNames.eachWithIndex{ col, i -> (END)" }
					
				} //columnNames.eachWithIndex{ col, i -> 
                
				outputStream << "\r\n"
				
				//if (debug) { println "outputStream: ${outputStream}" }
				//if (debug) { println "recordset.each{ row -> (END)" }
				
			} //recordset.each{ row -> 
            
			//if (debug) { println "if(recordset) (END)" }
			
		} //if (recordset)
        
	} 
    
	
} 
