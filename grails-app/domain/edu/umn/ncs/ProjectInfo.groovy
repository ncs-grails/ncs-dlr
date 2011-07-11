package edu.umn.ncs

class ProjectInfo {

    String principalInvestigator
    String contractNumber
    String sponsoredFinancialReporting
    Date startDate

	def onDelete = { oldMap ->
		
		def now = new Date()

		// describe items to delete
		String oldValue = "Deleting Project Info, projectInfo.id, principalInvestigator, contractNumber, sponsoredFinancialReporting, startDate "

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

    static constraints = {
        principalInvestigator(blank:false)
        contractNumber(blank:false)
        sponsoredFinancialReporting(blank:false)
        startDate()
    }

}
