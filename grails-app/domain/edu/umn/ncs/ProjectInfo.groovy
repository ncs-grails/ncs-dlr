package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ProjectInfo {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true

	String contractNumber
	String principalInvestigator
	String contractPeriod

	static constraints = {
		contractNumber(blank:false)
		principalInvestigator(blank:false)
		contractPeriod(blank:false)
	}

	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->

		def now = new Date()

		String oldValue = "Project Info"
		oldValue += ", principalInvestigator: ${oldMap.principalInvestigator}"
		oldValue += ", contractNumber: ${oldMap.contractNumber}"
		//println "ProjectInfoDomain.onDelete.oldValue: ${oldValue}"

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
