package edu.umn.ncs

class AssignedEffort {
 
    LaborCategory laborCategory
    BigDecimal assignedEffort
    Date dateAssigned = new Date()
    ReportingStaff assigningStaff
    String appCreated = 'ncs-dlr'       // application used to assign effort
    Date dateCommitted
    ReportingStaff commitingStaff

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
