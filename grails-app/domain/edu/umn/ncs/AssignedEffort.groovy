package edu.umn.ncs

class AssignedEffort {
 
    LaborCategory laborCategory
    BigDecimal assignedEffort
    Date dateAssigned = new Date()
    ReportingStaff assigningStaff
    String appCreated = 'ncs-dlr'       
    Date dateCommitted
    ReportingStaff commitingStaff

	def onDelete = { oldMap ->
		
		def now = new Date()

		// describe items to delete
		String oldValue = "Deleting Assigned Effort for reportingStaff.id ${oldMap.reportingStaff.id} and period.id ${oldMap.period.id}, assignedEffort.id, laborCategory.id, assignedEffort, dateAssigned, assigningStaff.id, appCreated, dateCommitted, commitingStaff.id "
		
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

    static belongsTo = [reportingStaff: ReportingStaff, period: ReportingPeriod]
	// reportedEffort SHOULD BE PLURAL!
    static hasMany = [emails: NotificationEmail, reportedEffort: ReportedEffort]

    def transients = ['assignedEffortConverted']

    BigDecimal getAssignedEffortConverted() {        
        return assignedEffort * 100
    }

    void setAssignedEffortConverted(BigDecimal assignedEffortConverted) {        
        percentEffort = assignedEffortConverted / 100.00
    }

    void setAssignedEffortConverted(String assignedEffortConvertedString) {
        try {
            assignedEffortConverted = assignedEffortConvertedString.toBigDecimal()
        } finally {
            assignedEffort = assignedEffortConverted / 100.00
        }
    }

    String toString() {
        assignedEffort
    }

    static constraints = {
        reportingStaff()
        laborCategory()
        period()
        assignedEffort(max:1.0, scale:3)
        dateAssigned()
        assigningStaff(blank:false)
        appCreated(blank:false)
        dateCommitted(nullable:true)
        commitingStaff(nullable:true)
    }

}
