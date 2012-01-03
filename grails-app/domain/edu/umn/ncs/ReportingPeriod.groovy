package edu.umn.ncs
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ReportingPeriod {

	static auditable = true
	
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

	String toString() { "${month}/${year}" }
    
	static constraints = {
		referenceInvoiceNumber(nullable:true)
		periodDate()
		preparedDate(nullable:true)
		completedReportDate(nullable:true)
	}

	def onDelete = { oldMap ->
		
		def now = new Date()
		
		String oldValue = "ReportingPeriod"
			oldValue += ", referenceInvoiceNumber: ${oldMap.referenceInvoiceNumber}"
			oldValue += ", periodDate: ${oldMap.periodDate}"
			oldValue += ", preparedDate: ${oldMap.preparedDate}"
			oldValue += ", completedReportDate: ${oldMap.completedReportDate} "
		//println "PRINTLN ReportingPeriodDomain.onDelete.oldValue: ${oldValue}"
			
		String className = this.class.toString().replace('class ', '')
		//println "${now}\tAudit:DELETE::\t${oldValue}"

		def auditLogEventInstance = new AuditLogEvent(
			className: className,
			dateCreated: now,
			eventName: 'DELETE',
			lastUpdated: now,
			oldValue: oldValue,
			persistedObjectId: this.id,
			persistedObjectVersion: this.version
		)
		if ( ! auditLogEventInstance.save() ) {
			auditLogEventInstance.errors.each{
				println "${now}\tError Transacting DELETE:: \t ${it}"
			}
		}

	}
	
}
