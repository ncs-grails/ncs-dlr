package edu.umn.ncs

class AssignedEffort {
 
    LaborCategory laborCategory
    BigDecimal assignedEffort
    Date dateAssigned = new Date()
    ReportingStaff assigningStaff
    String appCreated = 'ncs-dlr'       // application used to assign effort
    Date dateCommitted
    ReportingStaff commitingStaff

    def transients = ['previousAssignedEffort']

    static belongsTo = [reportingStaff: ReportingStaff, period: ReportingPeriod]
    static hasMany = [emails: NotificationEmail, reportedEfforts: ReportedEffort]

    AssignedEffort getPreviousAssignedEffort() {
        def assignedEffort = null

        // TODO: look it up here

        return assignedEffort
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
