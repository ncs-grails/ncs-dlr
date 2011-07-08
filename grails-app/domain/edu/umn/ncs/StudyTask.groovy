package edu.umn.ncs

class StudyTask {

    String name
    Boolean obsolete = true
    Date dateCreated = new Date()
    String userCreated
    String appCreated = 'ncs-dlr'
    Integer etdlrCode
    Integer odeCode

    String toString() {
        name
    }

    static constraints = {
        name(maxSize:1024)
        obsolete()
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)
        etdlrCode(nullable:true)
        odeCode(nullable:true)
    }
    
    static mapping = { sort "name" }

}
