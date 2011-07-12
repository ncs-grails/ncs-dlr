package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ApplicationLog {

	static auditable = true
	
    Date logDate = new Date()
    String sourceIpAddress
    String username
    String event
    String appCreated = 'ncs-dlr'

	def onDelete = { oldMap ->
		
		def now = new Date()
		def oldItemsIds = oldMap.items?.collect{it.id}.join(',')

        String oldValue = "Deleting Application Log for Items: ${oldItemsIds}"
			oldValue += ", applicationLog.id: ${oldMap.id}"
			oldValue += ", logDate: ${oldMap.logDate}"
			oldValue += ", sourceIpAddress: ${oldMap.sourceIpAddress}"
			oldValue += ", username: ${oldMap.username}"
			oldValue += ", event: ${oldMap.event}"
			oldValue += ", appCreated: ${oldMap.appCreated} "
		
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

	String toString() {
       "${logDate}\t${sourceIpAddress}\t${username}\t${event}"
    }

    static constraints = {
        logDate()
        sourceIpAddress()
        username(nullable:true)
        event()
        appCreated()
    }
    
}
