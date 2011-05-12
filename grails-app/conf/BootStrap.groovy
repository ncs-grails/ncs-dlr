import edu.umn.ncs.*

class BootStrap {

    def init = { servletContext ->
        
        environments {
            
            development {
                
                /* Error checking for what was saved ***************************
                if ( ! reportedEffortfMarchNgp.save()) {
                    reportedEffortfMarchNgp.errors.each{ println it }
                }
                */
               
                // Reporting Periods
                //def reportingPeriodDecember = new ReportingPeriod(periodDate: '12/1/2010').save()
                //def reportingPeriodJanuary = new ReportingPeriod(periodDate: '1/1/2011').save()
                def reportingPeriodFebruary = new ReportingPeriod(
                    referenceInvoiceNumber:41368, 
                    periodDate:'2/1/2011', 
                    preparedDate:'2/28/2011', 
                    completedReportDate:'2/27/2011'
                ).save()
                def reportingPeriodMarch = new ReportingPeriod(
                    referenceInvoiceNumber:41548, 
                    periodDate:'3/1/2011', 
                    preparedDate:'2/30/2011', 
                    completedReportDate:'2/29/2011'
                ).save()
                def reportingPeriodApril = new ReportingPeriod(
                    referenceInvoiceNumber:55555, 
                    periodDate:'4/1/2011', 
                    preparedDate:'', 
                    completedReportDate:''
                ).save()
             
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
                    laborCategory:laborCategoryDataQualityStaff,
                    email:'sqv@cccs.umn.edu', 
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
                    laborCategory:laborCategoryItCoordinator,
                    email:'sqv@cccs.umn.edu', 
                    isTestAccount:false, 
                    reportsEffort:true, 
                    userCreated:'sqv', 
                    appCreated:'ncs-dlr'
                ).save()
                def reportingStaffSqv = new ReportingStaff(
                    username:'sqv', 
                    lastName:'Vuong', 
                    firstName:'Simone', 
                    middleInit:'Q', 
                    laborCategory:laborCategoryOther,
                    email:'simone.vuong@gmail.com', 
                    isTestAccount:false, 
                    reportsEffort:true, 
                    userCreated:'sqv', 
                    appCreated:'ncs-dlr'
                ).save()
                /*
                def reportingStaffSxv = new ReportingStaff(
                    username:'sxv', 
                    lastName:'Vuong', 
                    firstName:'Sim', 
                    middleInit:'', 
                    fullName:'Sim Vuong', 
                    laborCategory:laborCategoryOther,
                    email:'sqv@cccs.umn.edu', 
                    isTestAccount:false, 
                    reportsEffort:true, 
                    userCreated:'sqv', 
                    appCreated:'ncs-dlr'
                ).save()
                */
                
                // Assigned Effort 
                def reportedEffortfMarchNgp = new AssignedEffort(
                    reportingStaff:reportingStaffNgp,
                    period:reportingPeriodMarch, 
                    laborCategory:laborCategoryDataQualityStaff,
                    assignedEffort: 0.75, 
                    dateAssigned:'4/1/2011',
                    assigningStaff:reportingStaffSqv, 
                    dateCommitted:'4/20/2011', 
                    commitingStaff:reportingStaffNgp
                ).save()                
                def reportedEffortMarchAjz = new AssignedEffort(
                    reportingStaff:reportingStaffAjz,
                    laborCategory:laborCategoryItCoordinator,
                    period:reportingPeriodMarch, 
                    assignedEffort:0.655, 
                    dateAssigned:'4/1/2011',
                    assigningStaff:reportingStaffSqv, 
                    dateCommitted:'4/30/2011', 
                    commitingStaff:reportingStaffAjz
                ).save()
                def reportedEffortlAprilNgp = new AssignedEffort(
                    reportingStaff:reportingStaffNgp,
                    laborCategory:laborCategoryDataQualityStaff,
                    period:reportingPeriodApril, 
                    assignedEffort:0.4, 
                    dateAssigned:'5/1/2011',
                    assigningStaff:reportingStaffSqv, 
                    dateCommitted:'', 
                    commitingStaff:reportingStaffSqv
                ).save()
                def reportedEffortAprilAjz = new AssignedEffort(
                    reportingStaff:reportingStaffAjz,
                    laborCategory:laborCategoryItCoordinator,
                    period:reportingPeriodApril, 
                    assignedEffort:0.10, 
                    dateAssigned:'5/1/2011',
                    assigningStaff:reportingStaffSqv,
                    dateCommitted:'', 
                    commitingStaff:''
                ).save()
                def reportedEffortlAprilSqv = new AssignedEffort(
                    reportingStaff:reportingStaffSqv,
                    laborCategory:laborCategoryOther,
                    period:reportingPeriodApril, 
                    assignedEffort:0.50, 
                    dateAssigned:'5/1/2011',
                    assigningStaff:reportingStaffSqv,
                    dateCommitted:'', 
                    commitingStaff:''
                ).save()
                                        
                // email notification sent
                def emailNotificationAprilNgp1 = new NotificationEmail(
                    assignedEffort:reportedEffortlAprilNgp, 
                    dateSent:'5/5/2011', 
                    userSent:'sqv'
                ).save()
                def emailNotificationAprilNgp2 = new NotificationEmail(
                    assignedEffort:reportedEffortlAprilNgp, 
                    dateSent:'5/10/2011', 
                    userSent:'sqv'
                ).save()
              
            }//development
                
        } //environments
        
        println "started https://sqv.healthstudies.umn.edu:8443/ncs-dlr/"

    } //def init

    def destroy = { }

}
