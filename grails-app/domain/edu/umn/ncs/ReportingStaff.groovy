package edu.umn.ncs

class ReportingStaff {

    String username
    String lastName
    String firstName
    String middleInit
    LaborCategory laborCategory
    String email
    Boolean isTestAccount = false
    Boolean reportsEffort = false
    Date dateCreated = new Date()
    String userCreated
    String appCreated = 'ncs-dlr'

    static hasMany = [assignedEfforts: AssignedEffort]
    static transients = ['fullNameLFM']

	String toString() {
		fullNameLFM
	}
	
    String getFullNameLFM() {
        "${lastName} , ${firstName} ${middleInit ?: ''}".trim()
    }
    
    static constraints = {
        username(blank:false, unique:true)
        firstName(blank:false)
        middleInit(nullable:true)
        lastName(blank:false)
        laborCategory(nullable:true)
        email(blank:false, email:true)
        isTestAccount()
        reportsEffort()
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)
    }
    
}
