<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Welcome to DTBS</title>
<script src="jquery-3.2.1.slim.min.js"></script>
<script>
</script>
</head>
<body>
	To test the rollout 
	<form id="rollout-form" action="rollout" method="post">
		<table>
			<thead>
				<tr>
					<td>Days</td>
					<td>Region</td>
					<td>StartDate</td>
					<td>RolloutStatus</td>
					<td>User</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<input name="days" type="text" value="5" />
					</td>
					<td>
						<input name="region" type="text" value='SOUTH'/>
					</td>
					<td>
						<input name="startDate" type="text" value='01/01/2020' />
					</td>
					<td>
						<input name="rolloutStatus" type="text" value='PUBLISHED' />
					</td>
					<td>
						<input name="user" type="text"  value='John C Test'/>
					</td>
				</tr>
				<input type="submit" value="load..." />
			</tbody>
		</table>
	</form>
	<a href="#" onclick="rollOutFun()">Go</a>
	<hr />
	
</body>
</html>