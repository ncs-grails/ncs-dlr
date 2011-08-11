package edu.umn.ncs
import java.util.Date;

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class Reports {

	static auditable = true
	
	String name
	String abbreviation
	Boolean obsolete = true
	Boolean pdf = false
	Boolean csv = false
	Boolean xml = false

	def onDelete = { oldMap ->
		
		def now = new Date()
		
		String oldValue = "Reports"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
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

	String toString() {
		name
	}

    static constraints = {
        name(blank:false, maxSize:256)
		abbreviation(blank:false, maxSize:16)
        obsolete()
    }
    
}
