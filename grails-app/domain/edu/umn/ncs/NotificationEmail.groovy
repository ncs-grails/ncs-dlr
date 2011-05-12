package edu.umn.ncs
import org.joda.time.*

class NotificationEmail {

    Date dateSent = new Date()
    String userSent

    static belongsTo = [assignedEffort: AssignedEffort]
    static transients = ['year', 'month', 'day']

    Integer getYear() {
        def localYear = new LocalDate(periodDate)
        return localYear.year
    }
    Integer getMonth() {
        def localMonth = new LocalDate(periodDate)
        return localMonth.monthOfYear
    }
    Integer getDay() {
        def localDay = new LocalDate(periodDate)
        return localDay.dayOfMonth
    }

    String toString() {
        "${dateSent}"
    }

    static constraints = {
        assignedEffort()
        dateSent()
        userSent(blank:false)
    }
    
}
