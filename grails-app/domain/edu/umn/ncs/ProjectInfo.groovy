package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/**
This class represents some informtion about the DLR to be pulled into reports.
*/
class ProjectInfo {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true

	/** This is the study contract number. */
	String contractNumber
	/** Name of the principle investigator for the study.  */
	String principalInvestigator
	/** This is the study contract period. The period represents fiscal year. */
	String contractPeriod

	/** Non-default constraints for this class 
	<dl>
		<dt>contractNumber(</dt>
			<dd>cannot be blank</dd>
		<dt>principalInvestigator(</dt>
			<dd>cannot be blank</dd>
		<dt>contractPeriod(</dt>
			<dd>cannot be blank</dd>
	</dl>	
	*/
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
