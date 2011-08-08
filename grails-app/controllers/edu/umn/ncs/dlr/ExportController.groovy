package edu.umn.ncs.dlr

import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import edu.umn.ncs.ReportingPeriod
import org.joda.time.format.DateTimeFormat
import grails.converters.*

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ExportController {
	
	def laborService

	//static def allowedFormats = [ 'csv', 'pdf', 'xml' ] as Set
	static def allowedFormats = [ 'csv', 'pdf'] as Set
	static def defaultFormat = "csv"	
	static def debug = true
	
    def reportingPeriod = {

        if (debug) { 
			println "EXPORT CONTROLLER > REPORTING PERIOD ----------------------" 
			println "=> params: ${params}"
		}
		
        // OUTPUT FORMAT
		def format = params?.format
        if (debug) { println "=> format: ${format}" }
        
		if ( !format ) {
			if (debug) { println "=> if(!format) = TRUE" }
			format = defaultFormat
			if (debug) { println "=> format: ${format}" }
		}
		        
		// REPORTING PERIOD
		def reportingPeriodInstance = ReportingPeriod.read(params?.id)
		if (debug) { println "=> reportingPeriodInstance: ${reportingPeriodInstance}" }
				
		// if Reporting Period exist in database
        if (reportingPeriodInstance) {
			
			if (debug) { println "=> if(reportingPeriodInstance) = TRUE" }
			
			// file name
			//def fileName = "reporting-period_${reportingPeriodInstance.year}-${reportingPeriodInstance.month}.${format}"
			def fileName = "${reportingPeriodInstance.year}-${reportingPeriodInstance.month}.${format}"
			if (debug) { println "=> fileName: ${fileName}" }
			
			// Render to Format ------------------------------------------------
            
            // render PDF (use rendering plugin to generate a PDF)
			if (format == "pdf") {                
				
				if (debug) { println "=> if (format == pdf) = TRUE" }				
				renderPdf(template: "/pdfs/reportingPeriod", model: [reportingPeriodInstance: reportingPeriodInstance], filename: fileName)
				return
                
    		// render XML (use Grails Converter)
			} else if (format == "xml") {
                
				if (debug) { println "=> if (format == xml) = TRUE" }				
				XML.use("deep") {
					render reportingPeriodInstance as XML
				}                
                
            // render CSV (use our own CVS renderer)
			} else if (format == "csv") {
                
				if (debug) { println "=> if (format == csv) = TRUE" }				

                def recordSet = laborService.getReportingPeriodData(reportingPeriodInstance)
                
				renderAsCsv recordSet, fileName, response
				render ""
				return
                
            // other?    
			} else {
                
				if (debug) { println "=> if (format == other?) = TRUE" }				
				flash.message = "Unknown format: ${fmt}. Please choose from ${allowedFormats}"
				redirect(action:'list')
                
			}
			
		} else {
            
			if (debug) { println "=> if(reportingPeriodInstance) = FALSE" }

			flash.message = "Reporting Period ID: '${params?.id}' not found."
			redirect(action:'list')
            
		} 
        
    } 
	
	
	// Convert list of maps to CSV
	private void renderAsCsv(recordset, fileName, outputStream) {
        
        if (debug) { 
			println "EXPORT CONTROLLER > renderAsCsv METHOD ----------------"
			println "params: ${params}"
        }
		
		def fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
		if (debug) { println "fmt: ${fmt}" }
		
		response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"");
		response.contentType = "text/csv"

        // Render output data as CSV, if there is a recordset
		if (recordset) {
            
			if (debug) { println "if(recordset)" }
		
			// field list
			def firstRow = recordset[0]
			def columnNames = firstRow.collect{ it.key }

			//columnNames.each{ println "Dataset columnNames >> ${it}" }

			// write header column ("ID","FirstName","MiddleName","LastName","Suffix")
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
    
	
} 
