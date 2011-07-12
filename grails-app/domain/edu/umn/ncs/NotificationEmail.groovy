package edu.umn.ncs
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class NotificationEmail {

	static auditable = true
	
    Date dateSent = new Date()
    String userSent
	
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
