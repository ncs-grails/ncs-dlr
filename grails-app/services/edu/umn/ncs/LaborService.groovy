package edu.umn.ncs
import org.joda.time.*

class LaborService {

    static transactional = true
    static debug = true

    def getReportingStaff(principal) {

        def username = principal.getUsername()  // getUsername() came from UMN Acegi
        def reportingStaff = ReportingStaff.findByUsername(username)

        if ( !reportingStaff ) {

            // see edu.umn.auth.UmnCookiesUserDetails
            reportingStaff = new ReportingStaff(username:username)
            reportingStaff.email = principal.getEmail()
            reportingStaff.fullName = principal.getFullName()
            reportingStaff.userCreated = username
            if ( debug ) {
                println "PRINTLN userService.getReportingStaff.getInfo: ${principal.getInfo()}"
            }

            def nameParts = reportingStaff.fullName.tokenize()
            if ( debug ) {
                println "PRINTLN userService.getReportingStaff.nameParts ${nameParts.size()}"
            }
            if ( nameParts.size() == 2 ) {
                reportingStaff.firstName = nameParts[0]
                reportingStaff.lastName = nameParts[1]
            }
            if ( nameParts.size() == 3 ) {
                reportingStaff.firstName = nameParts[0]
                reportingStaff.middleInit = nameParts[1]
                reportingStaff.lastName = nameParts[2]
            }
            if ( debug ) {
                println "PRINTLN userService.getReportingStaff.nameParts[0] ${nameParts[0]}"
                println "PRINTLN userService.getReportingStaff.nameParts[1] ${nameParts[1]}"
                println "PRINTLN userService.getReportingStaff.nameParts[2] ${nameParts[2]}"
            }

            reportingStaff.save(flush:true)


        } //if ( !reportingStaff )

        return reportingStaff

    } //def getReportingStaff(principal)

    def getCurrentReportingPeriod() {

        def today = new LocalDate()
        def midnight = new LocalTime(0, 0)
        def lastMonth = today.minusMonths(1)
        lastMonth = lastMonth.minusDays(lastMonth.dayOfMonth - 1)

        // Now we have the DateTime of midnight of the first day of last month!
        def lastMonthDate = lastMonth.toDateTime(midnight).toCalendar().getTime()

        def reportingPeriodInstance = ReportingPeriod.findByPeriodDate(lastMonthDate)

        if (!reportingPeriodInstance ) {
            // create a new reportin period
        } //if (!reportingPeriodInstance )

        return reportingPeriodInstance

    } //def getCurrentReportingPeriod()
    
}
