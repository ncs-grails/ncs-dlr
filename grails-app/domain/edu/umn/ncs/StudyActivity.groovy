package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/**
This class represents the funding group staff are assigned to and report on the DLR.
*/
class StudyActivity {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true
	/** Description of Study Activity */
	String name
	/** Indicates whether this specific activity is still made available, in the DLR application, for staff to report. */
    	Boolean obsolete = true
	/** Date activity was added to database */
    	Date dateCreated = new Date()
	/** User account of person that added activity to database */
    	String userCreated
	/** Application, used by person, to add activity to database */
    	String appCreated= 'ncs-dlr'

	/** Sets default string for this domain to activity "name" (description) */	
	String toString() { name }

	/** Non-default constraints for this class 
	<dl>
		<dt>name</dt>
			<dd>cannot be blank, maximum length of 1024 characters</dd>
		<dt>userCreated</dt>
			<dd>cannot be blank</dd>
		<dt>appCreated</dt>
			<dd>cannot be blank</dd>
	</dl>	
	*/
    	static constraints = {
        	name(blank:false, maxSize:1024)
        	obsolete()
        	dateCreated()
        	userCreated(blank:false)
        	appCreated(blank:false)
    	}		

	/** Sets the default sorting, for this domain, by "name," in ascending order. */
    	static mapping = { sort "name" }

	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class */
    	def onDelete = { oldMap ->
        
        	def now = new Date()
		
        	String oldValue = "Study Activity"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated} "
		//println "PRINTLN StudyActivityDomain.onDelete.oldValue: ${oldValue}"
			
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
