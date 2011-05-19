package edu.umn.ncs
import org.joda.time.*

class LaborService {

    static transactional = true
    static debug = true

    def authenticateService
    def mailService

    def getReportingStaff(principal) {

        def uname = principal.getUsername()          //getUsername() came from UMN Acegi
        // println "PRINTLN LaborService.getReportingStaff.principal.getUsername().uname: ${uname}"

        def reportingStaff = ReportingStaff.findByUsername(uname)
        // println "PRINTLN LaborService.getReportingStaff.ReportingStaff.findByUsername(uname).reportingStaff: ${reportingStaff}"
        
        if ( !reportingStaff ) {

            // get info about logged user from cookie (see edu.umn.auth.UmnCookiesUserDetails)
            reportingStaff = new ReportingStaff(username:uname)
            reportingStaff.email = principal.getEmail()
            reportingStaff.fullName = principal.getFullName()
            reportingStaff.userCreated = uname
            // println "PRINTLN LaborService.getReportingStaff.if(!reportingStaff).getInfo: ${principal.getInfo()}"
        
            def nameParts = reportingStaff.fullName.tokenize()
            if ( nameParts.size() == 2 ) {
                reportingStaff.firstName = nameParts[0]
                reportingStaff.lastName = nameParts[1]
            }
            if ( nameParts.size() == 3 ) {
                reportingStaff.firstName = nameParts[0]
                reportingStaff.middleInit = nameParts[1]
                reportingStaff.lastName = nameParts[2]
            }
            // println "PRINTLN LaborService.getReportingStaff.nameParts = ${nameParts.size()}"
            // println "PRINTLN LaborService.getReportingStaff.nameParts[0] = ${nameParts[0]}"
            // println "PRINTLN LaborService.getReportingStaff.nameParts[1] = ${nameParts[1]}"
            // println "PRINTLN LaborService.getReportingStaff.nameParts[2] = ${nameParts[2]}"

            

            reportingStaff.save(flush:true)

        } //if ( !reportingStaff )

        // println "PRINTLN LaborService.getReportingStaff.return.reportingStaff: ${reportingStaff}"
        return reportingStaff

    } //def getReportingStaff(principal)

    def getCurrentReportingPeriod() {

        // get today's date
        def today = new LocalDate()
        // println "PRINTLN LaborService.getCurrentReportingPeriod.today = ${today}"

        def midnight = new LocalTime(0, 0)
        // println "PRINTLN LaborService.getCurrentReportingPeriod.midnight = ${midnight}"

        // get first day of last month based on today's date
        def lastMonth = today.minusMonths(1)
        // println "PRINTLN LaborService.getCurrentReportingPeriod.today.minusMonths(1).lastMonth = ${lastMonth}"
        lastMonth = lastMonth.minusDays(lastMonth.dayOfMonth - 1)
        // println "PRINTLN LaborService.getCurrentReportingPeriod.lastMonth = ${lastMonth}"

        def lastMonthDate = lastMonth.toDateTime(midnight).toCalendar().getTime()
        // println "PRINTLN LaborService.getCurrentReportingPeriod.lastMonthDate = ${lastMonthDate}"

        def reportingPeriodInstance = ReportingPeriod.findByPeriodDate(lastMonthDate)
        // println "PRINTLN LaborService.getCurrentReportingPeriod.ReportingPeriod.findByPeriodDate(lastMonthDate).reportingPeriodInstance = ${reportingPeriodInstance}"

        //if current reporting period does not exist in db, insert a new one in there
        if ( !reportingPeriodInstance ) {
            reportingPeriodInstance = new ReportingPeriod(periodDate:lastMonthDate)
            reportingPeriodInstance.save(flush:true)
            // println "PRINTLN LaborService.getCurrentReportingPeriod.reportingPeriodInstance = ${reportingPeriodInstance}"
        }

        return reportingPeriodInstance

    } //def getCurrentReportingPeriod()


    def getNextReportingPeriodDateTime(paramReportingPeriodDate) {

        def theReportingPeriodDate = new LocalDate(paramReportingPeriodDate)
        println "PRINTLN LaborService.getNextReportingPeriodDate.theReportingPeriodDate = ${theReportingPeriodDate}"

        def midnight = new LocalTime(0, 0)

        def nextReportingPeriodDate = theReportingPeriodDate.plusMonths(1)
        println "PRINTLN LaborService.getNextReportingPeriodDate.nextReportingPeriodDate = ${nextReportingPeriodDate}"

        def nextReportingPeriodDateTime = nextReportingPeriodDate.toDateTime(midnight).toCalendar().getTime()
        println "PRINTLN LaborService.getNextReportingPeriodDate.nextReportingPeriodDateTime = ${nextReportingPeriodDateTime}"

        return nextReportingPeriodDateTime

    } //def getNextReportingPeriodDate
    
    
    def sendEmailNotification (periodId, staffId) {
        
        // Get logged in user account
        def principal = authenticateService.principal()
        def username = principal.getUsername()
        def loggedInReportingStaffInstance = ReportingStaff.findByUsername(username)
        println "PRINTLN laborService.sendEmailNotification.loggedInReportingStaffInstance: ${loggedInReportingStaffInstance}"
        println "LaborService.sendEmailNotification.loggedInReportingStaffInstance.email = ${loggedInReportingStaffInstance.email}"

        def reportingStaffInstance = ReportingStaff.read(staffId)
        println "LaborService.sendEmailNotification.reportingStaffInstance = ${reportingStaffInstance}"        
        println "LaborService.sendEmailNotification.reportingStaffInstance.email = ${reportingStaffInstance.email}"

        //Get next month/year after current reporting period
        def currentReportingPeriodInstance = ReportingPeriod.read(periodId)
        println "PRINTLN LaborService.sendEmailNotification.currentReportingPeriodInstance: ${currentReportingPeriodInstance}"
        
        def currentReportingPeriodDate = currentReportingPeriodInstance .periodDate
        println "PRINTLN LaborService.sendEmailNotification.currentReportingPeriodDate: ${currentReportingPeriodDate}"
                
        def reportingDueDate = getNextReportingPeriodDateTime(currentReportingPeriodDate)
        println "PRINTLN LaborService.sendEmailNotification.reportingDueDate: ${reportingDueDate}"
        
        // Get curent period's assigned effort instance
        def currentPeriodAssignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(currentReportingPeriodInstance,reportingStaffInstance)
        println "PRINTLN LaborService.sendEmailNotification.currentPeriodAssignedEffortInstance: ${currentPeriodAssignedEffortInstance}"
        
        // Get notification email instance, if there is one
        def notificationEmailInstanceList = currentPeriodAssignedEffortInstance.emails
        println "PRINTLN LaborService.sendEmailNotification.notificationEmailInstanceList: ${notificationEmailInstanceList}"

        // if notification email instance already exists, send a REMINDER
        def emailSubjectTitle
        
        if ( notificationEmailInstanceList ) {
        println "PRINTLN LaborService.sendEmailNotification.if(notificationEmailInstance) = TRUE"
            emailSubjectTitle = "Reminder - NCS Direct Labor Report due ${g.formatDate(date:reportingDueDate, format:'MMMM')} 20 ${g.formatDate(date:reportingDueDate, format:'yyyy')}"
        // otherwise, send an INITIAL
        } else {
        println "PRINTLN LaborService.sendEmailNotification.if(notificationEmailInstance) = FALSE"
            emailSubjectTitle = "Notification - NCS Direct Labor Report due ${g.formatDate(date:reportingDueDate, format:'MMMM')} 20 ${g.formatDate(date:reportingDueDate, format:'yyyy')}"                        
        }
        
        mailService.sendMail {
            to reportingStaffInstance.email
            from loggedInReportingStaffInstance.email
            subject emailSubjectTitle
            body( 
                view:"/assignEffort/email",
                model:[ 
                    reportingPeriodInstance: reportingPeriodInstance, 
                    reportingStaffInstance: reportingStaffInstance
                ]
            )                    
        } //mailService.sendMail

        // create row that email was sent
        def notificationEmailInstance = new NotificationEmail(
            assignedEffort:currentPeriodAssignedEffortInstance,
            userSent:loggedInReportingStaffInstance
        )
        notificationEmailInstance.save(flush:true)
        
        return 'sent email!'
    }
    
} //class LaborService
