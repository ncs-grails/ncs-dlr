package edu.umn.ncs
import java.util.Date;
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ReportSfr {

	static auditable = true

	// SELECT id, EffortID,
	// ContractNumber, ReferenceInvoiceNumber, ContractPeriodTitle, ContractPeriodDesc, PrincipalInvestigator,
	// StaffName, LaborCatDesc, ActivityDesc, TaskDesc, PercentEffort,
	// CreatedDate, CreatedByWhom, CreatedByWhat, DatePrepared
	// FROM WebLookup.dbo.ncs_dlr_report
	
	Integer effortId
	String contractNumber
	String referenceInvoiceNumber
	String contractPeriodTitle
	String contractPeriodDesc
	String principalInvestigator
	String staffName
	String laborCategoryName
	String studyActivityName
	String studyTaskName
	BigDecimal percentEffort
	Date datePrepared
	Date dateCreated = new Date()
	String userCreated
	String appCreated = 'ncs-dlr'

	static constraints = {
		effortId(nullable:true)
		contractNumber(blank:false)
		referenceInvoiceNumber(blank:false)
		contractPeriodTitle(blank:false)
		contractPeriodDesc(blank:false)
		principalInvestigator(blank:false)
		staffName(blank:false)
		laborCategoryName(blank:false)
		studyActivityName(blank:false)
		studyTaskName(blank:false)
		percentEffort(blank:false)
		datePrepared(blank:false)
		dateCreated(blank:false)
		userCreated(blank:false)
		appCreated(blank:false)
	}
		
	def onDelete = { oldMap ->
		
		def now = new Date()
		
		String oldValue = "SFR Report"
			oldValue += ", effortId: ${oldMap.effortId}"
			oldValue += ", contractNumber: ${oldMap.contractNumber}"
			oldValue += ", referenceInvoiceNumber: ${oldMap.referenceInvoiceNumber}"
			oldValue += ", contractPeriodTitle: ${oldMap.contractPeriodTitle}"
			oldValue += ", contractPeriodDesc: ${oldMap.contractPeriodDesc}"
			oldValue += ", principalInvestigator: ${oldMap.principalInvestigator}"
			oldValue += ", staffName: ${oldMap.staffName}"
			oldValue += ", laborCategoryName: ${oldMap.laborCategoryName}"
			oldValue += ", studyActivityName: ${oldMap.studyActivityName}"
			oldValue += ", studyTaskName: ${oldMap.studyTaskName}"
			oldValue += ", percentEffort: ${oldMap.percentEffort}"
			oldValue += ", datePrepared: ${oldMap.datePrepared}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated}"
		//println "PRINTLN SfrReportDomain.onDelete.oldValue: ${oldValue}"
			
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

	} //def onDelete

}
