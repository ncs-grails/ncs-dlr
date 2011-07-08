package edu.umn.ncs.dlr

import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import edu.umn.ncs.ReportingPeriod
import org.joda.time.format.DateTimeFormat
import grails.converters.*

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ExportController {
	
	static def allowedFormats = [ 'csv', 'pdf', 'xml' ] as Set
	static def defaultFormat = "csv"
	static def debug = false

	def laborService
	
    def reportingPeriod = {

        println "PRINTLN EXPORT CONTROLLER > REPORTING PERIOD ------------------"
        println "PRINTLN ExportController.reportingPeriod.params: ${params}"
		
        // OUTPUT FORMAT
		def format = params?.format
        println "PRINTLN ExportController.reportingPeriod.format: ${format}"
        
		if ( ! format ) {
			format = defaultFormat
		}
		// check / force format

		if (debug) { println "rendering as : ${format}" }				
		if (debug) { println "entering : reportingPeriod" }
        
		// REPORTING PERIOD
		def reportingPeriodInstance = ReportingPeriod.read(params?.id)
        
		// if Reporting Period exist in database
        if (reportingPeriodInstance) {
			
			// create file name
			def fileName = "reporting-period_${reportingPeriodInstance.year}-${reportingPeriodInstance.month}.${format}"

			// Set the filename ??? AJZ

			// RENDER TO FORMAT ------------------------------------------------
            
            // render PDF, using rendering plugin to generate a PDF
			if (format == "pdf") {                
				
				renderPdf(template: "/pdfs/reportingPeriod", model: [reportingPeriodInstance: reportingPeriodInstance], filename: fileName)
				return
                
    		// renderas XML, using the Grails Converter
			} else if (format == "xml") {
                
				XML.use("deep") {
					render reportingPeriodInstance as XML
				}                
                
            // render CSV (default), using our own CVS renderer
			} else if (format == "csv") {
                
                // create record set for reporting period from LaborService
                def recordSet = laborService.getReportingPeriodData(reportingPeriodInstance)
                
				renderAsCsv recordSet, fileName, response
				render ""
				return
                
            // other?    
			} else {
                
				flash.message = "Unknown format: ${fmt}. Please choose from ${allowedFormats}"
				redirect(action:'list')
                
			}
			
		} else {
            
			flash.message = "Reporting Period ID: '${params?.id}' not found."
			redirect(action:'list')
            
		} //if (reportingPeriodInstance)
        
    } //def reportingPeriod
	
	
	// method to convert a list of maps to CSV
	private void renderAsCsv(recordset, fileName, outputStream) {
        
        println "PRINTLN EXPORT CONTROLLER > renderAsCsv METHOD ----------------"
        println "PRINTLN ExportController.renderAsCsv.params: ${params}"
		
		if (debug) { println "entering : renderAsCsv" }

		def fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
        println "PRINTLN ExportController.renderAsCsv.fmt: ${fmt}"
		
		response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"");
		response.contentType = "text/csv"

        // Render outputData as CSV, if there is a recordset
		if (recordset) {
            
			// get a field list
			def firstRow = recordset[0]
			def columnNames = firstRow.collect{ it.key }

			/*columnNames.each{
				println "Dataset columnNames >> ${it}"
			}*/

			// write header column
			//  "ID","FirstName","MiddleName","LastName","Suffix"
			columnNames.eachWithIndex{ col, i ->
				if (i > 0) {
					outputStream << ","
				}
				outputStream << ("\"" + col.replace("\"", "\"\"") + "\"")
			}
            
			// Using \r\n for MS Windows
			outputStream << "\r\n"

			// write data
			recordset.each{ row ->
                
				columnNames.eachWithIndex{ col, i ->

					// default content is empty
					def columnValue = ""
                    
					// if there's a non-null value
					if (row[col] != null) {
                        
						// take the content and escape the double quotes (")						
						def columnContent = "" 

                        // if it's a date, then format it specifically
						if (row[col].class == java.util.Date) {
                            
							Date refDate = row[col]
							columnContent = fmt.print(refDate.time)
                            
                        // Otherwise use the default toString() method
						} else {                            
                            
							columnContent = row[col].toString().replace('"', '""')                            
                            
						}
						
						// then surround it with double quotes
						columnValue = '"' + columnContent  + '"'
						
						// print a comma if is not the first field
						if (i > 0) {
							outputStream << ","
						}
                        
					} //if (row[col] != null)

					outputStream << columnValue
                    
				} //columnNames.eachWithIndex
                
				outputStream << "\r\n"
                
			} //recordset.each
            
		} //if (recordset)
        
	} //private void renderAsCsv(recordset, fileName, outputStream)
    
} //class ExportController
