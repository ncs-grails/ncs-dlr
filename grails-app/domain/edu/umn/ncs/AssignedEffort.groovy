package edu.umn.ncs

class AssignedEffort {
 
    LaborCategory laborCategory
    BigDecimal assignedEffort
    Date dateAssigned = new Date()
    ReportingStaff assigningStaff
    String appCreated = 'ncs-dlr'       // application used to assign effort
    Date dateCommitted
    ReportingStaff commitingStaff

    AssignedEffort getPreviousAssignedEffort() {
        def assignedEffort = null

        // TODO: look it up here

        return assignedEffort
    }

    String toString() {
        assignedEffort
    }

    def transients = ['previousAssignedEffort']

    static belongsTo = [reportingStaff: ReportingStaff, period: ReportingPeriod]
    static hasMany = [emails: NotificationEmail, reportedEfforts: ReportedEffort]

    static constraints = {
        reportingStaff()
        laborCategory()
        period()
        assignedEffort(blank:false, max:1.0, scale:3)
        dateAssigned()
        assigningStaff()
        appCreated(blank:false)
        dateCommitted()
        commitingStaff()
    }

}
