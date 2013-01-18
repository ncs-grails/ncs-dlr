package edu.umn.ncs.dlr

import grails.plugins.springsecurity.Secured
import edu.umn.ncs.ProjectInfo
import edu.umn.ncs.ReportingPeriod
import edu.umn.ncs.ReportType
import edu.umn.ncs.AssignedEffort
import org.joda.time.format.DateTimeFormat
import grails.converters.*

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ExportController {
	
	def laborService

	static def allowedFormats = [ 'csv', 'pdf'] as Set
	static def defaultFormat = "csv"
	//static def allowedFormats = [ 'csv', 'pdf', 'xml' ] as Set
		
	//GENERATE REPORTS (csv, pdf, xml)
	def generateReport = {

		log.debug ""
		log.debug "EXPORT CONTROLLER > GENERATE-REPORT -----------------------" 
		log.debug "=> params: ${params}"
		
		// REPORT TYPE
		def reportTypeInstance = ReportType.read(params?.report_type_id)
		log.debug "=> reportTypeInstance: ${reportTypeInstance}" 
		
		// OUTPUT FORMAT
		def format = params?.format
		if ( !format ) { format = defaultFormat }
		log.debug "=> format: ${format}" 
		
		// REPORTING PERIOD
		def reportingPeriodInstance = ReportingPeriod.read(params?.reporting_period_id)
		log.debug "=> reportingPeriodInstance: ${reportingPeriodInstance}" 
		
		// if Reporting Period exist in database
		if (reportTypeInstance && format && reportingPeriodInstance) {
			
			log.debug "=> if (reportTypeInstance && format && reportingPeriodInstance) = TRUE" 
			
			// file name
			def fileName = "${reportingPeriodInstance.year}-${reportingPeriodInstance.month}.${format}"
			log.debug "=> fileName: ${fileName}" 
			
			// Render to Format ------------------------------------------------
            
			// CSV (use our own CVS renderer)
			if (format == "csv") {
                
				log.debug "=> if (format == csv) = TRUE" 
							
				def recordSet = laborService.getReportingPeriodDataForCsv(reportTypeInstance, reportingPeriodInstance)				
				renderAsCsv recordSet, false, fileName, response
				render ""
				return
				
			// PDF (use rendering plugin to generate a PDF)
			} else if (format == "pdf") {                
				
				log.debug "=> if (format == pdf) = TRUE" 
				
				if (reportTypeInstance.abbreviation == 'SFR') {
					
					log.debug "=> if (reportTypeInstance.abbreviation == 'SFR') = TRUE" 
				
					// PROJECT INFO
					def projectInfoInstance = ProjectInfo.findByPrincipalInvestigatorIsNotNull()
					log.debug "=> projectInfoInstance: ${projectInfoInstance}" 
					
															
					// ASSIGNED EFFORT list
					def assignedEffortInstanceList = reportingPeriodInstance?.assignedEfforts.sort{ it.reportingStaff.fullNameLFM }
					log.debug "=> assignedEffortList: ${assignedEffortInstanceList}" 
					
					def model = [
						projectInfoInstance: projectInfoInstance,
						reportingPeriodInstance: reportingPeriodInstance,
						assignedEffortInstanceList: assignedEffortInstanceList
					] 
					
					def templatePath = "/pdfs/reportingPeriod"
					log.debug "=> templatePath: ${templatePath}"
					
					renderPdf(template: templatePath, model: model, filename: fileName)
					
				}
				
				return
                
			// XML (use Grails Converter)
			} else if (format == "xml") {
                
				log.debug "=> if (format == xml) = TRUE" 
				XML.use("deep") {
					render reportingPeriodInstance as XML
				}                
                
			// other?    
			} else {
                
				log.debug "=> if (format == other?) = TRUE" 
				flash.message = "Unknown format: ${formatDateTime}. Please choose from ${allowedFormats}"
				redirect(action:'list')
                
			}
			
		} else {
            
			log.debug "=> if(reportTypeInstance && format && reportingPeriodInstance) = FALSE" 

			flash.message = "Reporting Period ID: '${params?.id}' not found."
			redirect(action:'list')
            
		} 
        
    } 
	
	// Convert list of maps to CSV
	private void renderAsCsv(recordset, headerRow, fileName, outputStream) {
        
        
		log.debug "EXPORT CONTROLLER > renderAsCsv METHOD ----------------"
		log.debug "=> params: ${params}"
       
		/*
		log.debug "=> recordset: ${recordset}"
		log.debug "=> headerRow: ${headerRow}"
		log.debug "=> fileName: ${fileName}"			
		log.debug "=> outputStream: ${outputStream}"
		*/
		
		def formatDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
		//log.debug "=> formatDateTime: ${formatDateTime}" 
		
		response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"");
		response.contentType = "text/csv"
		
        // Render output data as CSV, if there is a recordset
		if (recordset) {
            
			//log.debug "=> if(recordset) = TRUE (START)" 
		
			// field list (row 1)
			def firstRow = recordset[0]
			def columnNames = firstRow.collect{ it.key }

			//log.debug "=> firstRow: ${firstRow}" 
			//log.debug "=> columnNames: ${columnNames}" 
			
			if (headerRow) {
				
				//log.debug "=> if (headerRow) = TRUE" 
				
				// write header column ("ID","FirstName","MiddleName","LastName","Suffix")
				columnNames.eachWithIndex{ col, i ->
					if (i > 0) {
						outputStream << ","
					}
					outputStream << ("\"" + col.replace("\"", "\"\"") + "\"")
				}
	            
				// Using \r\n for MS Windows
				outputStream << "\r\n"
				//log.debug "=> outputStream: ${outputStream}" 
			}
			
			// write data
			recordset.each{ rowOfData ->
                
				//log.debug "recordset.each{ rowOfData -> (BEGIN)" 
				
				columnNames.eachWithIndex{ col, i ->

					//log.debug "columnNames.eachWithIndex{ col, i -> (BEGIN)" 
				
					def columnValue = ""
                    
					if (rowOfData[col] != null) {
                        
						//log.debug "if (rowOfData[col] != null) = TRUE" 

						def columnContent = "" 

						if (rowOfData[col].class == java.util.Date) {
							
							//log.debug "if (rowOfData[col].class == java.util.Date) = TRUE" 
														
							Date refDate = rowOfData[col]
							columnContent = formatDateTime.print(refDate.time)
							//log.debug "refDate: ${refDate}" 
							log.debug "columnContent: ${columnContent}" 
							
 						} else {                            
                            
							//log.debug "if (rowOfData[col].class == java.util.Date) = FALSE" 
														
							columnContent = rowOfData[col].toString().replace('"', '""')                            
							log.debug "columnContent: ${columnContent}" 
							
						}
												 
						columnValue = '"' + columnContent  + '"'
						//log.debug "columnValue: ${columnValue}" 
						
						// print a comma if this is not the first field
						if (i > 0) {
							outputStream << ","
						}
						//log.debug "outputStream: ${outputStream}" 
						
					} //if (rowOfData[col] != null)

					outputStream << columnValue
					
					//log.debug "outputStream: ${outputStream}" 
					//log.debug "columnNames.eachWithIndex{ col, i -> (END)" 
					
				} //columnNames.eachWithIndex{ col, i -> 
                
				outputStream << "\r\n"
				
				//log.debug "outputStream: ${outputStream}" 
				//log.debug "recordset.each{ row -> (END)" 
				
			} //recordset.each{ row -> 
            
			//log.debug "if(recordset) (END)" 
			
		} //if (recordset)
        
	} 
    
	
} 
