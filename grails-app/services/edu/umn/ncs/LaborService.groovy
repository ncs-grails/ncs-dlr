package edu.umn.ncs
import org.joda.time.*

class LaborService {

    static transactional = true

    def authenticateService
    def mailService

	def debug = true
	
    def getReportingStaff(principal) {

		def uname = principal.getUsername()          
        if (debug) { println "=> laborService.getReportingStaff.uname: ${uname}" }

        def reportingStaff = ReportingStaff.findByUsername(uname)
        if (debug) { println "=> laborService.getReportingStaff.reportingStaff: ${reportingStaff}" }
	
        if ( !reportingStaff ) {

			if (debug) { "=> if(!reportingStaff) = TRUE" }
			
			// get info about logged user from cookie (see edu.umn.auth.UmnCookiesUserDetails)
            reportingStaff = new ReportingStaff(username:uname)
			if (debug) { println "=> laborService.getReportingStaff.reportingStaff: ${reportingStaff}" }
			
            reportingStaff.email = principal.getEmail()
			if (debug) { println "=> laborService.getReportingStaff.reportingStaff.email: ${reportingStaff.email}" }
			
            reportingStaff.fullName = principal.getFullName()
			if (debug) { println "=> laborService.getReportingStaff.reportingStaff.fullName: ${reportingStaff.fullName}" }
			
            reportingStaff.userCreated = uname
			if (debug) { println "=> laborService.getReportingStaff.reportingStaff.userCreated: ${reportingStaff.userCreated}" }
			        
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
            if (debug) { println "=> laborService.getReportingStaff.nameParts = ${nameParts.size()}" }
            if (debug) { println "=> laborService.getReportingStaff.nameParts[0] = ${nameParts[0]}" }
            if (debug) { println "=> laborService.getReportingStaff.nameParts[1] = ${nameParts[1]}" }
            if (debug) { println "=> laborService.getReportingStaff.nameParts[2] = ${nameParts[2]}" }

            reportingStaff.save(flush:true)

        } //if ( !reportingStaff )

        return reportingStaff

    } 
	

    def getCurrentReportingPeriod() {

        // today's date
        def today = new LocalDate()
        if (debug) { println "=> laborService.getCurrentReportingPeriod.today = ${today}" }

        def midnight = new LocalTime(0, 0)
        if (debug) { println "=> laborService.getCurrentReportingPeriod.midnight = ${midnight}" }

        // REPORTING PERIOD 
        def lastMonth = today.minusMonths(1)
        if (debug) { println "=> laborService.getCurrentReportingPeriod.lastMonth = ${lastMonth}" }
		
        def lastMonthPeriodDate = lastMonth.minusDays(lastMonth.dayOfMonth - 1)
        if (debug) { println "=> laborService.getCurrentReportingPeriod.lastMonthPeriodDate = ${lastMonthPeriodDate}" }

        def lastMonthPeriodDateTime = lastMonthPeriodDate.toDateTime(midnight).toCalendar().getTime()
        if (debug) { println "=> laborService.getCurrentReportingPeriod.lastMonthPeriodDateTime = ${lastMonthPeriodDateTime}" }

        def reportingPeriodInstance = ReportingPeriod.findByPeriodDate(lastMonthPeriodDateTime)
        if (debug) { println "=> laborService.getCurrentReportingPeriod.reportingPeriodInstance = ${reportingPeriodInstance}" }

        //if current reporting period does not exist in db, insert a new one in there
        if ( !reportingPeriodInstance ) {
			
			if (debug) { println "=> laborService.getCurrentReportingPeriod.if(!reportingPeriodInstance) = TRUE" }			
            reportingPeriodInstance = new ReportingPeriod(periodDate:lastMonthPeriodDateTime)
            reportingPeriodInstance.save(flush:true)
            if (debug) { println "=> laborService.getCurrentReportingPeriod.reportingPeriodInstance = ${reportingPeriodInstance}" }
			 
        }

        return reportingPeriodInstance

    } 

	

    def getNextReportingPeriodDateTime(paramReportingPeriodDate) {

        def theReportingPeriodDate = new LocalDate(paramReportingPeriodDate)
        if (debug) { println "=> laborService.getNextReportingPeriodDate.theReportingPeriodDate = ${theReportingPeriodDate}" }

        def midnight = new LocalTime(0, 0)
		if (debug) { println "=> laborService.getNextReportingPeriodDate.midnight = ${midnight}" }
		
        def nextReportingPeriodDate = theReportingPeriodDate.plusMonths(1)
        if (debug) { println "=> laborService.getNextReportingPeriodDate.nextReportingPeriodDate = ${nextReportingPeriodDate}" }

        def nextReportingPeriodDateTime = nextReportingPeriodDate.toDateTime(midnight).toCalendar().getTime()
        if (debug) { println "=> laborService.getNextReportingPeriodDate.nextReportingPeriodDateTime = ${nextReportingPeriodDateTime}" }

        return nextReportingPeriodDateTime

    } 
	

    def getActiveStudyActivityList() {
        
        def csa = StudyActivity.createCriteria()        
        def studyActivityList = csa.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //if (debug) { println "=> laborServicegetActiveStudyActivityList: ${studyActivityList}" }        
        
        return studyActivityList
        
    }

    def getActiveStudyTaskList() {
        
        def cst = StudyTask.createCriteria()        
        def studyTaskList = cst.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //if (debug) { laborServicegetActiveStudyTaskList: ${getActiveStudyTaskList}" }        
                        
        return studyTaskList
        
    }
    
	
	def getReportingPeriodData(Reports reportsInstance, ReportingPeriod reportingPeriodInstance) {
        
		if (debug) { println "=> laborService.getReportingPeriodData.reportingPeriodInstance = ${reportingPeriodInstance}" }

		def dataset = null
        
		if (reportingPeriodInstance) {
			
			if (debug) { println "=> laborService.getReportingPeriodData.if(reportingPeriodInstance) = TRUE" }
			
			dataset = []
			
			def cre = ReportedEffort.createCriteria()
			
			def sumOfReportedEffortByEtdlrTask = cre.list{
	            eq("assignedEffort", assignedEffortInstance)
	            projections {
	                sum("percentEffort")
	            }
			}
			
			
			// per row
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
				
				if (debug) { println "=> laborService.getReportingPeriodData.adding row ${it}: ${row}" }

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
		
        if (debug) { println "=> laborService.getCombineReportedEffortConverted.sumConverted: ${sumConverted}" }
		if (debug) { println "=> laborService.getCombineReportedEffortConverted.entryConverted: ${entryConverted}" }
		
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
        if (debug) { println "laborService.sendEmailNotification.loginReportingStaffInstance = ${loginReportingStaffInstance}" }
		
		if ( loginReportingStaffInstance ) {
	        if (debug) { println "=> laborService.sendEmailNotification.loginReportingStaffInstance.username = ${loginReportingStaffInstance.username}" }
	        if (debug) { println "=> laborService.sendEmailNotification.loginReportingStaffInstance.email = ${loginReportingStaffInstance.email}" }
		}

        // REPORTING STAFF
        def reportingStaffInstance = ReportingStaff.read(staffId)
        if (debug) { println "=> laborService.sendEmailNotification.reportingStaffInstance = ${reportingStaffInstance}" }
		
		if ( reportingStaffInstance ) {
			if (debug) { "=> laborService.sendEmailNotification.reportingStaffInstance.email = ${reportingStaffInstance.email}" }
		}

        //REPORTING PERIOD  
		def reportingPeriodInstance = ReportingPeriod.read(periodId)
		if (debug) { println "=> laborService.sendEmailNotification.reportingPeriodInstance.id: ${reportingPeriodInstance.id}" }
		if (debug) { println "=> laborService.sendEmailNotification.reportingPeriodInstance: ${reportingPeriodInstance}" }
		
		//REPORTING DUE DATE
		def reportingDueDateString
		
		if ( reportingPeriodInstance ) {
			
			def reporingDueDate = new LocalDate(reportingPeriodInstance.periodDate)
			if (debug) { println "=> laborService.sendEmailNotification.reporingDueDate: ${reporingDueDate}" }
			
			reporingDueDate = reporingDueDate.plusMonths(1).plusDays(19)
			if (debug) { println "=> laborService.sendEmailNotification.reporingDueDate: ${reporingDueDate}" }
			
			def reportingDueDateMonthName = reporingDueDate.toString('MMMM')
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDateMonthName = ${reportingDueDateMonthName}" }
	
			def reportingDueDateDay = reporingDueDate.toString('dd')
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDateDay = ${reportingDueDateDay}" }

			def reportingDueDateYear = reporingDueDate.toString('yyyy')
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDateYear = ${reportingDueDateYear}" }
	
			reportingDueDateString = reportingDueDateMonthName + ' ' + reportingDueDateDay + ' ' + reportingDueDateYear
			
		}				
		if (debug) { println "=> laborService.sendEmailNotification.reportingDueDateString = ${reportingDueDateString}" }

		// ASSIGNED EFFORT 
		def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
		if (debug) { println "=> laborService.sendEmailNotification.assignedEffortInstance: ${assignedEffortInstance}" }
		
		if ( assignedEffortInstance ) {
			if (debug) { println "=> laborService.sendEmailNotification.assignedEffortInstance.id: ${assignedEffortInstance.id}" }
		} 
		
		// send & record NOTIFICATION EMAIL
		if ( reportingStaffInstance && loginReportingStaffInstance && reportingPeriodInstance) {

			// subject title (initial or reminder)
			def notificationEmailInstanceList
			def emailSubjectTitle
			
			notificationEmailInstanceList = assignedEffortInstance.emails
			if (debug) { println "=> laborService.sendEmailNotification.notificationEmailInstanceList: ${notificationEmailInstanceList}" }
	
			if ( notificationEmailInstanceList ) {
				emailSubjectTitle = "Reminder - NCS Direct Labor Report due ${reportingDueDateString}"
			} else {
				emailSubjectTitle = "Notification - NCS Direct Labor Report due ${reportingDueDateString}"
			}
			if (debug) { println "=> laborService.sendEmailNotification.emailSubjectTitle: ${emailSubjectTitle}" }

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
			if (debug) { println "=> laborService.sendEmailNotification.call mailService.sendMail" }
			
			// record NOTIFICATION EMAIL into db
			def notificationEmailInstance = new NotificationEmail(
				assignedEffort:assignedEffortInstance,
				userSent:loginReportingStaffInstance.username
			)
			if ( notificationEmailInstance.save(flush:true) ) {
				if (debug) {
					println "=> laborService.sendEmailNotification.notificationEmailInstance.save SUCCESSFULLY"
					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.id:${notificationEmailInstance.id} "
					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.assignedEffort:${notificationEmailInstance.assignedEffort} "
					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.dateSent:${notificationEmailInstance.dateSent} "
					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.userSent:${notificationEmailInstance.userSent} "
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
		if (debug) { println "=> laborService.generateReportEmail.reportingPeriodInstance.id: ${reportingPeriodInstance.id}" } 
		if (debug) { println "=> laborService.generateReportEmail.reportingPeriodInstance: ${reportingPeriodInstance}" }
		
		def reportingPeriodMonthName = reportingPeriodInstance.month.toString('MMMM')
		if (debug) { println "=> laborService.generateReportEmail.reportingDueDateMonthName = ${reportingDueDateMonthName}" }
		
		def reportingPeriodYear = reportingDueDate.year.toString('yyyy')		
		if (debug) { println "=> laborService.generateReportEmail.reportingPeriodYear = ${reportingPeriodYear}" }
		
		def reportingPeriodString = g.formatDate(date:reportingPeriodInstance.periodDate, format:'MMMM yyyy')
		//periodSelectiontList.add([id:it.id, name: g.formatDate(date:it.periodDate, format:'MMMM yyyy')])
		if (debug) { println "=> laborService.generateReportEmail.reportingPeriodString = ${reportingPeriodString}" }
		
		// email SUBJECT
		def emailSubjectTitle = "NCS DLR: generate reports for reporting period ${reportingPeriodString}"
		if (debug) { println "=> laborService.generateReportEmail.emailSubjectTitle = ${emailSubjectTitle}" }		
		
		// email TO list
		//def toEmailList = "sqv@cccs.umn.edu, gdw@cccs.umn.edu, jaf@cccs.umn.edu, will1945@umn.edu, bsteward@umn.edu"
		def toEmailList = "sqv@cccs.umn.edu, sqv@umn.edu" 
		if (debug) { println "=> laborService.generateReportEmail.toEmailList = ${toEmailList}" }
		
		// email FROM list
		def fromEmailList = "sqv@cccs.umn.edu"
		if (debug) { println "=> laborService.generateReportEmail.fromEmailList = ${fromEmailList}" }
		
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
