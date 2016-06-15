$(document).ready(function() {
	
	$('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		editable: true,
		eventLimit: true, // allow "more" link when too many events

		
		

		events: [//Put the event function here and remove the example below:
			{ title:1, //This is the title of the event
				start:"10:00", //This is the start time
				end:"12:00",  //This is the end time
				dow:[0,4], //This is the day of the week 0 being Sunday, 6 Being Saturday
				ranges:[{start:"2016/03/01", end:"2016/07/01"},] //This is the start and end dates for the repeating event.
			//remove the example above
}
		],
		
		
		
		eventRender: function(event){
		    return (event.ranges.filter(function(range){ // test event against all the ranges

		        return (event.start.isBefore(range.end) &&
		                event.end.isAfter(range.start));

		    }).length)>0; //if it isn't in one of the ranges, don't render it (by returning false)
		},
		
		
	});
	
});