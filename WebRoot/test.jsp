
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>

		<title>Custom Checkboxes, Custom Radio Buttons, Custom Select Lists</title>

		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="keywords" content="checkbox, radio button, select, option, list, html, javascript, example" />
		<meta name="description" content="Custom form elements to style HTML checkboxes, radio buttons and select lists." />

		<script type="text/javascript" src="js/custom-form-elements.js"></script>
		<link rel="stylesheet" href="css/board.css" media="screen" />
		
		<style type="text/css">
			body {
				padding: 20px;
				font: 0.8em/21px arial,sans-serif;
			}
			p {
				font: 1em/1.7em arial,sans-serif;
			}
			.third {
				width: 30%;
				float: left;
			}
			.clear {
				clear: both;
			}
			.code {
				font: 1.2em/1.7em monospace;
			}
		</style>

	</head>
	<body>
		<h1>Custom HTML Checkboxes, Radio Buttons and Select Lists</h1>
		<h2>Working Example with Submission</h2>
		<form method="post" action=".">
			<div >
				<p><input type="checkbox" name="1" id="1" class="styled" /> <label for="1">(styled)</label></p>
				<p><input type="checkbox" name="2" id="2" class="styled" /> <label for="2">(styled)</label></p>
				<p><input type="checkbox" name="3" id="3" /> <label for="3">(unstyled)</label></p>
				<p><input type="checkbox" name="4" id="4" /> <label for="4">(unstyled)</label></p>
			</div>
			<div class="third left">
				<p><input type="radio" name="5" id="5" value="1" class="styled" /> <label for="5">(styled)</label></p>
				<p><input type="radio" name="5" id="6" value="2" class="styled" /> <label for="6">(styled)</label></p>
				<p><input type="radio" name="6" id="7" value="1" /> <label for="7">(unstyled)</label></p>
				<p><input type="radio" name="6" id="8" value="2" /> <label for="8">(unstyled)</label></p>
			</div>
			<div class="third left">
				<p><select name="7" class="styled">
					<option value="1">Option 1</option>
					<option value="2">Option 2</option>
					<option value="3">Option 3</option>
					<option value="4">Option 4</option>
					<option value="5">Option 5</option>
				</select></p>
				<p><select name="8" style="width: 190px;">
					<option value="1">Option 1</option>
					<option value="2">Option 2</option>
					<option value="3">Option 3</option>
					<option value="4">Option 4</option>
					<option value="5">Option 5</option>
				</select></p>
				<p>Select lists appear unstyled in IE6 and some versions of Opera.</p>
			</div>
			<p class="clear" style="padding: 20px 0 50px;"><input type="submit" value="Submit" style="width: 200px;" /></p>
		</form>
		<h2>Disabled Checkbox and Radio Button</h2>
		<ol class="code">
			<li>&lt;input type="checkbox" class="styled" disabled="disabled" /&gt;</li>
			<li>&lt;input type="radio" class="styled" checked="checked" disabled="disabled" /&gt;</li>
		</ol>
		<p><input type="checkbox" id="9" class="styled" disabled="disabled" /> <label for="9">Disabled Unchecked</label></p>
		<p style="padding: 0 0 50px;"><input type="radio" id="10" class="styled" checked="checked" disabled="disabled" /> <label for="10">Disabled Checked</label></p>
		<h2>Not Checked and Checked by Default</h2>
		<ol class="code">
			<li>&lt;input type="checkbox" class="styled" /&gt;</li>
			<li>&lt;input type="checkbox" class="styled" checked="checked" /&gt;</li>
		</ol>
		<p><input type="checkbox" id="11" class="styled" /><label for="11"> Unchecked</label></p>
		<p style="padding: 0 0 50px;"><input type="checkbox" id="12" class="styled" checked="checked" /> <label for="12">Checked</label></p>
		<h2>Checked Radio Button in Group</h2>
		<ol class="code">
			<li>&lt;input type="radio" class="styled" /&gt;</li>
			<li>&lt;input type="radio" class="styled" checked="checked" /&gt;</li>
		</ol>
		<p><input type="radio" name="1" id="13" class="styled" /> <label for="13">Unchecked</label></p>
		<p style="padding: 0 0 50px;"><input type="radio" name="1" id="14" class="styled" checked="checked" /> <label for="14">Checked</label></p>
		<h2>No Radio Button Checked By Default</h2>
		<ol class="code">
			<li>&lt;input type="radio" class="styled" /&gt;</li>
			<li>&lt;input type="radio" class="styled" /&gt;</li>
		</ol>
		<p><input type="radio" name="2" id="15" class="styled" /> <label for="15">Unchecked</label></p>
		<p style="padding: 0 0 50px;"><input type="radio" name="2" id="16" class="styled" /> <label for="16">Unhecked</label></p>
		<ol class="code">
			<li>&lt;select class="styled" /&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;Who&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option selected="selected"&gt;What&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;When&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;Where&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;How&lt;/option&gt;</li>
			<li>&lt;/select&gt;</li>
		</ol>
		<p style="padding: 0 0 50px;"><select class="styled" name="who">
			<option>Who</option>
			<option selected="selected">What</option>
			<option>When</option>
			<option>Where</option>
			<option>How</option>
		</select></p>
		<ol class="code">
			<li>&lt;select class="styled" /&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;This&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;That&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;These&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;Those&lt;/option&gt;</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&lt;option&gt;Theirs&lt;/option&gt;</li>
			<li>&lt;/select&gt;</li>
		</ol>
		<p style="padding: 0 0 50px;"><select class="styled" name="this">
			<option>This</option>
			<option>That</option>
			<option>These</option>
			<option>Those</option>
			<option>Theirs</option>
		</select></p>
		<p class="copyright">Copyright &copy; 2007-2008, 2011-2014 Ryan Fait</p>
	</body>
</html>