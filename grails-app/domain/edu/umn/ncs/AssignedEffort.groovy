package edu.umn.ncs

class AssignedEffort {
 
    LaborCategory laborCategory
    BigDecimal assignedEffort
    Date dateAssigned = new Date()
    ReportingStaff assigningStaff
    // application used to assign effort
    String appCreated = 'ncs-dlr'
    Date dateCommitted
    ReportingStaff commitingStaff

    String toString() {
        assignedEffort
    }

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
