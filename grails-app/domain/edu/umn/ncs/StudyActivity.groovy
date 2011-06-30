package edu.umn.ncs

class StudyActivity {

    String name
    Boolean obsolete = true
    Date dateCreated = new Date()
    String userCreated
    String appCreated= 'ncs-dlr'

    String toString() {
        name
    }

    static constraints = {
        name(blank:false, maxSize:1024)
        obsolete()
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)
    }
    
    static mapping = { sort "name" }


}
