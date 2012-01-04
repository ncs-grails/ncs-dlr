package edu.umn.ncs
import java.util.Date;

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/** This class represents types of reports that can be generated from the DLR application. */
class ReportType {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true
			
	/** Name of report type generated in the DLR applicaiton  */
	String name
	/** Abbreviation of report type generated in the DLR applicaiton  */
	String abbreviation
	/** Indicates whether this specific report type is still made available, in the DLR application. */
	Boolean obsolete = true
	/** Indicates if the report type can be generated in csv format, in the DLR application  */
	Boolean csv = false
	/** Indicates if the report type can be generated in pdf format, in the DLR application  */
	Boolean pdf = false
	/** Indicates if the report type can be generated in xml format, in the DLR application  */
	Boolean xml = false

	/** Sets default string for this domain to report type "name" (description) */	
	String toString() { name }

	/** Non-default constraints for this class 
	<dl>
		<dt>name</dt>
			<dd>cannot be blank, maximum length of 256 characters</dd>
		<dt>abbreviation</dt>
			<dd>cannot be blank, maximum length of 16 characters</dd>
	</dl>	
	*/	
	static constraints = {
		name(blank:false, maxSize:256)
		abbreviation(blank:false, maxSize:16)
		obsolete()
		csv()
		pdf()
		xml()
	}

	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->
		
		def now = new Date()
		
		String oldValue = "ReportType"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", abbreviation: ${oldMap.abbreviation}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
			oldValue += ", csv: ${oldMap.csv}"
			oldValue += ", pdf: ${oldMap.pdf}"
			oldValue += ", xml: ${oldMap.xml}"
		//println "PRINTLN ReportsDomain.onDelete.oldValue: ${oldValue}"
			
		String className = this.class.toString().replace('class ', '')
		//println "${now}\tAudit:DELETE::\t${oldValue}"

		def auditLogEventInstance = new AuditLogEvent(
			className: className,
			dateCreated: now,
			eventName: 'DELETE',
			lastUpdated: now,
			oldValue: oldValue,
			persistedObjectId: this.id,
			persistedObjectVersion: this.version
		)
		if ( ! auditLogEventInstance.save() ) {
			auditLogEventInstance.errors.each{
				println "${now}\tError Transacting DELETE:: \t ${it}"
			}
		}

	}

}
