import edu.umn.ncs.*

class BootStrap {

    def init = { servletContext ->

        
        // Reporting Staff *****************************************************
        
        def reportingStaffList = [
            [
                username:'ngp', 
                lastName:'PortNov', 
                firstName:'Natalya', 
                middleInit:'G', 
                fullName:'Natalya G Portnov', 
                laborCategory:LaborCategory.read(4),
                email:'ngp@cccs.umn.edu'
            ], 
            [
                username:'ajz', 
                lastName:'Zirbes', 
                firstName:'Aaron', 
                middleInit:'J', 
                fullName:'Aaron J Zirbes', 
                laborCategory:LaborCategory.read(11),
                email:'ajz@cccs.umn.edu'
            ]
        ]
        
        reportingStaffList.each{

            def reportingStaffInstance = ReportingStaff.findByFullName(it.fullName)

            if ( ! reportingStaffInstance ) {

                reportingStaffInstance = new ReportingStaff(
                    username:it.username,
                    lastName:it.lastName,
                    firstName:it.firstName, 
                    middleInit:it.middleInit,
                    fullName:it.fullName,
                    laborCategory:it.laborCategory,
                    email:it.email,                    
                    isTestAccount:false, 
                    reportsEffort:true, 
                    userCreated:'sqv', 
                    appCreated:'ncs-dlr'
                )

                if ( ! reportingStaffInstance.save() ) {
                    println "failed to save ${it}:"
                    reportingStaffInstance.errors.each{ e ->
                        println "\t${e}"
                    }
                }

            } 

        } 


        
        // Reporting Period ****************************************************
        def reportingPeriodList = [
            '12/1/2010',
            '1/1/2011',
            '2/1/2011'
        ]

        reportingPeriodList.each{

            def reportingPeriodInstance = ReportingPeriod.findByPeriodDate(it)

            if ( ! reportingPeriodInstance ) {

                reportingPeriodInstance = new ReportingPeriod(periodDate:it)

                if ( ! reportingPeriodInstance.save() ) {
                    println "failed to save ${it}:"
                    reportingPeriodInstance.errors.each{ e ->
                        println "\t${e}"
                    }
                }

            } //if ( ! reportingPeriodInstance )

        } 

        // LABOR CATEGORY ******************************************************
        def laborCategoryList = [
            'Administrative Assistant',
            'Community Liaison',
            'Consultant',
            'Data Analyst',
            'Data Collection/Call Center Manager',
            'Data Quality Staff',
            'Fieldworker/Data Collector/Call Center Interviewer',
            'Financial Analyst/Administrator',
            'Hospital Negotiator',
            'Investigator',
            'IT Coordinator',
            'Other',
            'Principal Investigator',
            'Project Manager',
            'Research Assistant',
            'Study Coordinator'
        ]

        laborCategoryList.each{

            def laborCategoryInstance = LaborCategory.findByName(it)

            if ( ! laborCategoryInstance ) {

                laborCategoryInstance = new LaborCategory(name:it, obsolete:false, userCreated:'sqv')

                if ( ! laborCategoryInstance.save() ) {
                    println "failed to save ${it}:"
                    laborCategoryInstance.errors.each{ e ->
                        println "\t${e}"
                    }
                }

            } //if ( ! laborCategoryInstance )

        } //laborCategoryList.each

        // STUDY ACTIVITY ******************************************************
        def studyActivityList = [
            'High-Low Recruitment Vanguard',
            'WA 01 (Executive Function Formative)',
            'WA 02 (Dietary Assessment Formative)',
            'WA 03 (Ulnar Measurement Formative)',
            'WA 04 (Stress & Cortisol Formative)',
            'WA 05 (Birth Visit Questionnaires Formative)'
        ]

        studyActivityList.each{

            def studyActivityInstance = StudyActivity.findByName(it)

            if ( ! studyActivityInstance ) {

                studyActivityInstance = new StudyActivity(name:it, obsolete:false, userCreated:'sqv')

                if ( ! studyActivityInstance.save() ) {
                    println "failed to save ${it}:"
                    studyActivityInstance.errors.each{ e ->
                        println "\t${e}"
                    }
                }

            } //if ( ! studyActivityInstance )

        } //studyActivityList.each


        // STUDY TASK **********************************************************
        def studyTaskList = [
            'Administrative Tasks',
            'Community Engagement - Developing Plans',
            'Community Engagement - Other',
            'Community Engagement - Reviewing Plans',
            'Data Collection - 1st Pregnancy Visit',
            'Data Collection - 2nd Pregnancy Visit',
            'Data Collection - Birth Visit',
            'Data Collection - Household Enumeration',
            'Data Collection - Informed Consent',
            'Data Collection - Listing',
            'Data Collection - Pre-Pregnancy Visit',
            'Data Collection - Pregnancy Screening',
            'Data Collection - Telephone Interviewing',
            'Data Management',
            'Field Management',
            'Fieldwork - Data Collection',
            'Fieldwork - EPSC',
            'Fieldwork - Mapping/Listing',
            'Hospital Engagement',
            'Hospital Outreach',
            'Informatics Management',
            'IRB Management',
            'Mailout Preparation',
            'Management and Oversight',
            'Meetings - Other',
            'Meetings with Center Staff',
            'Meetings with Community Members',
            'Meetings with Program Office',
            'Meetings with Subcontractors',
            'NCS Management',
            'Not Applicble',
            'Operations Planning & Development',
            'Other',
            'Phone Center Management',
            'Programming/Data Management',
            'Protocol Development',
            'Provider Outreach',
            'Scientific Activities - Analysis',
            'Scientific Activities - IRB',
            'Scientific Activities - Other',
            'Scientific Activities - Pilot Studies',
            'Site Office Management',
            'Spatial Analysis',
            'Study Coordination',
            'Training',
            'Webcasts',
            'Working Team/Committee Meeting'
        ]

        studyTaskList.each{

            def studyTaskInstance = StudyTask.findByName(it)

            if ( ! studyTaskInstance ) {

                studyTaskInstance = new StudyTask(name:it, obsolete:false, userCreated:'sqv')

                if ( ! studyTaskInstance.save() ) {
                    println "failed to save ${it}:"
                    studyTaskInstance.errors.each{ e ->
                        println "\t${e}"
                    }
                }

            } //if ( ! studyTaskInstance )

        } //studyTaskList.each

        println "started https://sqv.healthstudies.umn.edu:8443/ncs-dlr/"

    } //def init

    def destroy = {
    }

}
