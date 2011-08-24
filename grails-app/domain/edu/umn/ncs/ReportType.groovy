package edu.umn.ncs
import java.util.Date;

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ReportType {

	static auditable = true
	
	String name
	String abbreviation
	Boolean obsolete = true
	Boolean csv = false
	Boolean pdf = false
	Boolean xml = false

	String toString() {
		name
	}

    static constraints = {
        name(blank:false, maxSize:256)
		abbreviation(blank:false, maxSize:16)
        obsolete()
		csv()
		pdf()
		xml()
    }

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