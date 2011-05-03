import edu.umn.ncs.*

class BootStrap {

    def init = { servletContext ->
        
        environments {
            
            development {
                
                // Reporting Periods
                def reportingPeriodDecember = new ReportingPeriod(periodDate: '12/1/2010').save()
                def reportingPeriodJanuary = new ReportingPeriod(periodDate: '1/1/2011').save()
                def reportingPeriodFebruary = new ReportingPeriod(periodDate: '2/1/2011').save()
                def reportingPeriodMarch = new ReportingPeriod(periodDate: '3/1/2011').save()
             
                // Labor Categories
                def laborCategoryDataQualityStaff = new LaborCategory(name:'Data Quality Staff', userCreated:'sqv').save()
                def laborCategoryItCoordinator = new LaborCategory(name:'IT Coordinator', userCreated:'sqv').save()
                def laborCategoryOther = new LaborCategory(name:'Other', userCreated:'sqv').save()
                
                // Study Activity
                def studyActivityHighLowRecruitmentVanguard = new StudyActivity(name:'High-Low Recruitment Vanguard', userCreated:'sqv').save()
                def studyActivityExecutiveFunctionFormative = new StudyActivity(name:'WA 01 (Executive Function Formative)', userCreated:'sqv').save()
                def studyActivityDietaryAssessmentFormative = new StudyActivity(name:'WA 02 (Dietary Assessment Formative)', userCreated:'sqv').save()
                def studyActivityUlnarMeasurementFormative = new StudyActivity(name:'WA 03 (Ulnar Measurement Formative)', userCreated:'sqv').save()
                def studyActivityStressAndCortisolFormative = new StudyActivity(name:'WA 04 (Stress & Cortisol Formative)', userCreated:'sqv').save()
                def studyActivityBirthVisitQuestionnairesFormative = new StudyActivity(name:'WA 05 (Birth Visit Questionnaires Formative)', userCreated:'sqv').save()
 
                // Study Task
                def studyTaskDataManagement = new StudyTask(name:'Data Management', userCreated:'sqv').save()
                def studyTaskOther = new StudyTask(name:'Other', userCreated:'sqv').save()

                // Reporting Staff 
                def reportingStaffNgp = new ReportingStaff(
                    username:'ngp', 
                    lastName:'Portnov', 
                    firstName:'Natalya', 
                    middleInit:'', 
                    fullName:'Natalya Portnov', 
                    laborCategory:laborCategoryDataQualityStaff,
                    email:'ngp@cccs.umn.edu', 
                    isTestAccount:false, 
                    reportsEffort:true, 
                    userCreated:'sqv', 
                    appCreated:'ncs-dlr'
                ).save()
                def reportingStaffAjz = new ReportingStaff(
                    username:'ajz', 
                    lastName:'Zirbes', 
                    firstName:'Aaron', 
                    middleInit:'J', 
                    fullName:'Aaron J Zirbes', 
                    laborCategory:laborCategoryItCoordinator,
                    email:'ajz@cccs.umn.edu', 
                    isTestAccount:false, 
                    reportsEffort:true, 
                    userCreated:'sqv', 
                    appCreated:'ncs-dlr'
                ).save()

                // Assigned Effort 
                def reportedEffortfMarchNgp = new AssignedEffort(
                    period:reportingPeriodMarch, 
                    reportingStaff:reportingStaffNgp,
                    laborCategory:laborCategoryDataQualityStaff,
                    assignedEffort: , 
                    dateAssigned:'4/1/2011',
                    assigningStaff:'sqv', 
                    dateCommitted:'4/15/2011', 
                    commitingStaff:reportingStaffNgp
                ).save()
                /*
                def reportedEffortMarchAjz = new AssignedEffort(
                    reportingStaff:reportingStaffAjz,
                    laborCategory:laborCategoryItCoordinator,
                    period:reportingPeriodMarch, 
                    assignedEffort:'0', 
                    dateAssigned:'4/1/2011',
                    assigningStaff:'sqv', 
                    dateCommitted:'4/20/2011', 
                    commitingStaff:reportingStaffAjz
                ).save()
                */
            }//development
                
        } //environments
        
        println "started https://sqv.healthstudies.umn.edu:8443/ncs-dlr/"

    } //def init

    def destroy = { }

}
