import edu.umn.ncs.*

class BootStrap {

    def init = { servletContext ->
        
        environments {
            
            development {
                
                if (false) {

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
                    def studyActivityHighLowRecruitmentVanguard = new StudyActivity(name:'High-Low Recruitment Vanguard', obsolete:false, userCreated:'sqv').save()
                    def studyActivityExecutiveFunctionFormative = new StudyActivity(name:'WA 01 (Executive Function Formative)', obsolete:false, userCreated:'sqv').save()
                    def studyActivityDietaryAssessmentFormative = new StudyActivity(name:'WA 02 (Dietary Assessment Formative)', obsolete:false, userCreated:'sqv').save()
                    def studyActivityUlnarMeasurementFormative = new StudyActivity(name:'WA 03 (Ulnar Measurement Formative)', obsolete:false, userCreated:'sqv').save()
                    def studyActivityStressAndCortisolFormative = new StudyActivity(name:'WA 04 (Stress & Cortisol Formative)', obsolete:false, userCreated:'sqv').save()
                    def studyActivityBirthVisitQuestionnairesFormative = new StudyActivity(name:'WA 05 (Birth Visit Questionnaires Formative)', obsolete:false, userCreated:'sqv').save()

                    // Study Task
                    /* run in ms sql to get list
                        SELECT 'def studyTask' 
                            + Replace(Replace(Replace(Replace(Description, ' ', ''), '-', ''), '/', '_'), '&', 'And')
                            + ' = new StudyTask(name:''' + Replace(Description, ' ', '') 
                            + ''', obsolete:false, userCreated:''sqv'').save()'
                        FROM WebLookup.dbo.ncs_task
                        WHERE Obsolete = 0                
                     */

                    //def studyTaskDataManagement = new StudyTask(name:'Data Management', obsolete:false, userCreated:'sqv').save()
                    //def studyTaskOther = new StudyTask(name:'Other', obsolete:false, userCreated:'sqv').save()

                    def studyTaskAdministrativeTasks = new StudyTask(name:'AdministrativeTasks', obsolete:false, userCreated:'sqv').save()
                    def studyTaskCommunityEngagementOther = new StudyTask(name:'CommunityEngagement-Other', obsolete:false, userCreated:'sqv').save()
                    def studyTaskCommunityEngagementDevelopingPlans = new StudyTask(name:'CommunityEngagement-DevelopingPlans', obsolete:false, userCreated:'sqv').save()
                    def studyTaskCommunityEngagementReviewingPlans = new StudyTask(name:'CommunityEngagement-ReviewingPlans', obsolete:false, userCreated:'sqv').save()
                    def studyTaskHospitalEngagement = new StudyTask(name:'HospitalEngagement', obsolete:false, userCreated:'sqv').save()
                    def studyTaskMeetingsOthers = new StudyTask(name:'Meetings-Others', obsolete:false, userCreated:'sqv').save()
                    def studyTaskMeetingswithCenterStaff = new StudyTask(name:'MeetingswithCenterStaff', obsolete:false, userCreated:'sqv').save()
                    def studyTaskMeetingswithCommunityMembers = new StudyTask(name:'MeetingswithCommunityMembers', obsolete:false, userCreated:'sqv').save()
                    def studyTaskMeetingswithProgramOffice = new StudyTask(name:'MeetingswithProgramOffice', obsolete:false, userCreated:'sqv').save()
                    def studyTaskMeetingswithSubcontractors = new StudyTask(name:'MeetingswithSubcontractors', obsolete:false, userCreated:'sqv').save()
                    def studyTaskProgramming_DataManagement = new StudyTask(name:'Programming/DataManagement', obsolete:false, userCreated:'sqv').save()
                    def studyTaskProtocolDevelopment = new StudyTask(name:'ProtocolDevelopment', obsolete:false, userCreated:'sqv').save()
                    def studyTaskOperationsPlanningAndDevelopment = new StudyTask(name:'OperationsPlanning&Development', obsolete:false, userCreated:'sqv').save()
                    def studyTaskScientificActivitiesOther = new StudyTask(name:'ScientificActivities-Other', obsolete:false, userCreated:'sqv').save()
                    def studyTaskScientificActivitiesAnalysis = new StudyTask(name:'ScientificActivities-Analysis', obsolete:false, userCreated:'sqv').save()
                    def studyTaskScientificActivitiesPilotStudies = new StudyTask(name:'ScientificActivities-PilotStudies', obsolete:false, userCreated:'sqv').save()
                    def studyTaskSpatialAnalysis = new StudyTask(name:'SpatialAnalysis', obsolete:false, userCreated:'sqv').save()
                    def studyTaskTraining = new StudyTask(name:'Training', obsolete:false, userCreated:'sqv').save()
                    def studyTaskWebcasts = new StudyTask(name:'Webcasts', obsolete:false, userCreated:'sqv').save()
                    def studyTaskWorkingTeam_CommitteeMeeting = new StudyTask(name:'WorkingTeam/CommitteeMeeting', obsolete:false, userCreated:'sqv').save()
                    def studyTaskOther = new StudyTask(name:'Other', obsolete:false, userCreated:'sqv').save()
                    def studyTaskNCSManagement = new StudyTask(name:'NCSManagement', obsolete:false, userCreated:'sqv').save()
                    def studyTaskStudyCoordination = new StudyTask(name:'StudyCoordination', obsolete:false, userCreated:'sqv').save()
                    def studyTaskIRBManagement = new StudyTask(name:'IRBManagement', obsolete:false, userCreated:'sqv').save()
                    def studyTaskSiteOfficeManagement = new StudyTask(name:'SiteOfficeManagement', obsolete:false, userCreated:'sqv').save()
                    def studyTaskInformaticsManagement = new StudyTask(name:'InformaticsManagement', obsolete:false, userCreated:'sqv').save()
                    def studyTaskPhoneCenterManagement = new StudyTask(name:'PhoneCenterManagement', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollectionTelephoneInterviewing = new StudyTask(name:'DataCollection-TelephoneInterviewing', obsolete:false, userCreated:'sqv').save()
                    def studyTaskMailoutPreparation = new StudyTask(name:'MailoutPreparation', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollectionListing = new StudyTask(name:'DataCollection-Listing', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollectionHouseholdEnumeration = new StudyTask(name:'DataCollection-HouseholdEnumeration', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollectionPregnancyScreening = new StudyTask(name:'DataCollection-PregnancyScreening', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollectionInformedConsent = new StudyTask(name:'DataCollection-InformedConsent', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollectionPrePregnancyVisit = new StudyTask(name:'DataCollection-Pre-PregnancyVisit', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollection1stPregnancyVisit = new StudyTask(name:'DataCollection-1stPregnancyVisit', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollection2ndPregnancyVisit = new StudyTask(name:'DataCollection-2ndPregnancyVisit', obsolete:false, userCreated:'sqv').save()
                    def studyTaskDataCollectionBirthVisit = new StudyTask(name:'DataCollection-BirthVisit', obsolete:false, userCreated:'sqv').save()
                    def studyTaskProviderEngagement = new StudyTask(name:'ProviderEngagement', obsolete:false, userCreated:'sqv').save()

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
                        email:'sqv@cccs.umn.edu', 
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
                    def assignedEffortMarchNgp = new AssignedEffort(
                        reportingStaff:reportingStaffNgp,
                        period:reportingPeriodMarch, 
                        laborCategory:laborCategoryDataQualityStaff,
                        assignedEffort: 0.75, 
                        dateAssigned:'4/1/2011',
                        assigningStaff:reportingStaffSqv, 
                        dateCommitted:'4/20/2011', 
                        commitingStaff:reportingStaffNgp
                    ).save()                
                    def assignedEffortMarchAjz = new AssignedEffort(
                        reportingStaff:reportingStaffAjz,
                        laborCategory:laborCategoryItCoordinator,
                        period:reportingPeriodMarch, 
                        assignedEffort:0.655, 
                        dateAssigned:'4/1/2011',
                        assigningStaff:reportingStaffSqv, 
                        dateCommitted:'4/30/2011', 
                        commitingStaff:reportingStaffAjz
                    ).save()
                    def assignedEffortlAprilNgp = new AssignedEffort(
                        reportingStaff:reportingStaffNgp,
                        laborCategory:laborCategoryDataQualityStaff,
                        period:reportingPeriodApril, 
                        assignedEffort:0.4, 
                        dateAssigned:'5/1/2011',
                        assigningStaff:reportingStaffSqv, 
                        dateCommitted:'5/30/2011', 
                        commitingStaff:reportingStaffNgp
                    ).save()                
                    def assignedEffortAprilAjz = new AssignedEffort(
                        reportingStaff:reportingStaffAjz,
                        laborCategory:laborCategoryItCoordinator,
                        period:reportingPeriodApril, 
                        assignedEffort:0.20, 
                        dateAssigned:'5/1/2011',
                        assigningStaff:reportingStaffSqv,
                        dateCommitted:'', 
                        commitingStaff:''
                    ).save()
                    def assignedEffortFebruarySqv = new AssignedEffort(
                        reportingStaff:reportingStaffSqv,
                        period:reportingPeriodFebruary, 
                        laborCategory:laborCategoryOther,
                        assignedEffort: 0.5, 
                        dateAssigned:'3/1/2011',
                        assigningStaff:reportingStaffSqv, 
                        dateCommitted:'3/20/2011', 
                        commitingStaff:reportingStaffSqv
                    ).save()                
                    def assignedEffortMarchSqv = new AssignedEffort(
                        reportingStaff:reportingStaffSqv,
                        period:reportingPeriodMarch, 
                        laborCategory:laborCategoryOther,
                        assignedEffort: 0.65, 
                        dateAssigned:'4/1/2011',
                        assigningStaff:reportingStaffSqv, 
                        dateCommitted:'4/20/2011', 
                        commitingStaff:reportingStaffSqv
                    ).save()                
                    def assignedEffortlAprilSqv = new AssignedEffort(
                        reportingStaff:reportingStaffSqv,
                        laborCategory:laborCategoryOther,
                        period:reportingPeriodApril, 
                        assignedEffort:0.50, 
                        dateAssigned:'5/1/2011',
                        assigningStaff:reportingStaffSqv,
                        dateCommitted:'', 
                        commitingStaff:''
                    ).save()

                    // Effort Reported
                    def reportedEffortAprilNgp1 = new ReportedEffort(
                        assignedEffort:assignedEffortlAprilNgp, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskProgramming_DataManagement, 
                        percentEffort:0.2,
                        dateCreated:'5/31/2011',
                        userCreated:reportingStaffNgp.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    def reportedEffortAprilNgp2 = new ReportedEffort(
                        assignedEffort:assignedEffortlAprilNgp, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskOther, 
                        percentEffort:0.2,
                        dateCreated:'5/31/2011',
                        userCreated:reportingStaffNgp.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    def reportedEffortAprilAjz1 = new ReportedEffort(
                        assignedEffort:assignedEffortAprilAjz, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskProgramming_DataManagement, 
                        percentEffort:0.1,
                        dateCreated:'5/29/2011',
                        userCreated:reportingStaffAjz.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    def reportedEffortFebruarySqv1 = new ReportedEffort(
                        assignedEffort:assignedEffortFebruarySqv, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskAdministrativeTasks, 
                        percentEffort:0.3,
                        dateCreated:'3/31/2011',
                        userCreated:reportingStaffSqv.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    def reportedEffortFebruarySqv2 = new ReportedEffort(
                        assignedEffort:assignedEffortFebruarySqv, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskProtocolDevelopment, 
                        percentEffort:0.2,
                        dateCreated:'3/31/2011',
                        userCreated:reportingStaffSqv.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    def reportedEffortMarchSqv1 = new ReportedEffort(
                        assignedEffort:assignedEffortMarchSqv, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskInformaticsManagement, 
                        percentEffort:0.4,
                        dateCreated:'4/31/2011',
                        userCreated:reportingStaffSqv.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    def reportedEffortMarchSqv2 = new ReportedEffort(
                        assignedEffort:assignedEffortMarchSqv, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskScientificActivitiesAnalysis, 
                        percentEffort:0.25,
                        dateCreated:'4/31/2011',
                        userCreated:reportingStaffSqv.username, 
                        appCreated:'ncs-dlr'
                    ).save()


                    def reportedEffortAprilSqv1 = new ReportedEffort(
                        assignedEffort:assignedEffortlAprilSqv, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskProgramming_DataManagement, 
                        percentEffort:0.3,
                        dateCreated:'5/31/2011',
                        userCreated:reportingStaffSqv.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    /*
                    def reportedEffortAprilSqv2 = new ReportedEffort(
                        assignedEffort:assignedEffortlAprilSqv, 
                        activity:studyActivityHighLowRecruitmentVanguard, 
                        task:studyTaskOther, 
                        percentEffort:0.2,
                        dateCreated:'5/31/2011',
                        userCreated:reportingStaffSqv.username, 
                        appCreated:'ncs-dlr'
                    ).save()
                    */

                    // email notification sent
                    def emailNotificationAprilNgp1 = new NotificationEmail(
                        assignedEffort:assignedEffortlAprilNgp, 
                        dateSent:'5/5/2011', 
                        userSent:'sqv'
                    ).save()
                    def emailNotificationAprilNgp2 = new NotificationEmail(
                        assignedEffort:assignedEffortlAprilNgp, 
                        dateSent:'5/10/2011', 
                        userSent:'sqv'
                    ).save()
                    def emailNotificationAprilAjz1 = new NotificationEmail(
                        assignedEffort:assignedEffortAprilAjz, 
                        dateSent:'5/5/2011', 
                        userSent:'sqv'
                    ).save()
                    
                }
              
            }//development
                
        } //environments
        
        println "started https://sqv.healthstudies.umn.edu:8443/ncs-dlr/"

    } //def init

    def destroy = { }

}
