<script type="text/javascript">
	/**
	 * Hide all optional properties (i.e. all table rows with the class 'optional').
	 */
	function toggleOptional(){
		var button = document.getElementById("toggleOptional");
		var display = (button.value == "hide") ? 'none' : '';
		var lines = document.querySelectorAll("tr.optional");
		for(var i=0 ; i<lines.length ; i++)
			lines[i].style.display = display;
		button.value = (display == '') ? 'hide' : 'show';
		button.textContent = (display == '') ? 'Hide optional' : 'Show optional';
	}
</script>
