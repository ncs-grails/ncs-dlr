package edu.umn.ncs
import java.util.Date;
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/** This class represents tasks eqivalent to those reported in the "Sponsored Financial Reporting" report that is used for the "Electronic Technical Direct Labor Report" (ETDLR) */
class StudyTaskEtdlr {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true

	/** Description of task performed */
	String name
	/** Indicates whether this specific task is still active for reporting. */
    	Boolean obsolete = true
	/** Date task was added to database */
    	Date dateCreated = new Date()
	/** User account of person that added task to database */
    	String userCreated
	/** Application, used by person, to add task to database */
    	String appCreated = 'ncs-dlr'

	/** Non-default constraints for this class 
	<dl>
		<dt>name</dt>
			<dd>maximum length of 1024 characters</dd>
		<dt>dateCreated</dt>
			<dd>cannot be blank</dd>
		<dt>userCreated</dt>
			<dd>cannot be blank</dd>
		<dt>appCreated</dt>
			<dd>cannot be blank</dd>
	</dl>	
	*/
	static constraints = {
		name(maxSize:1024)
		obsolete()
		dateCreated(blank:false)
		userCreated(blank:false)
		appCreated(blank:false)
	}
	
	/** Sets default string for this domain to task "name" (description) */	
	String toString() { name }

	/** Sets the default sorting, for this domain, by "name," in ascending order. */
	static mapping = { sort "name" }

	/** Trigger that saves old task information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->
		
		def now = new Date()
		
        	String oldValue = "Task ETDLR"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated}"
		//println "PRINTLN EtdlrTaskDomain.onDelete.oldValue: ${oldValue}"
			
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
