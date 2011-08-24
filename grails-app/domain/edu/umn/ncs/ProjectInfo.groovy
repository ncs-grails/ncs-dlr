package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ProjectInfo {
	
	static auditable = true
	
    String contractNumber
    String principalInvestigator

    static constraints = {
        contractNumber(blank:false)
        principalInvestigator(blank:false)
    }
	
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

	} //def onDelete

}
