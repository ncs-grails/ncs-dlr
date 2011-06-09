$(document).ready(function(){

    // ADD button to add new effort is pressed
    $("#buttonMainAdd").click(function(){
        
        alert("buttonMainAdd clicked");

        // get url to go to
        var url = $("form[name='reportedEffort-create']").attr('action');       // /ncs-dlr/reportedEffort/create
        alert("(#buttonMainAdd).click(function().url = " + url)
        
        // get asignedEffort.id from main.show
        var assignedEffortId = $("form[name='main']").find("input[name='assignedEffort.id']").val();
        alert("(#buttonMainAdd).click(function().assignedEffortId = " + assignedEffortId)
                
        // get data to load
        var data = {'assignedEffort.id': assignedEffortId}                      // [object Object]
        alert("(#buttonMainAdd).click(function().data = " + data)

        $("#addOrEditEffortForm").load(url, data);
        $("#addDeleteEditCommitControls").hide();

        return false;

    });
     
    // EDIT button to edit effort is pressed
    $("#buttonMainEdit").click(function(){
        
        alert("buttonMainEdit clicked");

        var url = $("form[name='reportedEffort-create']").attr('action');
        alert("(#buttonMainEdit).click(function().url = " + url)

        $("#addOrEditEffortForm").load(url);
        $("#addDeleteEditCommitControls").hide();
        
        return false;
        
     });

 });

// reload main.show page
function loadAssignedEffort() {

    alert("in dlr.js > function loadAssignedEffort");

    var url = $("form[name='main']").attr('action');
    alert("in dlr.js > function loadAssignedEffort.url = " + url);

    var assignedEffortId = $("form[name='reportedEffort-create']").find("input[name='assignedEffort.id']").val();
    alert("in dlr.js > function loadAssignedEffort.assignedEffortId = " + assignedEffortId);

    var data = {'assignedEffort.id': assignedEffortId};
    alert("in dlr.js > function loadAssignedEffort.data = " + data);
    
    //display (assignedEffort.show): 1) message boxes: ASSIGNED, REPORTED COMMITED message and 2) EFFORT REPORTED so far
    $("#showAssignedEffortInclude").load(url, data);    
    $("#addOrEditEffortForm").html("");

    return false;
    
}

// reset main.show, when CANCEL button is pressed
function resetForm() {
    
    alert("in function resetForm()");
    
    $("#addOrEditEffortForm").html("");
    $("#addDeleteEditCommitControls").show();

    return false;
    
}

