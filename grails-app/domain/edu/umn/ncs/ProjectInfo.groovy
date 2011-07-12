package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ProjectInfo {
	
	static auditable = true
	
    String principalInvestigator
    String contractNumber
    String sponsoredFinancialReporting
    Date startDate

	def onDelete = { oldMap ->
		
		def now = new Date()
				
		String oldValue = "Project Info"
			oldValue += ", principalInvestigator: ${oldMap.principalInvestigator}"
			oldValue += ", contractNumber: ${oldMap.contractNumber}"
			oldValue += ", sponsoredFinancialReporting: ${oldMap.sponsoredFinancialReporting}"
			oldValue += ", startDate: ${oldMap.startDate} "
		//println "PRINTLN ProjectInfoDomain.onDelete.oldValue: ${oldValue}"
			
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

    static constraints = {
        principalInvestigator(blank:false)
        contractNumber(blank:false)
        sponsoredFinancialReporting(blank:false)
        startDate()
    }

}
