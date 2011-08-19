package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class StudyTask {

	static auditable = true
	
    String name
    Boolean obsolete = true
	StudyTaskEtdlr studyTaskEtdlr
	StudyTaskOde studyTaskOde
    Date dateCreated = new Date()
    String userCreated
    String appCreated = 'ncs-dlr'
	
	
	def onDelete = { oldMap ->
		
		def now = new Date()
		
        String oldValue = "Study Task"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
			oldValue += ", etdlrCode: ${oldMap.etdlrCode}"
			oldValue += ", odeCode: ${oldMap.odeCode} "
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated}"
		//println "PRINTLN StudyTaskDomain.onDelete.oldValue: ${oldValue}"
			
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
	
    String toString() {
        name
    }

    static constraints = {
        name(maxSize:1024)
        obsolete()
        studyTaskEtdlr(nullable:true)
        studyTaskOde(nullable:true)
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)
    }
    
    static mapping = { sort "name" }

}
