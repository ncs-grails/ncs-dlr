package edu.umn.ncs

class ReportingPeriod {

    Integer referenceInvoiceNumber
    Date periodDate
    Date preparedDate

    static hasMany = [assignedEfforts: AssignedEffort]

    static constraints = {

        referenceInvoiceNumber()
        periodDate()
        preparedDate()

    }

}
