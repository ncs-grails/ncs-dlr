package edu.umn.ncs
import org.joda.time.*

class LaborService {

    static transactional = true

    def authenticateService
    def mailService

	def debug = true
	
    def getReportingStaff(principal) {

		def uname = principal.getUsername()          
        if (debug) { println "=> LaborService.getReportingStaff.uname: ${uname}" }

        def reportingStaff = ReportingStaff.findByUsername(uname)
        if (debug) { println "=> LaborService.getReportingStaff.reportingStaff: ${reportingStaff}" }
	
        if ( !reportingStaff ) {

			if (debug) { "=> if(!reportingStaff) = TRUE" }
			
			// get info about logged user from cookie (see edu.umn.auth.UmnCookiesUserDetails)
            reportingStaff = new ReportingStaff(username:uname)
			if (debug) { println "=> LaborService.getReportingStaff.reportingStaff: ${reportingStaff}" }
			
            reportingStaff.email = principal.getEmail()
			if (debug) { println "=> LaborService.getReportingStaff.reportingStaff.email: ${reportingStaff.email}" }
			
            reportingStaff.fullName = principal.getFullName()
			if (debug) { println "=> LaborService.getReportingStaff.reportingStaff.fullName: ${reportingStaff.fullName}" }
			
            reportingStaff.userCreated = uname
			if (debug) { println "=> LaborService.getReportingStaff.reportingStaff.userCreated: ${reportingStaff.userCreated}" }
			        
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
            if (debug) { println "=> LaborService.getReportingStaff.nameParts = ${nameParts.size()}" }
            if (debug) { println "=> LaborService.getReportingStaff.nameParts[0] = ${nameParts[0]}" }
            if (debug) { println "=> LaborService.getReportingStaff.nameParts[1] = ${nameParts[1]}" }
            if (debug) { println "=> LaborService.getReportingStaff.nameParts[2] = ${nameParts[2]}" }

            reportingStaff.save(flush:true)

        } //if ( !reportingStaff )

        return reportingStaff

    } 
	

    def getCurrentReportingPeriod() {

        // get today's date
        def today = new LocalDate()
        if (debug) { println "=> LaborService.getCurrentReportingPeriod.today = ${today}" }

        def midnight = new LocalTime(0, 0)
        if (debug) { println "=> LaborService.getCurrentReportingPeriod.midnight = ${midnight}" }

        // get first day of last month based on today's date
        def lastMonth = today.minusMonths(1)
        if (debug) { println "=> LaborService.getCurrentReportingPeriod.lastMonth = ${lastMonth}" }
		
        lastMonth = lastMonth.minusDays(lastMonth.dayOfMonth - 1)
        if (debug) { println "=> LaborService.getCurrentReportingPeriod.lastMonth = ${lastMonth}" }

        def lastMonthDate = lastMonth.toDateTime(midnight).toCalendar().getTime()
        if (debug) { println "=> LaborService.getCurrentReportingPeriod.lastMonthDate = ${lastMonthDate}" }

        def reportingPeriodInstance = ReportingPeriod.findByPeriodDate(lastMonthDate)
        if (debug) { println "=> LaborService.getCurrentReportingPeriod.reportingPeriodInstance = ${reportingPeriodInstance}" }

        //if current reporting period does not exist in db, insert a new one in there
        if ( !reportingPeriodInstance ) {
			if (debug) { println "=> LaborService.getCurrentReportingPeriod.if(!reportingPeriodInstance) = TRUE" }
			
            reportingPeriodInstance = new ReportingPeriod(periodDate:lastMonthDate)
            reportingPeriodInstance.save(flush:true)
            if (debug) { println "=> LaborService.getCurrentReportingPeriod.reportingPeriodInstance = ${reportingPeriodInstance}" } 
        }

        return reportingPeriodInstance

    } //def getCurrentReportingPeriod()

	

    def getNextReportingPeriodDateTime(paramReportingPeriodDate) {

        def theReportingPeriodDate = new LocalDate(paramReportingPeriodDate)
        if (debug) { println "=> LaborService.getNextReportingPeriodDate.theReportingPeriodDate = ${theReportingPeriodDate}" }

        def midnight = new LocalTime(0, 0)
		if (debug) { println "=> LaborService.getNextReportingPeriodDate.midnight = ${midnight}" }
		
        def nextReportingPeriodDate = theReportingPeriodDate.plusMonths(1)
        if (debug) { println "=> LaborService.getNextReportingPeriodDate.nextReportingPeriodDate = ${nextReportingPeriodDate}" }

        def nextReportingPeriodDateTime = nextReportingPeriodDate.toDateTime(midnight).toCalendar().getTime()
        if (debug) { println "=> LaborService.getNextReportingPeriodDate.nextReportingPeriodDateTime = ${nextReportingPeriodDateTime}" }

        return nextReportingPeriodDateTime

    } 
	

    def getActiveStudyActivityList() {
        
        def csa = StudyActivity.createCriteria()        
        def studyActivityList = csa.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //if (debug) { LaborService.getActiveStudyActivityList: ${studyActivityList}" }        
        
        return studyActivityList
        
    }

    def getActiveStudyTaskList() {
        
        def cst = StudyTask.createCriteria()        
        def studyTaskList = cst.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //if (debug) { LaborService.getActiveStudyTaskList: ${getActiveStudyTaskList}" }        
                        
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
				
				if (debug) { println "=> getReportingPeriodData: adding row ${it}: ${row}" }

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
		
        if (debug) { println "=> LaborService.getCombineReportedEffortConverted.sumConverted: ${sumConverted}" }
		if (debug) { println "=> LaborService.getCombineReportedEffortConverted.entryConverted: ${entryConverted}" }
		
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
		
	} 
	
	
    def sendEmailNotification (periodId, staffId) {
        
        // LOG-IN USER (SENDER)
        def principal = authenticateService.principal()
        def username = principal.getUsername()
        def loginReportingStaffInstance = ReportingStaff.findByUsername(username)
        if (debug) { println "LaborService.sendEmailNotification.loginReportingStaffInstance = ${loginReportingStaffInstance}" }
		
		if ( loginReportingStaffInstance ) {
	        if (debug) { println "=> LaborService.sendEmailNotification.loginReportingStaffInstance.username = ${loginReportingStaffInstance.username}" }
	        if (debug) { println "=> LaborService.sendEmailNotification.loginReportingStaffInstance.email = ${loginReportingStaffInstance.email}" }
		}

        // REPORTING STAFF
        def reportingStaffInstance = ReportingStaff.read(staffId)
        if (debug) { println "=> LaborService.sendEmailNotification.reportingStaffInstance = ${reportingStaffInstance}" }
		
		if ( reportingStaffInstance ) {
			if (debug) { "=> LaborService.sendEmailNotification.reportingStaffInstance.email = ${reportingStaffInstance.email}" }
		}

        //REPORTING PERIOD  
		def reportingPeriodInstance = ReportingPeriod.read(periodId)
		if (debug) { println "=> LaborService.sendEmailNotification.reportingPeriodInstance.id: ${reportingPeriodInstance.id}" }
		if (debug) { println "=> LaborService.sendEmailNotification.reportingPeriodInstance: ${reportingPeriodInstance}" }
		
		//REPORTING DUE DATE
		def reportingDueDateString
		
		if ( reportingPeriodInstance ) {
			
			def reporingDueDate = new LocalDate(reportingPeriodInstance.periodDate)
			if (debug) { println "=> LaborService.sendEmailNotification.reporingDueDate: ${reporingDueDate}" }
			
			reporingDueDate = reporingDueDate.plusMonths(1).plusDays(19)
			if (debug) { println "=> LaborService.sendEmailNotification.reporingDueDate: ${reporingDueDate}" }
			
			def reportingDueDateMonthName = reporingDueDate.toString('MMMM')
			if (debug) { println "=> LaborService.sendEmailNotification.reportingDueDateMonthName = ${reportingDueDateMonthName}" }
	
			def reportingDueDateDay = reporingDueDate.toString('dd')
			if (debug) { println "=> LaborService.sendEmailNotification.reportingDueDateDay = ${reportingDueDateDay}" }

			def reportingDueDateYear = reporingDueDate.toString('yyyy')
			if (debug) { println "=> LaborService.sendEmailNotification.reportingDueDateYear = ${reportingDueDateYear}" }
	
			reportingDueDateString = reportingDueDateMonthName + ' ' + reportingDueDateDay + ' ' + reportingDueDateYear
			
		}				
		if (debug) { println "=> LaborService.sendEmailNotification.reportingDueDateString = ${reportingDueDateString}" }

		// ASSIGNED EFFORT 
		def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
		if (debug) { println "=> LaborService.sendEmailNotification.assignedEffortInstance: ${assignedEffortInstance}" }
		
		if ( assignedEffortInstance ) {
			if (debug) { println "=> LaborService.sendEmailNotification.assignedEffortInstance.id: ${assignedEffortInstance.id}" }
		} 
		
		// send & record NOTIFICATION EMAIL
		if ( reportingStaffInstance && loginReportingStaffInstance && reportingPeriodInstance) {

			// subject title (initial or reminder)
			def notificationEmailInstanceList
			def emailSubjectTitle
			
			notificationEmailInstanceList = assignedEffortInstance.emails
			if (debug) { println "=> LaborService.sendEmailNotification.notificationEmailInstanceList: ${notificationEmailInstanceList}" }
	
			if ( notificationEmailInstanceList ) {
				emailSubjectTitle = "Reminder - NCS Direct Labor Report due ${reportingDueDateString}"
			} else {
				emailSubjectTitle = "Notification - NCS Direct Labor Report due ${reportingDueDateString}"
			}
			if (debug) { println "=> LaborService.sendEmailNotification.emailSubjectTitle: ${emailSubjectTitle}" }

			// send email						
			mailService.sendMail {
				to reportingStaffInstance.email
				from loginReportingStaffInstance.email
				subject emailSubjectTitle
				body(
					view:"/assignEffort/notificationEmail",
					model:[
						reportingPeriodInstance: reportingPeriodInstance,
						reportingStaffInstance: reportingStaffInstance
					]
				)
			}
			if (debug) { println "=> LaborService.sendEmailNotification.call mailService.sendMail" }
			
			// record NOTIFICATION EMAIL into db
			def notificationEmailInstance = new NotificationEmail(
				assignedEffort:assignedEffortInstance,
				userSent:loginReportingStaffInstance.username
			)
			if ( notificationEmailInstance.save(flush:true) ) {
				if (debug) {
					println "=> LaborService.sendEmailNotification.notificationEmailInstance.save SUCCESSFULLY"
					println "=> LaborService.sendEmailNotification.newly created notificationEmailInstance.id:${notificationEmailInstance.id} "
					println "=> LaborService.sendEmailNotification.newly created notificationEmailInstance.assignedEffort:${notificationEmailInstance.assignedEffort} "
					println "=> LaborService.sendEmailNotification.newly created notificationEmailInstance.dateSent:${notificationEmailInstance.dateSent} "
					println "=> LaborService.sendEmailNotification.newly created notificationEmailInstance.userSent:${notificationEmailInstance.userSent} "
				}
			} else {
				if (debug) { println "=> notificationEmailInstance.save FAILED" }
				notificationEmailInstance.errors.each{
					if (debug) { println it }
				}
			}
			
			return 'sent email!'
			
		} //if ( reportingStaffInstance && loginReportingStaffInstance && reportingPeriodInstance)        
		
    } 

	
	def generateReportEmail (periodId) {

		// REPORTING PERIOD
		def reportingPeriodInstance = ReportingPeriod.read(periodId)		
		if (debug) { println "=> LaborService.generateReportEmail.reportingPeriodInstance.id: ${reportingPeriodInstance.id}" } 
		if (debug) { println "=> LaborService.generateReportEmail.reportingPeriodInstance: ${reportingPeriodInstance}" }
		
		def reportingPeriodMonthName = reportingPeriodInstance.month.toString('MMMM')
		if (debug) { println "=> LaborService.generateReportEmail.reportingDueDateMonthName = ${reportingDueDateMonthName}" }
		
		def reportingPeriodYear = reportingDueDate.year.toString('yyyy')		
		if (debug) { println "=> LaborService.generateReportEmail.reportingPeriodYear = ${reportingPeriodYear}" }
		
		def reportingPeriodString = g.formatDate(date:reportingPeriodInstance.periodDate, format:'MMMM yyyy')
		//periodSelectiontList.add([id:it.id, name: g.formatDate(date:it.periodDate, format:'MMMM yyyy')])
		if (debug) { println "=> LaborService.generateReportEmail.reportingPeriodString = ${reportingPeriodString}" }
		
		// email SUBJECT
		def emailSubjectTitle = "NCS DLR: generate reports for reporting period ${reportingPeriodString}"
		if (debug) { println "=> LaborService.generateReportEmail.emailSubjectTitle = ${emailSubjectTitle}" }		
		
		// email TO list
		//def toEmailList = "sqv@cccs.umn.edu, gdw@cccs.umn.edu, jaf@cccs.umn.edu, will1945@umn.edu, bsteward@umn.edu"
		def toEmailList = "sqv@cccs.umn.edu, sqv@umn.edu" 
		if (debug) { println "=> LaborService.generateReportEmail.toEmailList = ${toEmailList}" }
		
		// email FROM list
		def fromEmailList = "sqv@cccs.umn.edu"
		if (debug) { println "=> LaborService.generateReportEmail.fromEmailList = ${fromEmailList}" }
		
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
		if (debug) { println "=> called mailService.sendMail" }
		
		return 'sent Generate Report email!'
				
	} 

				 
} //class LaborService
