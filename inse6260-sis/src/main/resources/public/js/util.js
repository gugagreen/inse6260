/**
 * create a table row
 * @param rowData	values for each cell
 * @param isHeader	true if it is a header, false if a normal row
 * @param link	last cell in row. Can accept any html. Use "" if you want a blank content. 
 * Use null if you don't won't the cell to be rendered at all.  
 * @returns	The newly created row.
 */
function createRow(rowData, isHeader, link) {
	var row = $('<tr>');
	var celDef = (isHeader) ? ('<th>'): ('<td>');
	for (var i = 0; i < rowData.length; i++) {
		var cel = $(celDef).text(rowData[i]);
		row.append(cel);
	}
	
	if (link) {
		var celLink = $(celDef).html(link);
		row.append(celLink);
	}
	
	return row;
}

/**
 * Format a date in milliseconds (as it comes from java) to a human readable format (mm/dd/yyyy).
 * @param dateMs	date in milliseconds
 * @returns	date in string format.
 */
function formatDate(dateMs) {
	var jsDate =  new Date(dateMs);
	return jsDate.toLocaleDateString();
}