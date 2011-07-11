package edu.umn.ncs

class ApplicationLog {

    Date logDate = new Date()
    String sourceIpAddress
    String username
    String event
    String appCreated = 'ncs-dlr'

    def onDelete = { oldMap ->
        
        def now = new Date()

        // describe items to delete
        String oldValue = "Deleting Application Log, applicationLog.id, logDate, sourceIpAddress, username, event, appCreated "

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
