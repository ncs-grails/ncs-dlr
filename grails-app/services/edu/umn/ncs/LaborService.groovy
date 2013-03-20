package edu.umn.ncs
import org.joda.time.*
import org.codehaus.groovy.grails.commons.*


class LaborService {

	static transactional = true

	def springSecurityService
	def mailService

	def debug = true

	def getReportingStaff(principal) {

		log.debug ""
		log.debug "=> Begin LABOR SERVICE > GET-REPORTING-STAFF" 	

		def uname = principal.getUsername()          
		//def uname = '<username>'          
		log.debug "==> uname (based on principal.getUsername()): ${uname}"	

		def reportingStaff = ReportingStaff.findByUsername(uname)
		log.debug "==> reportingStaff (based on ReportingStaff.findByUsername(uname): ${reportingStaff}"	
	
		if ( !reportingStaff ) {

			if (debug) { "==> if (!reportingStaff) = TRUE" }
			
			// get info about logged user from cookie (see edu.umn.auth.UmnCookiesUserDetails)
			reportingStaff = new ReportingStaff(username:uname)
			reportingStaff.email = principal.getEmail()
			reportingStaff.fullName = principal.getFullName()
			reportingStaff.userCreated = uname
			if (debug) { log.debug "==> reportingStaff (based on new ReportingStaff(username:uname)): ${reportingStaff}" }
			if (debug) { log.debug "==> reportingStaff.email (based on principal.getEmail()): ${reportingStaff.email}" }
			if (debug) { log.debug "==> reportingStaff.fullName (based on principal.getFullName()): ${reportingStaff.fullName}" }
			if (debug) { log.debug "==> reportingStaff.userCreated (based on uname): ${reportingStaff.userCreated}" }
				
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
			if (debug) { log.debug "==> nameParts (based on reportingStaff.fullName.tokenize())= ${nameParts.size()}" }
			if (debug) { log.debug "==> nameParts[0] = ${nameParts[0]}" }
			if (debug) { log.debug "==> nameParts[1] = ${nameParts[1]}" }
			if (debug) { log.debug "==> nameParts[2] = ${nameParts[2]}" }

			reportingStaff.save(flush:true)

		} 

		log.debug "=> End LABOR SERVICE > GET-REPORTING-STAFF"
		log.debug ""
		return reportingStaff

	} 
	

    def getCurrentReportingPeriod() {

	log.debug ""
	log.debug "=> Begin LABOR SERVICE > GET-CURRENT-REPORTING-PERIOD" 	

	// today's date
        def today = new LocalDate()
        if (debug) { log.debug "==> today = ${today}" }

        def midnight = new LocalTime(0, 0)
        if (debug) { log.debug "==> midnight = ${midnight}" }

        // REPORTING PERIOD 
        def lastMonth = today.minusMonths(1)
        if (debug) { log.debug "==> lastMonth = ${lastMonth}" }
		
        def lastMonthPeriodDate = lastMonth.minusDays(lastMonth.dayOfMonth - 1)
        if (debug) { log.debug "==> lastMonthPeriodDate = ${lastMonthPeriodDate}" }

        def lastMonthPeriodDateTime = lastMonthPeriodDate.toDateTime(midnight).toCalendar().getTime()
        if (debug) { log.debug "==> lastMonthPeriodDateTime = ${lastMonthPeriodDateTime}" }

        def reportingPeriodInstance = ReportingPeriod.findByPeriodDate(lastMonthPeriodDateTime)
        if (debug) { log.debug "==> reportingPeriodInstance = ${reportingPeriodInstance}" }

        //if current reporting period does not exist in db, insert a new one in there
        if ( !reportingPeriodInstance ) {
		if (debug) { log.debug "==> if(!reportingPeriodInstance) = TRUE" }			
		reportingPeriodInstance = new ReportingPeriod(periodDate:lastMonthPeriodDateTime)
		reportingPeriodInstance.save(flush:true)
		if (debug) { log.debug "==> reportingPeriodInstance = ${reportingPeriodInstance}" }
        }

	log.debug "=> End LABOR SERVICE > GET-CURRENT-REPORTING-PERIOD" 	
	log.debug ""

        return reportingPeriodInstance

    } 


    def getNextReportingPeriodDateTime(paramReportingPeriodDate) {

	log.debug ""
	log.debug "=> Begin LABOR SERVICE > GET-NEXT-REPORTING-PERIOD" 	

        def theReportingPeriodDate = new LocalDate(paramReportingPeriodDate)
        if (debug) { log.debug "==> theReportingPeriodDate = ${theReportingPeriodDate}" }

        def midnight = new LocalTime(0, 0)
	if (debug) { log.debug "==> midnight = ${midnight}" }
		
        def nextReportingPeriodDate = theReportingPeriodDate.plusMonths(1)
        if (debug) { log.debug "==> nextReportingPeriodDate = ${nextReportingPeriodDate}" }

        def nextReportingPeriodDateTime = nextReportingPeriodDate.toDateTime(midnight).toCalendar().getTime()
        if (debug) { log.debug "==> nextReportingPeriodDateTime = ${nextReportingPeriodDateTime}" }

	log.debug "=> End LABOR SERVICE > GET-NEXT-REPORTING-PERIOD" 	
	log.debug ""

        return nextReportingPeriodDateTime

    } 
	

    def getActiveStudyActivityList() {
        
        def csa = StudyActivity.createCriteria()        
        def studyActivityList = csa.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //if (debug) { log.debug "==> getActiveStudyActivityList: ${studyActivityList}" }        
        
        return studyActivityList
        
    }

    def getActiveStudyTaskList() {
        
        def cst = StudyTask.createCriteria()        
        def studyTaskList = cst.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //if (debug) { getActiveStudyTaskList: ${getActiveStudyTaskList}" }        
                        
        return studyTaskList
        
    }    
	
	def getReportingPeriodDataForCsv(ReportType reportTypeInstance, ReportingPeriod reportingPeriodInstance) {
        
		log.debug ""
		log.debug "=> Begin LABOR SERVICE > GET-REPORTING-PERIOD-DATA-FOR-CSV" 	
		log.debug "==> params ReportType reportTypeInstance: ${reportTypeInstance}" 	
		log.debug "==> params ReportingPeriod reportingPeriodInstance: ${reportingPeriodInstance}" 	

		def hql = null
		def resultSet
		def dataset = null
		
		if (reportTypeInstance && reportingPeriodInstance) {
			
			if (debug) { log.debug "==> if (reportTypeInstance && reportingPeriodInstance) = TRUE" }
			
			dataset = []			
			
			// SFR
			if (reportTypeInstance.abbreviation == 'SFR') {
				
				if (debug) { log.debug "==> if (reportTypeInstance.abbreviation == 'SFR')" }
												
				def projectInfoInstance = ProjectInfo.findByPrincipalInvestigatorIsNotNull()				
				def contractNumber = projectInfoInstance.contractNumber
				def contractPeriod = projectInfoInstance.contractPeriod
				DateTime datePrepared = new DateTime(reportingPeriodInstance.preparedDate)
				def rin = reportingPeriodInstance.referenceInvoiceNumber
				def principalInvestigator = projectInfoInstance.principalInvestigator
				DateTime reportingPeriodDate = new DateTime(reportingPeriodInstance.periodDate)
								
				if (debug) { 
					log.debug "==> projectInfoInstance: ${projectInfoInstance}"
					log.debug "==> contractNumber: ${contractNumber}"
					log.debug "==> contractPeriod: ${contractPeriod}"
					log.debug "==> datePrepared: ${datePrepared}"					
					log.debug "==> rin: ${rin}"										
					log.debug "==> principalInvestigator: ${principalInvestigator}"  
					log.debug "==> reportingPeriodDate: ${reportingPeriodDate}" 
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
				//if (debug) { log.debug "==> resultSet: ${resultSet}" }
	
				resultSet.each{ rowOfData ->
					
					//if (debug) { log.debug "==> rowOfData: ${rowOfData}" }

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
					if (debug) { log.debug "==> row; ${row}" }
					
					dataset.add(row)
					
				}

			// ETDLR
			} else if (reportTypeInstance.abbreviation == 'ETDLR')  {
			
				if (debug) { log.debug "==> reportTypeInstance.abbreviation == 'ETDLR'" }
			
				hql = """SELECT TRIM(CONCAT(s.lastName , ', ', s.firstName, ' ', s.middleInit)), 
					lc.name as laborCategory,
					te.name as taskEtdlr, 							
					sum(re.percentEffort) as percentEffort
				FROM AssignedEffort ae inner join
					ae.reportingStaff s INNER JOIN
					ae.laborCategory lc INNER JOIN
					ae.reportedEffort re INNER JOIN
					re.activity a INNER JOIN
					re.task t INNER JOIN
					t.taskEtdlr te 		
				WHERE (ae.period.id = ?) AND (a.id IN (54, 61))
				GROUP BY s.lastName, s.firstName, s.middleInit, lc.name, te.name 
				ORDER BY s.lastName, s.firstName, s.middleInit, sum(re.percentEffort) desc, te.name"""
				
				resultSet = AssignedEffort.executeQuery(hql, [reportingPeriodInstance.id] );
				//if (debug) { log.debug "==> resultSet: ${resultSet}" }
				
				resultSet.each{ rowOfData ->
					
					if (debug) { log.debug "==> rowOfData: ${rowOfData}" }
					
					def row = [:]
					
					row["Staff Name"] = rowOfData[0]
					row["Labor Category"] = rowOfData[1]
					row["Task Etdlr"] = rowOfData[2]
					row["blank1"] = ''
					row["blank2"] = ''
					row["PercentEffort"] = rowOfData[3]
					//if (debug) { log.debug "==> row; ${row}" }
					
					dataset.add(row)
					
				}

			// ODE
			} else if (reportTypeInstance.abbreviation == 'ODE') {
			
				if (debug) { log.debug "==> (reportTypeInstance.abbreviation == 'ODE')" }

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
				WHERE (ae.period.id = ?) AND (a.id IN (54, 61))
				GROUP BY s.lastName, s.firstName, s.middleInit, lc.name, to.name  
				ORDER BY s.lastName, s.firstName, s.middleInit, sum(re.percentEffort) desc, to.name"""
				if (debug) { log.debug "==> hql: $hql" }
				
				resultSet = AssignedEffort.executeQuery(hql, [reportingPeriodInstance.id] );
				if (debug) { log.debug "==> resultSet: ${resultSet}" }

				resultSet.each{ rowOfData ->
					
					if (debug) { log.debug "==> rowOfData: ${rowOfData}" }
					
					def row = [:]
					
					row["Staff Name"] = rowOfData[0]
					row["Labor Category"] = rowOfData[1]
					row["Task ODE"] = rowOfData[2]
					row["Percent Effort for Month"] = rowOfData[3]
					row["Hours for Month"] = rowOfData[4]
					row["Weekly Hours"] = rowOfData[5]
					
					//if (debug) { log.debug "==> row; ${row}" }
					
					dataset.add(row)
					
				}
				
			} //if (reportTypeInstance.abbreviation == 'SFR')
			
		} //if (reportTypeInstance && reportingPeriodInstance)
        
		log.debug "=> End LABOR SERVICE > GET-REPORTING-PERIOD-DATA-FOR-CSV" 	
		log.debug ""

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
		
        if (debug) { log.debug "==> sumConverted: ${sumConverted}" }
		if (debug) { log.debug "==> entryConverted: ${entryConverted}" }
		
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

		if (debug) { log.debug "==>periodId = ${periodId}" }
		if (debug) { log.debug "==>staffId = ${staffId}" }

		// LOG-IN USER (SENDER)
		def principal = springSecurityService.principal
		def username = principal.getUsername()
		def loginReportingStaffInstance = ReportingStaff.findByUsername(username)
		if (debug) { log.debug "==>loginReportingStaffInstance = ${loginReportingStaffInstance}" }

		if ( loginReportingStaffInstance ) {
			if (debug) { log.debug "==> loginReportingStaffInstance.username = ${loginReportingStaffInstance.username}" }
			if (debug) { log.debug "==> loginReportingStaffInstance.email = ${loginReportingStaffInstance.email}" }
		}

		// REPORTING STAFF
		def reportingStaffInstance = ReportingStaff.read(staffId)
			if (debug) { log.debug "==> reportingStaffInstance = ${reportingStaffInstance}" }

		if ( reportingStaffInstance ) {
			if (debug) { "==> reportingStaffInstance.email = ${reportingStaffInstance.email}" }
		}

		//REPORTING PERIOD  
		def reportingPeriodInstance = ReportingPeriod.read(periodId)
		if (debug) { log.debug "==> reportingPeriodInstance.id: ${reportingPeriodInstance.id}" }
		if (debug) { log.debug "==> reportingPeriodInstance: ${reportingPeriodInstance}" }

		//REPORTING DUE DATE
		def reportingPeriodDateString
		def reportingDueDateString

		if ( reportingPeriodInstance ) {

			def reportingDueDate = new LocalDate(reportingPeriodInstance.periodDate)
			if (debug) { log.debug "==> reportingDueDate: ${reportingDueDate}" }
			
			reportingPeriodDateString = reportingDueDate.toString('MMMM') + ' ' + reportingDueDate.toString('yyyy')
			if (debug) { log.debug "==> reportingPeriodDateString: ${reportingPeriodDateString}" }
			
			reportingDueDate = reportingDueDate.plusMonths(1).plusDays(19)
			if (debug) { log.debug "==> reportingDueDate: ${reportingDueDate}" }

			def reportingDueDateMonthName = reportingDueDate.toString('MMMM')
			if (debug) { log.debug "==> reportingDueDateMonthName = ${reportingDueDateMonthName}" }

			def reportingDueDateDay = reportingDueDate.toString('dd')
			if (debug) { log.debug "==> reportingDueDateDay = ${reportingDueDateDay}" }

			def reportingDueDateYear = reportingDueDate.toString('yyyy')
			if (debug) { log.debug "==> reportingDueDateYear = ${reportingDueDateYear}" }

			reportingDueDateString = reportingDueDateMonthName + ' ' + reportingDueDateDay + ', ' + reportingDueDateYear

		}				
		if (debug) { log.debug "==> reportingDueDateString = ${reportingDueDateString}" }

		// ASSIGNED EFFORT 
		def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
		if (debug) { log.debug "==> assignedEffortInstance: ${assignedEffortInstance}" }

		if ( assignedEffortInstance ) {
			if (debug) { log.debug "==> assignedEffortInstance.id: ${assignedEffortInstance.id}" }
		} 

		// send & record NOTIFICATION EMAIL
		if ( reportingStaffInstance && loginReportingStaffInstance && reportingPeriodInstance) {

			// subject title (initial or reminder)
			def notificationEmailInstanceList
			def emailSubjectTitle

			notificationEmailInstanceList = assignedEffortInstance.emails
			if (debug) { log.debug "==> notificationEmailInstanceList: ${notificationEmailInstanceList}" }

			//emailSubjectTitle = "**REVISED/NEW** INSTRUCTIONS FOR NCS DIRECT LABOR REPORTING FOR NEW/JOFOC CONTRACT EFFECTIVE 9/28/12"
			// specific subject line for December 2011 effort reporting
			//IMPORTANT: READ REVISED INSTRUCTIONS ABOUT NCS DIRECT LABOR REPORT FOR ${reportingPeriodDateString} (due ${reportingDueDateString})" 	
			if ( notificationEmailInstanceList ) {
				emailSubjectTitle = "Reminder - NCS Direct Labor Report due ${reportingDueDateString}"
			} else {
				emailSubjectTitle = "Notification - NCS Direct Labor Report due ${reportingDueDateString}"
			}	
			if (debug) { log.debug "==> emailSubjectTitle: ${emailSubjectTitle}" }

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
			if (debug) { log.debug "==> call mailService.sendMail" }

			// record NOTIFICATION EMAIL into db
			def notificationEmailInstance = new NotificationEmail(assignedEffort:assignedEffortInstance, userSent:loginReportingStaffInstance.username)
			if ( notificationEmailInstance.save(flush:true) ) {
				if (debug) {

					log.debug "==> newly created notificationEmailInstance.id:${notificationEmailInstance.id} "
					log.debug "==> newly created notificationEmailInstance.assignedEffort:${notificationEmailInstance.assignedEffort} "
					log.debug "==> newly created notificationEmailInstance.dateSent:${notificationEmailInstance.dateSent} "
					log.debug "==> newly created notificationEmailInstance.userSent:${notificationEmailInstance.userSent} "
				} else {
					if (debug) { log.debug "==> notificationEmailInstance.save FAILED" }
					notificationEmailInstance.errors.each{
						if (debug) { log.debug it }
					}
				}
			}	
			return 'sent email!'

		} //if ( reportingStaffInstance && loginReportingStaffInstance && reportingPeriodInstance)        

	} 

	
	def sendReportEmail (AssignedEffort assignedEffortInstance) {

		// REPORTING PERIOD
		if (debug) {
			log.debug "==> assignedEffortInstance.id: ${assignedEffortInstance.id}"
			log.debug "==> assignedEffortInstance: ${assignedEffortInstance}"
		}

		def reportingPeriodInstance = assignedEffortInstance.period		
		if (debug) { 
			log.debug "==> reportingPeriodInstance.id: ${reportingPeriodInstance.id}"
			log.debug "==> reportingPeriodInstance: ${reportingPeriodInstance}"
		} 
		
		Date periodDate = new Date(reportingPeriodInstance.periodDate.time)
		def reportingPeriodString = periodDate.format('MMMM yyyy') 
		if (debug) { log.debug "==> reportingPeriodString = ${reportingPeriodString}" }
		
		// email SUBJECT
		def emailSubjectTitle = "NCS DLR: generate reports for reporting period ${reportingPeriodString}"
		if (debug) { log.debug "==> emailSubjectTitle = ${emailSubjectTitle}" }		
		
		// email TO list
		def toEmailList = "sqv@cccs.umn.edu, jaf@cccs.umn.edu, bsteward@umn.edu, dmd@cccs.umn.edu"
		//def toEmailList = "sqv@cccs.umn.edu, sqv@umn.edu" 
		//if (debug) { log.debug "==> toEmailList = ${toEmailList}" }
		
		// email FROM list
		def fromEmailList = "sqv@cccs.umn.edu"
		//if (debug) { log.debug "==> fromEmailList = ${fromEmailList}" }
		
		// send email
		mailService.sendMail {
			to toEmailList
			from fromEmailList
			subject emailSubjectTitle
			body(
				view:"/assignEffort/generateReportEmail",
				model:[reportingPeriodInstance: reportingPeriodInstance]
			)
		} 
		if (debug) { log.debug "==> called mailService.sendMail" }
		
		return 'sent Generate Report email!'
				
	} 

				 
} 
