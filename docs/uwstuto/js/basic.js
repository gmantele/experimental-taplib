function changeAction(){
	var action = $('#action').val();

	loadDetails(action);
	
	if (action == '')
		return;
	
	updateUri();
	
	if (action == 'homePage')
		load('GET', '');
	else if (action == 'jobList')
		load('GET', '');
	else if (action == 'createJob')
		load('POST', 'time=10');
	else if (action == 'jobSummary')
		load('GET', '');
	else if (action == 'getJobParam')
		load('GET', '');
	else if (action == 'setJobParam1'){
		load('POST', '');
		updateParam();
	}else if (action == 'setJobParam2'){
		load('PUT', '');
		updateParam();
	}else if (action == 'startJob')
		load('POST', 'PHASE=RUN');
	else if (action == 'abortJob')
		load('POST', 'PHASE=ABORT');
	else if (action == 'destroyJob1')
		load('DELETE', '');
	else if (action == 'destroyJob2')
		load('POST', 'ACTION=DELETE');
}

function loadDetails(action){
	if (action == "jobSummary" || action == "destroyJob1" || action == "destroyJob2" || action == "startJob" || action == "abortJob"){
		$('#jobIdLine').show();
		$('#jobAttLine').hide();
		$('#jobAtt').val('');
	}else if (action == "getJobParam" || action == "setJobParam1" || action == "setJobParam2"){
		$('#jobIdLine').show();
		$('#jobAttLine').show();
	}else{
		$('#jobIdLine').hide();
		$('#jobId').val('');
		$('#jobAttLine').hide();
		$('#jobAtt').val('');
	}
	
	if (action == "setJobParam1" || action == "setJobParam2")
		$('#attValueLine').show();
	else
		$('#attValueLine').hide();
}

function load(method, params){
	$('#method').val(method);
	changeMethod();
	
	$('#params').val(params);
}

function resetAction(){
	if ($('#action').val() != ''){
		$('#action').val('');
		loadDetails('');
	}
}

function changeMethod(){
	if ($('#method').val() == "POST" || $('#method').val() == "PUT")
		$('#paramsLine').show();
	else
		$('#paramsLine').hide();
}

function changeFormat(id){
	if (id == 'acceptXml')
		$('#acceptJson').prop('checked', false);
	else
		$('#acceptXml').prop('checked', false);
}

function updateUri(){
	var uri = '/';
	var uriField = $('#uri');
	var action = $('#action').val();

	if (action == '')
		return;
	
	if (action != 'homePage'){
		uri = uri+'timers';
	
		if (action != 'jobList' && action != 'createJob'){
			uri = uri+'/'+$('#jobId').val();
			
			if (action != 'jobSummary' && action != 'destroyJob'){
				if (action == 'startJob' || action == 'abortJob')
					uri = uri+'/phase';
				else
					uri = uri+'/'+$('#jobAtt').val();	
			}
		}
	}
		
	uriField.val(uri);
}

function updateParam(){
	var queryPart = $('#jobAtt').val()+"="+$('#attValue').val();
	
	if ($('#method').val() == "POST"){
		updateUri();
		$('#params').val(queryPart);
	}else if ($('#method').val() == "PUT"){
		updateUri();
		$('#params').val(queryPart);
	}
}

function reset(){
	$('#action').val('');
	
	$('#jobIdLine').hide();
	$('#jobId').val('');
	
	$('#jobAttLine').hide();
	$('#attValueLine').hide();
	$('#jobAtt').val('');
	
	$('#paramsLine').hide();
	
	$('#uri').val('');
	$('#method').val('GET');
	$('#params').val('');
	
	$('#status').html('');
	$('#format').html('');
	$('#lastAction').html('');
	$('#results').html('');
	
	$('#resultBlock').hide();
}

function execute(){
	// get the XML representation of the jobs list:
	var req = null;
	try {
		req = new ActiveXObject("Microsoft.XMLHTTP");    // Essayer Internet Explorer 
	}catch(e){   // Echec
		req = new XMLHttpRequest();  // Autres navigateurs
	}

	var uri = $('#uri').val().replace(/\./g, '');
	req.open($('#method').val(), '/uwstuto/basic'+uri, true);
	req.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
	if ($('#acceptJson').is(':checked'))
		req.setRequestHeader("Accept", $('#acceptJson').val());
	else
		req.setRequestHeader("Accept", $('#acceptXml').val());
	
	req.onreadystatechange=function(){
		 if (req.readyState==4){
			$('#status').html(req.status+' <i>('+req.statusText+')</i>');
			$('#format').html(req.getResponseHeader('Content-type'));
			
			var myXml = req.responseText;
			if (req.getResponseHeader('Content-type').toLowerCase().indexOf('html') >= 0){
				console.log(myXml);
				console.log("Start: " + myXml.indexOf('<body>')+6);
				console.log("End: "+ myXml.indexOf('</body>'));
				myXml = myXml.substring(myXml.indexOf('<body>')+6, myXml.indexOf('</body>'));
				console.log(myXml);
				$('#results').html('<div style="border: solid black 1px; padding: .5em">'+myXml+'</div>');
			}else{
				myXml = myXml.replace(/</g, "&lt;");
				myXml = myXml.replace(/>/g, "&gt;");
				var prettyprintLang = "";
				if (req.getResponseHeader('Content-type').toLowerCase().indexOf('json') >= 0){
					prettyprintLang = 'prettyprint lang-js';
					myXml = JSON.stringify(JSON.parse(myXml), null, '\t');
				}else if (req.getResponseHeader('Content-type').toLowerCase().indexOf('xml') >= 0)
					prettyprintLang = 'prettyprint lang-xml';
				$('#results').html('<pre class="'+prettyprintLang+'">'+myXml+'</pre>');
				prettyPrint();
			}
			if (req.status == 200)
				$('#resultBlock').removeClass('panel-success').removeClass('panel-danger').addClass('panel-success');
			else
				$('#resultBlock').removeClass('panel-success').removeClass('panel-danger').addClass('panel-danger');
			getExecutedAction();
		 }else
			 $('#status').html("sending request...("+req.readyState+"/4)");
	};
	 $('#status').html("sending request...(1/4)");
	 $('#format').html("");
	 $('#results').html("");
	 $('#resultBlock').show();
	req.send($('#params').val());
}

function getExecutedAction(){
	// get the XML representation of the jobs list:
	var req = null;
	try {
		req = new ActiveXObject("Microsoft.XMLHTTP");    // Essayer Internet Explorer 
	}catch(e){   // Echec
		req = new XMLHttpRequest();  // Autres navigateurs
	}
	req.open('GET', '/uwstuto/basic?lastAction', true);
	req.onreadystatechange=function(){
		 if (req.readyState==4)
			 $('#lastAction').html(req.responseText);
	}
	req.send(null);
}
