package edu.umn.ncs
import java.math.BigDecimal;
import java.util.Date;
import org.joda.time.*
import org.codehaus.groovy.grails.commons.*


class LaborService {

	static transactional = true

	def springSecurityService
	def mailService
	def debug = true
	
    def getReportingStaff(principal) {

		debug = ConfigurationHolder.config.console.debugging
	
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
	
	def getReportingPeriodDataForCsv(ReportType reportTypeInstance, ReportingPeriod reportingPeriodInstance) {
        
		def hql = null
		def resultSet
		def dataset = null
		
		if (reportTypeInstance && reportingPeriodInstance) {
			
			if (debug) { println "=> laborService.getReportingPeriodDataForCsv.if(reportTypeInstance && reportingPeriodInstance) = TRUE" }
			
			dataset = []			
			
			// SFR
			if (reportTypeInstance.abbreviation == 'SFR') {
				
				if (debug) { println "=> laborService.getReportingPeriodData.(reportTypeInstance.abbreviation == 'SFR')" }
												
				def projectInfoInstance = ProjectInfo.findByPrincipalInvestigatorIsNotNull()				
				def contractNumber = projectInfoInstance.contractNumber
				def contractPeriod = projectInfoInstance.contractPeriod
				DateTime datePrepared = new DateTime(reportingPeriodInstance.preparedDate)
				def rin = reportingPeriodInstance.referenceInvoiceNumber
				def principalInvestigator = projectInfoInstance.principalInvestigator
				DateTime reportingPeriodDate = new DateTime(reportingPeriodInstance.periodDate)
								
				if (debug) { 
					println "=> laborService.getReportingPeriodDataForCsv.contractNumber: ${contractNumber}"
					println "=> laborService.getReportingPeriodDataForCsv.contractPeriod: ${contractPeriod}"
					println "=> laborService.getReportingPeriodDataForCsv.datePrepared: ${datePrepared}"					
					println "=> laborService.getReportingPeriodDataForCsv.rin: ${rin}"										
					println "=> laborService.getReportingPeriodDataForCsv.principalInvestigator: ${principalInvestigator}"  
					println "=> laborService.getReportingPeriodDataForCsv.reportingPeriodDate: ${reportingPeriodDate}" 
				}
				
				hql = """SELECT TRIM(CONCAT(s.lastName , ', ', s.firstName, ' ', s.middleInit)),
					lc.name as laborCategory,
					a.name as studyActivity, 
					t.name as studyTask,
					re.percentEffort as percentEffort
				FROM AssignedEffort ae left outer join
					ae.reportingStaff s left outer join
					ae.laborCategory lc left outer join
					ae.reportedEffort re left outer join
					re.activity a left outer join
					re.task t 					 
				WHERE (ae.period.id = ?)
				ORDER BY s.lastName, s.firstName, s.middleInit, re.percentEffort desc, a.name, t.name"""
				
				resultSet = AssignedEffort.executeQuery(hql, [reportingPeriodInstance.id]);
				//if (debug) { println "=> laborService.getReportingPeriodDataForCsv.resultSet: ${resultSet}" }
	
				resultSet.each{ rowOfData ->
					
					//if (debug) { println "=> laborService.getReportingPeriodDataForCsv.rowOfData: ${rowOfData}" }

					def row = [:]
					
					row["Contract Number"] = contractNumber
					row["Reference Invoice Number"] = 'SFR 2706 - ' + rin
					row["Reporting Period"] = reportingPeriodDate.toString("MMMM yyyy")					
					row["Contract Period"] = contractPeriod					
					row["Date Prepared"] = datePrepared.toString("MM/dd/yyyy")
					row["Principal Investigator"] = principalInvestigator
					row["Staff Name"] = rowOfData[0]
					row["Labor Category"] = rowOfData[1]
					row["StudyActivity"] = rowOfData[2]
					row["Study Task"] = rowOfData[3]
					row["PercentEffort"] = rowOfData[4]
					if (debug) { println "=> laborService.getReportingPeriodDataForCsv.row; ${row}" }
					
					dataset.add(row)
					
				}

			// ETDLR
			} else if (reportTypeInstance.abbreviation == 'ETDLR')  {
			
				if (debug) { println "=> laborService.getReportingPeriodData.(reportTypeInstance.abbreviation == 'ETDLR')" }
			
				hql = """SELECT TRIM(CONCAT(s.lastName , ', ', s.firstName, ' ', s.middleInit)), 
					lc.name as laborCategory,
					te.name as taskEtdlr, 							
					sum(re.percentEffort) as percentEffort
				FROM AssignedEffort ae inner join
					ae.reportingStaff s inner join
					ae.laborCategory lc inner join
					ae.reportedEffort re inner join
					re.task t left outer join
					t.taskEtdlr te 		
				WHERE (ae.period.id = ?)
				GROUP BY s.lastName, s.firstName, s.middleInit, lc.name, te.name 
				ORDER BY s.lastName, s.firstName, s.middleInit, sum(re.percentEffort) desc, te.name"""
				
				resultSet = AssignedEffort.executeQuery(hql, [reportingPeriodInstance.id] );
				//if (debug) { println "=> laborService.getReportingPeriodData.resultSet: ${resultSet}" }
				
				resultSet.each{ rowOfData ->
					
					if (debug) { println "=> laborService.getReportingPeriodData.rowOfData: ${rowOfData}" }
					
					def row = [:]
					
					row["Staff Name"] = rowOfData[0]
					row["Labor Category"] = rowOfData[1]
					row["Task Etdlr"] = rowOfData[2]
					row["blank1"] = ''
					row["blank2"] = ''
					row["PercentEffort"] = rowOfData[3]
					//if (debug) { println "=> laborService.getReportingPeriodData.row; ${row}" }
					
					dataset.add(row)
					
				}

			// ODE
			} else if (reportTypeInstance.abbreviation == 'ODE') {
			
				if (debug) { println "=> laborService.getReportingPeriodData.(reportTypeInstance.abbreviation == 'ODE')" }

				hql = """SELECT TRIM(CONCAT(s.lastName , ', ', s.firstName, ' ', s.middleInit)), 
					lc.name as laborCategory,
					to.name as taskOde, 
					sum(re.percentEffort) * 100 as effortForMonth, 
					sum(re.percentEffort) * 160 as hoursForMonth, 
					(sum(re.percentEffort) * 160) / 4 as weeklyHours 					
				FROM AssignedEffort ae INNER JOIN
					ae.reportingStaff s INNER JOIN
					ae.laborCategory lc INNER JOIN
					ae.reportedEffort re INNER JOIN
					re.activity a INNER JOIN
					re.task t INNER JOIN
					t.taskOde to   
				WHERE (ae.period.id = ? AND a.id = 54)
				GROUP BY s.lastName, s.firstName, s.middleInit, lc.name, to.name  
				ORDER BY s.lastName, s.firstName, s.middleInit, sum(re.percentEffort) desc, to.name"""
				
				resultSet = AssignedEffort.executeQuery(hql, [reportingPeriodInstance.id] );
				//if (debug) { println "=> laborService.getReportingPeriodData.resultSet: ${resultSet}" }

				resultSet.each{ rowOfData ->
					
					if (debug) { println "=> laborService.getReportingPeriodData.rowOfData: ${rowOfData}" }
					
					def row = [:]
					
					row["Staff Name"] = rowOfData[0]
					row["Labor Category"] = rowOfData[1]
					row["Task ODE"] = rowOfData[2]
					row["Percent Effort for Month"] = rowOfData[3]
					row["Hours for Month"] = rowOfData[4]
					row["Weekly Hours"] = rowOfData[5]
					
					//if (debug) { println "=> laborService.getReportingPeriodData.row; ${row}" }
					
					dataset.add(row)
					
				}
				
			} //if (reportTypeInstance.abbreviation == 'SFR')
			
		} //if (reportTypeInstance && reportingPeriodInstance)
        
		return dataset
        
	} //def getReportingPeriodData(ReportType reportTypeInstance, ReportingPeriod reportingPeriodInstance) 
    
    
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

		if (debug) { println "=>laborService.sendEmailNotification.periodId = ${periodId}" }
		if (debug) { println "=>laborService.sendEmailNotification.staffId = ${staffId}" }

		// LOG-IN USER (SENDER)
		def principal = springSecurityService.principal
		def username = principal.getUsername()
		def loginReportingStaffInstance = ReportingStaff.findByUsername(username)
		if (debug) { println "=>laborService.sendEmailNotification.loginReportingStaffInstance = ${loginReportingStaffInstance}" }

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
		def reportingPeriodDateString
		def reportingDueDateString

		if ( reportingPeriodInstance ) {

			def reportingDueDate = new LocalDate(reportingPeriodInstance.periodDate)
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDate: ${reportingDueDate}" }
			
			reportingPeriodDateString = reportingDueDate.toString('MMMM') + ' ' + reportingDueDate.toString('yyyy')
			if (debug) { println "=> laborService.sendEmailNotification.reportingPeriodDateString: ${reportingPeriodDateString}" }
			
			reportingDueDate = reportingDueDate.plusMonths(1).plusDays(19)
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDate: ${reportingDueDate}" }

			def reportingDueDateMonthName = reportingDueDate.toString('MMMM')
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDateMonthName = ${reportingDueDateMonthName}" }

			def reportingDueDateDay = reportingDueDate.toString('dd')
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDateDay = ${reportingDueDateDay}" }

			def reportingDueDateYear = reportingDueDate.toString('yyyy')
			if (debug) { println "=> laborService.sendEmailNotification.reportingDueDateYear = ${reportingDueDateYear}" }

			reportingDueDateString = reportingDueDateMonthName + ' ' + reportingDueDateDay + ', ' + reportingDueDateYear

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

			// specific subject line for December 2011 effort reporting
			emailSubjectTitle = "IMPORTANT: READ REVISED INSTRUCTIONS ABOUT NCS DIRECT LABOR REPORT FOR ${reportingPeriodDateString} (due ${reportingDueDateString})" 	
			/*
			if ( notificationEmailInstanceList ) {
				emailSubjectTitle = "Reminder - NCS Direct Labor Report due ${reportingDueDateString}"
			} else {
				emailSubjectTitle = "Notification - NCS Direct Labor Report due ${reportingDueDateString}"
			}	
			*/
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
			def notificationEmailInstance = new NotificationEmail(assignedEffort:assignedEffortInstance, userSent:loginReportingStaffInstance.username)
			if ( notificationEmailInstance.save(flush:true) ) {
				if (debug) {

					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.id:${notificationEmailInstance.id} "
					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.assignedEffort:${notificationEmailInstance.assignedEffort} "
					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.dateSent:${notificationEmailInstance.dateSent} "
					println "=> laborService.sendEmailNotification.newly created notificationEmailInstance.userSent:${notificationEmailInstance.userSent} "
				} else {
					if (debug) { println "=> notificationEmailInstance.save FAILED" }
					notificationEmailInstance.errors.each{
						if (debug) { println it }
					}
				}
			}	
			return 'sent email!'

		} //if ( reportingStaffInstance && loginReportingStaffInstance && reportingPeriodInstance)        

	} 

	
	def sendReportEmail (AssignedEffort assignedEffortInstance) {

		// REPORTING PERIOD
		if (debug) {
			println "=> laborService.generateReportEmail.assignedEffortInstance.id: ${assignedEffortInstance.id}"
			println "=> laborService.generateReportEmail.assignedEffortInstance: ${assignedEffortInstance}"
		}

		def reportingPeriodInstance = assignedEffortInstance.period		
		if (debug) { 
			println "=> laborService.generateReportEmail.reportingPeriodInstance.id: ${reportingPeriodInstance.id}"
			println "=> laborService.generateReportEmail.reportingPeriodInstance: ${reportingPeriodInstance}"
		} 
		
		Date periodDate = new Date(reportingPeriodInstance.periodDate.time)
		def reportingPeriodString = periodDate.format('MMMM yyyy') 
		if (debug) { println "=> laborService.generateReportEmail.reportingPeriodString = ${reportingPeriodString}" }
		
		// email SUBJECT
		def emailSubjectTitle = "NCS DLR: generate reports for reporting period ${reportingPeriodString}"
		if (debug) { println "=> laborService.generateReportEmail.emailSubjectTitle = ${emailSubjectTitle}" }		
		
		// email TO list
		//def toEmailList = "sqv@cccs.umn.edu, gdw@cccs.umn.edu, jaf@cccs.umn.edu, will1945@umn.edu, bsteward@umn.edu"
		//def toEmailList = "sqv@cccs.umn.edu, sqv@umn.edu" 
		//if (debug) { println "=> laborService.generateReportEmail.toEmailList = ${toEmailList}" }
		
		// email FROM list
		def fromEmailList = "sqv@cccs.umn.edu"
		//if (debug) { println "=> laborService.generateReportEmail.fromEmailList = ${fromEmailList}" }
		
		// send email
		mailService.sendMail {
			to "sqv@cccs.umn.edu", "gdw@cccs.umn.edu", "jaf@cccs.umn.edu", "will1945@umn.edu, aarellan@cccs.umn.edu"
			from fromEmailList
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
