<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Welcome to DTBS</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="static/js/html5shiv.min.js"></script>
<script src="static/js/respond.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="http://d3js.org/d3.v3.js"></script>
<script src="https://d3js.org/d3.v4.min.js"></script>
<script src="http://d3-legend.susielu.com/d3-legend.min.js"></script>

<link href="static/css/dtbs.css" rel="stylesheet">
<script src="static/js/dtbs-heatMapJson.js"></script>
<script src="static/js/dtbs-Buttons.js"></script>
<script src="static/js/dtbs.js"></script>

</head>
<body>
	<div class="jumbotron text-center">
		<h1>DTBS Manager</h1>
		<table class="tg">
		  <tr>
		    <th class="tg-yw4l" colspan="7">DABS Roster Rollouts</th>
		  </tr>
		  <tr>
		    <td class="tg-rmb8">Region</td>
		    <td class="tg-rmb8">Start Slot</td>
		    <td class="tg-rmb8">Last Slot</td>
		    <td class="tg-rmb8">Available(%)</td>
		    <td class="tg-rmb8">Slots</td>
		    <td class="tg-rmb8" colspan="2"></td>
		  </tr>
		  <tr>
		    <td class="tg-yw4l">All</td>
		    <td id="all_s" class="tg-yw4l">xx/xx/xxxx</td>
		    <td id="all_l" class="tg-lqy6">xx/xx/xxxx</td>
		    <td id="all_a" class="tg-baqh">xx%</td>
		    <td id="all_ss" class="tg-baqh">xxx</td>
		    <td  class="tg-baqh"><button id="all_r">Rollout</button></td>
		    <td  class="tg-baqh"><button id="all_w">Wipe</button></td>
		  </tr>
		  <tr>
		    <td class="tg-cuvh">North</td>
		    <td id="north_s" class="tg-cuvh">xx/xx/xxxx</td>
		    <td id="north_l" class="tg-cc5g">xx/xx/xxxx</td>
		    <td id="north_a" class="tg-vk7f">xx%</td>
		    <td id="north_ss" class="tg-vk7f">xxx</td>
		    <td  class="tg-vk7f"><button id="north_r">Rollout</button></td>
		    <td  class="tg-vk7f"><button id="north_w">Wipe</button></td>
		  </tr>
		  <tr>
		    <td class="tg-yw4l">South</td>
		    <td id="south_s" class="tg-yw4l">xx/xx/xxxx</td>
		    <td id="south_l" class="tg-lqy6">xx/xx/xxxx</td>
		    <td id="south_a" class="tg-baqh">xx%</td>
		    <td id="south_ss" class="tg-baqh">xxx</td>
		    <td  class="tg-baqh"><button id="south_r">Rollout</button></td>
		    <td  class="tg-baqh"><button id="south_w">Wipe</button></td>
		  </tr>
		</table>
		<hr/>
		<div id="progressDiv" class="progress toDisplayNon">
    		<div  id="progressId" class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width:80%">
      				80%
   			 </div>
    	</div>
	</div>
	<hr/>
	<button class="regions">ALL</button>
	<button class="regions">SOUTH</button>
	<button class="regions">NORTH</button>
	<br/>
	<div id="locations">No Data</div>
	<hr/>
	<!-- Heat Map begin -->
		<div id="chart"></div>
	    <!-- Heat Map end -->
		<hr/>
	<hr />
</body>
</html>