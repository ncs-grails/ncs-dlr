package edu.umn.ncs
import java.util.Date;
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

//TODO: explain what etdlr is
class StudyTaskEtdlr {

	static auditable = true

	// SELECT etdlrTaskId, etdlrTaskDesc, Obsolete, EnteredWhen, EnteredByWhom, EnteredByWhat 
	// FROM WebLookup.dbo.ncs_task_etdlr
	
    String name
    Boolean obsolete = true
    Date dateCreated = new Date()
    String userCreated
    String appCreated = 'ncs-dlr'

	String toString() {
		name
	}

	static constraints = {
		name(maxSize:1024)
		obsolete()
		dateCreated(blank:false)
		userCreated(blank:false)
		appCreated(blank:false)
	}
	
	static mapping = { sort "name" }

	def onDelete = { oldMap ->
		
		def now = new Date()
		
        String oldValue = "Task ETDLR"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated}"
		//println "PRINTLN EtdlrTaskDomain.onDelete.oldValue: ${oldValue}"
			
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
