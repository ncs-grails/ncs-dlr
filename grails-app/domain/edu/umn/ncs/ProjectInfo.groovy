package edu.umn.ncs

class ProjectInfo {

    String principalInvestigator
    String contractNumber
    String sfr
    Date startDate

    static constraints = {

        principalInvestigator(blank:false)
        contractNumber(blank:false)
        sfr(blank:false)
        startDate()

    }

}
