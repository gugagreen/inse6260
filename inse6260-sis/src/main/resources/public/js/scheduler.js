const ROOT_PATH = "http://localhost:8080";


$(document).ready(function() {
	loadSchedule();
});

function makeCalendar(scheduledEvents){
	
	$('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		editable: false,
		eventLimit: false, // allow "more" link when too many events
		events: scheduledEvents,//Put the event function here and remove the example below:
		eventRender: function(event){
		    return (event.ranges.filter(function(range){ // test event against all the ranges

		        return (event.start.isBefore(range.end) &&
		                event.end.isAfter(range.start));

		    }).length)>0; //if it isn't in one of the ranges, don't render it (by returning false)
		},	
	});	
}


function loadSchedule() {
	var results = [];
	if (isStudent()) {
		var studentId = $("#username").val();
		$('#currentStudent').val(studentId);
		ajaxLoadScheduleForStudent(studentId);
	} 
}

function ajaxLoadScheduleForStudent(studentId) {
	var scheduledEvents = [];

	$.when($.ajax({
		url : ROOT_PATH + "/cart/student/" + studentId,
		success: function(data){
			for (var i = 0; i < data.length; i++) {
				scheduledEvents.push({title: data[i].courseEntry.course.code,
					start: data[i].courseEntry.dates.startTime,
					end: data[i].courseEntry.dates.endTime,
					dow: getDow(data[i]),
					ranges:[
					        {start:formatDate(data[i].courseEntry.dates.startDate), end:formatDate(data[i].courseEntry.dates.endDate)}
					        ]
				});
			}
			return scheduledEvents;
		}
	})
	).done(function(data){
		makeCalendar(scheduledEvents);
		});
}

function formatDate(date){
	var dateStamp = new Date(date);
	var yyyy = dateStamp.getFullYear().toString();
	var mm = (dateStamp.getMonth()+1).toString(); // getMonth() is zero-based
    var dd  = dateStamp.getDate().toString();
	return yyyy + "/" + (mm[1]?mm:"0"+mm[0]) + "/" + (dd[1]?dd:"0"+dd[0]);;
}

function getDow(data){
	var array = [];
	var weekdays = data.courseEntry.dates.weekDays;
	for (var i =0; i< weekdays.length; i++){
		if(weekdays.charAt(i)!== "-"){
			array.push(i);
		}
	}
	return array;
}