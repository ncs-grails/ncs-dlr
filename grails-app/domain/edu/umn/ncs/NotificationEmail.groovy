package edu.umn.ncs
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/** This class represents Initial and Reminder emais sent out to staff, asking them to report their work efforts for a given period. The initial email is the first email sent out based on "dateSent" and subsequent emails, after the initial, are reminders. */
class NotificationEmail {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin. */
	static auditable = true

	/** Date and tiem email was sent out to staff. */
	Date dateSent = new Date()
	/** User account of person who sent out the email, which is usually a DLR application administrator.  */
	String userSent

	/** Variable "assignedEffort" pertains to the Assigned Effort related to the email. */
	static belongsTo = [assignedEffort: AssignedEffort]

	/** Sets default string, for this domain, to the date email was sent out. */	
	String toString() { "${dateSent}" }

	/** Non-default constraints for this class 
	<dl>
		<dt>userSent</dt>
			<dd>cannot be blank</dd>
	</dl>	
	*/	
	static constraints = {
		assignedEffort()
		dateSent()
		userSent(blank:false)
	}

	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class. */
	def onDelete = { oldMap ->

		def now = new Date()

		String oldValue = "Notification Email associated with"
			oldValue += " assignedEffort.id ${oldMap.assignedEffort.id}"
			oldValue += ", dateSent: ${oldMap.dateSent}"
			oldValue += ", userSent: ${oldMap.userSent} "
		//println "PRINTLN NotificationEmailDomain.onDelete.oldValue: ${oldValue}"

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
