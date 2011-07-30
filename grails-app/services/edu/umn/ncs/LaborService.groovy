package edu.umn.ncs
import org.joda.time.*

class LaborService {

    static transactional = true
    static def debug = false

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
        //println "PRINTLN LaborService.getNextReportingPeriodDate.theReportingPeriodDate = ${theReportingPeriodDate}"

        def midnight = new LocalTime(0, 0)

        def nextReportingPeriodDate = theReportingPeriodDate.plusMonths(1)
        //println "PRINTLN LaborService.getNextReportingPeriodDate.nextReportingPeriodDate = ${nextReportingPeriodDate}"

        def nextReportingPeriodDateTime = nextReportingPeriodDate.toDateTime(midnight).toCalendar().getTime()
        //println "PRINTLN LaborService.getNextReportingPeriodDate.nextReportingPeriodDateTime = ${nextReportingPeriodDateTime}"

        return nextReportingPeriodDateTime

    } //def getNextReportingPeriodDate        

    def getActiveStudyActivityList() {
        
        def csa = StudyActivity.createCriteria()        
        def studyActivityList = csa.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //println "PRINTLN LaborService.getActiveStudyActivityList: ${studyActivityList}"        
        
        return studyActivityList
        
    }

    def getActiveStudyTaskList() {
        
        def cst = StudyTask.createCriteria()        
        def studyTaskList = cst.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //println "PRINTLN ReportedEffortController.create.studyTaskList: ${studyTaskList}"        
                        
        return studyTaskList
        
    }
    
	def getReportingPeriodData(ReportingPeriod reportingPeriodInstance) {
        
		def dataset = null
        
		if (reportingPeriodInstance) {
			// start the dataset
			dataset = []
			
			// for each row...
			(1..5).each{
				// create an empty row
				def row = [:]
				
				row.id = it
				row.name = "purple"
				row.color = "rock"
				row.size = "square"
				row.shape = "medium"
				row.status = "confusing"
				row.when = new Date()
				
				if (debug) { println "getReportingPeriodData: adding row ${it}: ${row}" }

				dataset.add(row)
			}
		}
        
		return dataset
        
	}
    
    def getSumOfReportedPercentEffort(assignedEffortInstance) {
        
        def cSum = ReportedEffort.createCriteria()
		
        def sumOfReportedPercentEffort = cSum.get {
            eq("assignedEffort", assignedEffortInstance)
            projections {
                sum("percentEffort")
            }
        }
		
		return sumOfReportedPercentEffort
        
    }
	
	def getCombineReportedEffortConverted(sumConverted, entryConverted) {
		
        println "PRINTLN LaborService.getCombineReportedEffortConverted.sumConverted: ${sumConverted}"
		println "PRINTLN LaborService.getCombineReportedEffortConverted.entryConverted: ${entryConverted}"
		
		def combineConverted
		
		if ( sumConverted && entryConverted ) {
			
			combineConverted = sumConverted + entryConverted
			
		} else if ( sumConverted && !entryConverted ) {
		
			combineConverted = sumConverted
			
		} else if ( !sumConverted && entryConverted ) {
		
			combineConverted = entryConverted
			
		}
		
		return combineConverted
		
	}
	
	def countNotCommittedAssignedEffort(periodId) {

		def cCount = AssignedEffort.createCriteria()
		
		def countOfNotCommittedAssignedEffort = cCount.get {
			eq("period", periodId)
			isNull("dateCommitted")
			projections {
				count("id")
			}
		}

		return countOfNotCommittedAssignedEffort		
		
	} //countNotCommittedAssignedEffort(periodId)
	
    def sendEmailNotification (periodId, staffId) {
        
		println "LABOR SERVICE > sendEmailNotification -------------------------"

        // LOGGED IN USER (SENDER)
        def principal = authenticateService.principal()
        def username = principal.getUsername()
        def loggedInReportingStaffInstance = ReportingStaff.findByUsername(username)
        //println "PRINTLN laborService.sendEmailNotification.loggedInReportingStaffInstance: ${loggedInReportingStaffInstance}"
        println "loggedInReportingStaffInstance.email = ${loggedInReportingStaffInstance.email}"

        // REPORTING STAFF
        def reportingStaffInstance = ReportingStaff.read(staffId)
        //println "LaborService.sendEmailNotification.reportingStaffInstance = ${reportingStaffInstance}"        
        println "reportingStaffInstance.email = ${reportingStaffInstance.email}"

        //REPORTING PERIOD & DUE DATE
        def reportingPeriodInstance = ReportingPeriod.read(periodId)
        println "reportingPeriodInstance: ${reportingPeriodInstance}"
        
        def reportingPeriodDate = reportingPeriodInstance.periodDate
        println "reportingPeriodDate: ${reportingPeriodDate}"
        
		/*
        def nextReportingDate = getNextReportingPeriodDateTime(currentReportingPeriodDate)
        println "nextReportingDate: ${nextReportingDate}"
        */
        
        def reportingDueDate = new LocalDate(reportingPeriodDate)        
		println "reportingDueDate = ${reportingDueDate}"
		
        def reportingDueDateMonthName = reportingDueDate.toString('MMMM')
        println "reportingDueDateMonthName = ${reportingDueDateMonthName}"

        def reportingDueDateYear = reportingDueDate.toString('yyyy')
        println "reportingDueDateYear = ${reportingDueDateYear}"

        def reportingDueDateString = reportingDueDateMonthName + ' 20 ' + reportingDueDateYear
        println "reportingDueDateString = ${reportingDueDateString}"
                
        // past NOTIFICATION EMAIL LIST
        def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
        //println "assignedEffortInstance: ${assignedEffortInstance}"
        
        def notificationEmailInstanceList = assignedEffortInstance.emails
        println "notificationEmailInstanceList: ${notificationEmailInstanceList}"

		// NOTIFICATION EMAIL		
        // subject title (initial or reminder)
        def emailSubjectTitle        
        if ( notificationEmailInstanceList ) {
            emailSubjectTitle = "Reminder - NCS Direct Labor Report due ${reportingDueDateString}"
        } else {
            emailSubjectTitle = "Notification - NCS Direct Labor Report due ${reportingDueDateString}"                        
        }
        println "PRINTLN emailSubjectTitle: ${emailSubjectTitle}"
        
        mailService.sendMail {
            to reportingStaffInstance.email
            from loggedInReportingStaffInstance.email
            subject emailSubjectTitle
            body( 
                view:"/assignEffort/notificationEmail",
                model:[ 
                    reportingPeriodInstance: reportingPeriodInstance, 
                    reportingStaffInstance: reportingStaffInstance
                ]
            )                    
        } 
        println "PRINTLN call mailService.sendMail"
        
        // INSERT NOTIFICATION EMAIL into db
        def notificationEmailInstance = new NotificationEmail(
            assignedEffort:assignedEffortInstance,
            userSent:loggedInReportingStaffInstance
        )
        notificationEmailInstance.save(flush:true)
        println "PRINTLN notificationEmailInstance.save(flush:true)"
        
        return 'sent email!'
		
    } //def sendEmailNotification (periodId, staffId)

	def generateReportEmail (periodId) {

		// REPORTING PERIOD
		def reportingPeriodInstance = ReportingPeriod.read(periodId)		
		println "PRINTLN LaborService.generateReportEmail.reportingPeriodInstance: ${reportingPeriodInstance}"

		def reportingPeriodMonthName = reportingPeriodInstancel.month.toString('MMMM')
		println "PRINTLN LaborService.generateReportEmail.reportingDueDateMonthName = ${reportingDueDateMonthName}"
		
		def reportingPeriodYear = reportingDueDate.year.toString('yyyy')		
		println "PRINTLN LaborService.generateReportEmail.reportingPeriodYear = ${reportingPeriodYear}"
		
		def reportingPeriodString = g.formatDate(date:reportingPeriodInstance.periodDate, format:'MMMM yyyy')
		//periodSelectiontList.add([id:it.id, name: g.formatDate(date:it.periodDate, format:'MMMM yyyy')])
		println "PRINTLN LaborService.generateReportEmail.reportingPeriodString = ${reportingPeriodString}"
		
		// email SUBJECT
		def emailSubjectTitle = "NCS DLR: generate reports for reporting period ${reportingPeriodString}"
		println "PRINTLN LaborService.generateReportEmail.emailSubjectTitle = ${emailSubjectTitle}"
		
		
		// email TO list
		//def toEmailList = "sqv@cccs.umn.edu, gdw@cccs.umn.edu, jaf@cccs.umn.edu, will1945@umn.edu, bsteward@umn.edu"
		def toEmailList = "sqv@cccs.umn.edu, sqv@umn.edu"
		println "PRINTLN LaborService.generateReportEmail.toEmailList = ${toEmailList}"
		
		// email FROM list
		def fromEmailList = "sqv@cccs.umn.edu"
		println "PRINTLN LaborService.generateReportEmail.fromEmailList = ${fromEmailList}"
		
		// send email
		mailService.sendMail {
			to toEmail
			from fromEmail
			subject emailSubjectTitle
			body(
				view:"/assignEffort/generateReportEmail",
				model:[reportingPeriodInstance: reportingPeriodInstance]
			)
		} 
		println "PRINTLN called mailService.sendMail"
		
		return 'sent Generate Report email!'
				
	} // generateReportEmail
			 
} //class LaborService
