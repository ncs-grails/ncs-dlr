package edu.umn.ncs
import org.joda.time.*

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ApplicationManagementController {

    def authenticateService
    def laborService

    // display APPLICATION MANAGEMENT page
    def index = {     
        println "PRINTLN ApplicationManagementController.index.params: ${params}"                
    }
    

}

