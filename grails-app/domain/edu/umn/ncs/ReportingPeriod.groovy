package edu.umn.ncs
import org.joda.time.*

class ReportingPeriod {

    Integer referenceInvoiceNumber
    Date periodDate
    Date preparedDate
    Date completedReportDate

    static hasMany = [assignedEfforts: AssignedEffort]
    static transients = ['year', 'month']

    Integer getYear() {
        def localYear = new LocalDate(periodDate)
        return localYear.year
    }
    Integer getMonth() {
        def localMonth = new LocalDate(periodDate)
        return localMonth.monthOfYear
    }
    
    static constraints = {
        referenceInvoiceNumber()
        periodDate()
        preparedDate(nullable:true)
        completedReportDate(nullable:true)
    }

}
