package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ReportedEffort {

    static auditable = true

    StudyActivity activity
    StudyTask task
    BigDecimal percentEffort
    Date dateCreated = new Date()
    String userCreated
    String appCreated  = 'ncs-dlr'

    def onDelete = { oldMap ->
        
		def now = new Date()		
		def oldItems = oldMap.collect{it}.join(',')
		
        String oldValue = "Deleting Reported Effort associated with assignedEffort.id ${oldMap.assignedEffort.id}"
			oldValue += " for Items: ${oldItems}"
			oldValue += ", activity.id: ${oldMap.activity.id}"
			oldValue += ", task.id: ${oldMap.task.id}"
			oldValue += ", percentEffort: ${oldMap.percentEffort}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated} "
		println "PRINTLN ReportedEffortDomain.onDelete.oldValue: ${oldValue}"
			
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

    static belongsTo = [assignedEffort: AssignedEffort]
    static transients = ['percentEffortConverted']

    BigDecimal getPercentEffortConverted() {
        if (percentEffort != null) {
            return percentEffort * 100.00
        } else {
            percentEffort
        }
    }
    
    void setPercentEffortConverted(BigDecimal percentEffortConverted) {
        if (percentEffortConverted != null) {
            percentEffort = percentEffortConverted / 100.00
        } else {
            percentEffort = null
        }
    }

    void setPercentEffortConverted(String percentEffortConvertedString) {
        try {
            percentEffortConverted = percentEffortConvertedString.toBigDecimal()
        } finally {
            percentEffort = percentEffortConverted / 100.00
        }
    }
    
    static constraints = {
        assignedEffort()
        activity()
        task()
        percentEffort(max:1.0, scale:5)
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)
    }

}