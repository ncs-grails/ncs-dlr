package edu.umn.ncs

class ReportedEffort {

    //ReportingStaff reportingStaff
    //ReportingPeriod period
    StudyActivity activity
    StudyTask task
    BigDecimal percentEffort
    Date dateCreated = new Date()
    String userCreated
    String appCreated  = 'ncs-dlr'

    static belongsTo = [assignedEffort: AssignedEffort]

    static constraints = {
        assignedEffort()
        activity()
        task()
        percentEffort(max:1.0, scale:3)
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)

    }

}