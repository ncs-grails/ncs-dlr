package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class StudyActivity {

    static auditable = true

	String name
    Boolean obsolete = true
    Date dateCreated = new Date()
    String userCreated
    String appCreated= 'ncs-dlr'

    def onDelete = { oldMap ->
        
        def now = new Date()
		
        String oldValue = "Study Activity"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated} "
		//println "PRINTLN StudyActivityDomain.onDelete.oldValue: ${oldValue}"
			
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
