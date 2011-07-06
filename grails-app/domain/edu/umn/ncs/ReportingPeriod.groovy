package edu.umn.ncs
import org.joda.time.*

class ReportingPeriod {

    Integer referenceInvoiceNumber
    Date periodDate
    Date preparedDate
    Date completedReportDate

    static hasMany = [assignedEfforts: AssignedEffort]
    static transients = ['year', 'month', 'previousPeriod', 'nextPeriod']

    Integer getYear() {
        def localYear = new LocalDate(periodDate)
        return localYear.year
    }
    Integer getMonth() {
        def localMonth = new LocalDate(periodDate)
        return localMonth.monthOfYear
    }
    
    ReportingPeriod getPreviousPeriod() {
        
        ReportingPeriod.createCriteria().get{
            lt("periodDate", periodDate)
            maxResults(1)
            order("periodDate","desc")
        }
        
    }
    
    ReportingPeriod getNextPeriod() {
        
        ReportingPeriod.createCriteria().get{
            gt("periodDate", periodDate)
            maxResults(1)
            order("periodDate")
        }
        
    }

    String toString() {
        "${month}/${year}"
    }
    
    static constraints = {
        referenceInvoiceNumber(nullable:true)
        periodDate()
        preparedDate(nullable:true)
        completedReportDate(nullable:true)
    }

}
