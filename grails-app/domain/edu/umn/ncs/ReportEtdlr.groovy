package edu.umn.ncs
import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/**
This class represents the "Electronic Technical Direct Labor Report" (ETDLR). 
*/
class ReportEtdlr {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true
	
	/** The Id of the reporting period the report is for.  */
	Integer period_id
	/** Full name of the staff reporting effort.  */
	String staffFullName
	/** Labor category of the staff reporting effort  */
	String staffLaborCategory
	/** Code of the ETDLR task, translated from the standard task which used for the "Sponsored Financial Reporting" (SFR) report.  */
	String taskEtdlr
	/** Staff's reported percent effort for the particular ETDLR task.  */
	BigDecimal percentEffort
	/** This is an arbitary date when the ETDLR was completed. It is the date when the last effort, for the given period, was committed. */
	Date dateCreated = new Date()
	/** This is an arbitary ser account that completed ETDLR.  It is the last person to commit his/her reported effort for the given period. */
	String userCreated
	/** This is the applicaiton, used to record the date the ETDLR was completed. */
	String appCreated = 'ncs-dlr'
	
	/** Non-default constraints for this class 
	<dl>
		<dt>staffFullName</dt>
			<dd>cannot be blank</dd>
		<dt>staffLaborCategory</dt>
			<dd>cannot be blank</dd>
		<dt>taskEtdlr(</dt>
			<dd>cannot be blank</dd>
		<dt>percentEffort</dt>
			<dd>cannot be blank</dd>
		<dt>userCreated</dt>
			<dd>cannot be blank</dd>
		<dt>appCreated</dt>
			<dd>cannot be blank</dd>
	</dl>	
	*/
	static constraints = {
		period_id()
		staffFullName(blank:false)
		staffLaborCategory(blank:false)
		taskEtdlr(blank:false)
		percentEffort(blank:false)
		dateCreated()
		userCreated(blank:false)
		appCreated(blank:false)
	}
	
	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->
		
		def now = new Date()
		
		String oldValue = "ETDLR import file"
			oldValue += ", staffFullName: ${oldMap.staffFullName}"
			oldValue += ", staffLaborCategory: ${oldMap.staffLaborCategory}"
			oldValue += ", taskEtdlr: ${oldMap.taskEtdlr}"
			oldValue += ", percentEffort: ${oldMap.percentEffort}"
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

	} 
	
}
