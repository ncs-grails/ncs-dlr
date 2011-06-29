package edu.umn.ncs.dlr

import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import edu.umn.ncs.ReportingPeriod
import org.joda.time.format.DateTimeFormat
import grails.converters.*

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ExportController {
	
	static def debug = false

	static def allowedFormats = [ 'pdf', 'xml', 'csv' ] as Set
	static def defaultFormat = "csv"
	
	def laborService
	
    def reportingPeriod = {
		
		def format = params?.format
		if ( ! format ) {
			format = defaultFormat
		}
		// check / force format

		if (debug) { println "rendering as : ${format}" }
				
		if (debug) { println "entering : reportingPeriod" }
		// use the ID to lookup the period to report on
		def reportingPeriodInstance = ReportingPeriod.read(params?.id)
		// if we could find it in the database...
		if (reportingPeriodInstance) {
			
			// build a file name
			def fileName = "reporting-period_${reportingPeriodInstance.year}-${reportingPeriodInstance.month}.${format}"

			// Set the filename

			// check the format and render appropriately
			if (format == "pdf") {
				// use the rendering plugin to generate a PDF
				renderPdf(template: "/pdfs/reportingPeriod", model: [reportingPeriodInstance: reportingPeriodInstance], filename: fileName)
				return
			} else if (format == "xml") {
				// render it as XML using the Grails Converter
				XML.use("deep") {
					render reportingPeriodInstance as XML
				}
			} else if (format == "csv") {
				// Default to CSV
                // get the record set for the period from the service LaborService
                def recordSet = laborService.getReportingPeriodData(reportingPeriodInstance)
				// render it as CSV using our own CSV renderer
				renderAsCsv recordSet, fileName, response
				render ""
				return
			} else {
				// WTF?
				flash.message = "Unknown format: ${fmt}. Please choose from ${allowedFormats}"
				redirect(controller:'main')
			}
			
		} else {
			flash.message = "Reporting Period ID: '${params?.id}' not found."
			redirect(controller:'main')
		}
	}
	
	
	// helper method to convert a list of maps to CSV
	private void renderAsCsv(recordset, fileName, outputStream) {
		if (debug) { println "entering : renderAsCsv" }

		def fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
		
		response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"");
		response.contentType = "text/csv"

		if (recordset) {
			// assume this doesn't work
			// Render outputData as CSV

			// get a field list
			def firstRow = recordset[0]
			def columnNames = firstRow.collect{ it.key }

			/*columnNames.each{
				println "Dataset columnNames >> ${it}"
			}*/

			// write the header column
			//  "ID","FirstName","MiddleName","LastName","Suffix"
			columnNames.eachWithIndex{ col, i ->
				if (i > 0) {
					outputStream << ","
				}
				outputStream << ("\"" + col.replace("\"", "\"\"") + "\"")
			}
			// Using \r\n for MS Windows
			outputStream << "\r\n"

			// write the data
			recordset.each{ row ->
				columnNames.eachWithIndex{ col, i ->

					// default content is empty
					def columnValue = ""
					// If there's a non-null value...
					if (row[col] != null) {
						// take the content and escape the double quotes (")
						
						def columnContent = "" 

						if (row[col].class == java.util.Date) {
							// if it's a date, then format it specifically
							Date refDate = row[col]
							columnContent = fmt.print(refDate.time)
						} else {
							// Otherwise use the default toString() method
							columnContent = row[col].toString().replace('"', '""')
						}
						
						// then surround it with double quotes
						columnValue = '"' + columnContent  + '"'
						
						// print a comma if is not the first field
						if (i > 0) {
							outputStream << ","
						}
					}

					outputStream << columnValue
				}
				outputStream << "\r\n"
			}
		}
	}
}
