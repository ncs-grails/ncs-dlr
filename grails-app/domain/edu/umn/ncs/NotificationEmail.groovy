package edu.umn.ncs
import org.joda.time.*

class NotificationEmail {

    Date dateSent = new Date()
    String userSent

	def onDelete = { oldMap ->
		
		def now = new Date()

		// describe items to delete
		String oldValue = "Deleting Notification Email for assignedEffort.id ${oldMap.assignedEffort.id}, notificationEmail.id, dateSent, userSent "

		String className = this.class.toString().replace('class ', '')
		//println "${now}\tAudit:DELETE::\t${oldValue}"

		// transaction auditing
		def auditLogEventInstance = new AuditLogEvent(
			className: className,
			dateCreated: now,
			eventName: 'DELETE',
			lastUpdated: now,
			oldValue: oldValue,
			persistedObjectId: this.id,
			persistedObjectVersion: this.version)
			if ( ! auditLogEventInstance.save() ) {
				auditLogEventInstance.errors.each{
					println "${now}\tError Transacting DELETE:: \t ${it}"
				}
			}

	} //def onDelete

	
	static belongsTo = [assignedEffort: AssignedEffort]
   
    String toString() {
        "${dateSent}"
    }

    static constraints = {
        assignedEffort()
        dateSent()
        userSent(blank:false)
    }
    
}
