package edu.umn.ncs

class StudyActivity {

    String name
    Boolean obsolete = true
    Date dateCreated = new Date()
    String userCreated
    String appCreated= 'ncs-dlr'

    def onDelete = { oldMap ->
        
        def now = new Date()

        // describe items to delete
        String oldValue = "Deleting Study Activity, studyActivity.id, name, obsolete, dateCreated, userCreated, appCreated "

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
        name
    }

    static constraints = {
        name(blank:false, maxSize:1024)
        obsolete()
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)
    }
    
    static mapping = { sort "name" }


}
