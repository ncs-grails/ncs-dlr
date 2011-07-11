package edu.umn.ncs
import org.joda.time.*

class ReportingPeriod {

    Integer referenceInvoiceNumber
    Date periodDate
    Date preparedDate
    Date completedReportDate

	def onDelete = { oldMap ->
		
		def now = new Date()

		// describe items to delete
		String oldValue = "Deleting ReportingPeriod, reportingPeriod.id, referenceInvoiceNumber, periodDate, preparedDate, completedReportDate "

		String className = this.class.toString().replace('class ', '')
		//println "${now}\tAudit:DELETE::\t${oldValue}"

		// transaction auditing
		def auditLogEventInstance = new AuditLogEvent(
			className: className,
			dateCreated: now,
			eventName: 'DELETE',
			lastUpdated: now,
			oldValue: oldValue,
			persistedObjectId: this.id,
			persistedObjectVersion: this.version)
			if ( ! auditLogEventInstance.save() ) {
				auditLogEventInstance.errors.each{
					println "${now}\tError Transacting DELETE:: \t ${it}"
				}
			}

	} //def onDelete
	
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
