var all_sEle = null;
var all_ssEle = null;
var all_lEle = null;
var all_aEle = null;
var all_rEle = null;
var all_wEle = null;
var south_sEle = null;
var south_ssEle = null;
var south_lEle = null;
var south_aEle = null;
var south_rEle = null;
var south_wEle = null;
var north_sEle = null;
var north_ssEle = null;
var north_lEle = null;
var north_aEle = null;
var north_rEle = null;
var north_wEle = null;
var progressDiv_Ele = null;
var progressId_Ele = null;

var intervalToggle = null;

window.onload = function() {
	
	console.log("dtbs");
	
	//initailHeatMap();

	//Set up
	all_sEle = document.getElementById("all_s");
	all_ssEle = document.getElementById("all_ss");
	all_lEle = document.getElementById("all_l");
	all_aEle = document.getElementById("all_a");
	all_rEle = document.getElementById("all_r");
	all_wEle = document.getElementById("all_w");
	south_sEle = document.getElementById("south_s");
	south_ssEle = document.getElementById("south_ss");
	south_lEle = document.getElementById("south_l");
	south_aEle = document.getElementById("south_a");
	south_rEle = document.getElementById("south_r");
	south_wEle = document.getElementById("south_w");
	north_sEle = document.getElementById("north_s");
	north_ssEle = document.getElementById("north_ss");
	north_lEle = document.getElementById("north_l");
	north_aEle = document.getElementById("north_a");
	north_rEle = document.getElementById("north_r");
	north_wEle = document.getElementById("north_w");

	progressDiv_Ele = document.getElementById("progressDiv");
	progressId_Ele = document.getElementById("progressId");

	initialDate4All();
	initialDate4North();
	initialDate4South();

	initialSlots();

	all_rEle.onclick = function() {
		doRollout(this, "ALL");
		getRolloutProcessing();
	}
	south_rEle.onclick = function() {
		doRollout(this, "SOUTH");
		getRolloutProcessing();
	}
	north_rEle.onclick = function() {
		doRollout(this, "NORTH");
		getRolloutProcessing();
	}

	all_wEle.onclick = function() {
		doWipe(this, "ALL");
	}
	south_wEle.onclick = function() {
		doWipe(this, "SOUTH");
	}
	north_wEle.onclick = function() {
		doWipe(this, "NORTH");
	}
	
	/*
	var test86 = document.getElementById("test86");
	var tempTest = doTest();
	test86.onclick= function(){
		console.log("testJson");
		tempTest("getSlotsVisualJson?regionCode='SOUTH'&locationId='86'");
	}
	
	var test87 = document.getElementById("test87");
	var tempTest = doTest();
	test87.onclick= function(){
		console.log("testJson");
		tempTest("getSlotsVisualJson?regionCode='SOUTH'&locationId='87'");
	}
	 */
	initialButtons();
	
	
}


function getRolloutProcessing() {
	console.log("processing");
	progressDiv_Ele.style.display = "block";
	intervalToggle = setInterval(function() {
		var request = $.ajax({
			url : "getProcessing",
			method : "GET",
			dataType : "html"
		});
		console.log("calling process....");
		request.done(function(msg) {
			console.log(msg);
			var count = parseInt(msg);
			progressId_Ele['aria-valuenow'] = count;
			progressId_Ele['style']['width'] = msg
			progressId_Ele['innerHTML'] = msg;
		});
	//
	}, 1000);
	console.log(progressDiv_Ele.style);
}


function doRollout(element, regionCode) {
	var request = $.ajax({
		url : "rollout",
		method : "GET",
		data : {
			"regionCode" : regionCode
		},
		dataType : "html"
	});
	element.innerHTML = "Processing";
	element.style = "background:gray";
	doLock();
	request.done(function(msg) {
		element.innerHTML = "Success";
		setTimeout(function() {
			element.innerHTML = "Rollout";
			if (regionCode === "ALL") {
				initialDate4All();
			} else if (regionCode === "SOUTH") {
				initialDate4North();
			} else if (regionCode === "NORTH") {
				initialDate4South();
			}
			doUnlock();
		}, 1500);
	});
}

function doWipe(element, regionCode) {
	var request = $.ajax({
		url : "cancelBooking",
		method : "GET",
		data : {
			"regionCode" : regionCode
		},
		dataType : "html"
	});
	element.innerHTML = "Processing";
	element.style = "background:gray";
	doLock();
	request.done(function(msg) {
		console.log("done!" + msg);
		element.innerHTML = "Success";
		setTimeout(function() {
			element.innerHTML = "Wipe";
			if (regionCode === "ALL") {
				initialDate4All();
			} else if (regionCode === "SOUTH") {
				initialDate4North();
			} else if (regionCode === "NORTH") {
				initialDate4South();
			}
			doUnlock();
		}, 1500);
	});
}

function doLock() {
	north_rEle.disabled = true;
	north_wEle.disabled = true;
	south_rEle.disabled = true;
	south_wEle.disabled = true;
	all_rEle.disabled = true;
	all_wEle.disabled = true;
}
function doUnlock() {
	north_rEle.disabled = false;
	north_rEle.style = "background:#e0ffeb";
	north_wEle.disabled = false;
	north_wEle.style = "background:#e0ffeb";
	south_rEle.disabled = false;
	south_rEle.style = "background:#e0ffeb";
	south_wEle.disabled = false;
	south_wEle.style = "background:#e0ffeb";
	all_rEle.disabled = false;
	all_rEle.style = "background:#e0ffeb";
	all_wEle.disabled = false;
	all_wEle.style = "background:#e0ffeb";
}

function initialDate4All() {
	initialDate(all_sEle, "ALL", "startRolloutDate");
	initialDate(all_lEle, "ALL", "lastRolloutDate");
	initialDate(all_aEle, "ALL", "checkAvailableSlotsPer");
}
function initialDate4North() {
	initialDate(south_sEle, "SOUTH", "startRolloutDate");
	initialDate(south_lEle, "SOUTH", "lastRolloutDate");
	initialDate(south_aEle, "SOUTH", "checkAvailableSlotsPer");
}
function initialDate4South() {
	initialDate(north_sEle, "NORTH", "startRolloutDate");
	initialDate(north_lEle, "NORTH", "lastRolloutDate");
	initialDate(north_aEle, "NORTH", "checkAvailableSlotsPer");
}

function initialSlots() {
	initialDate(north_ssEle, "NORTH", "acquireSlotsNum");
	initialDate(south_ssEle, "SOUTH", "acquireSlotsNum");
	initialDate(all_ssEle, "ALL", "acquireSlotsNum");
}

function initialDate(element, regionCode, url) {
	var request = $.ajax({
		url : url,
		method : "GET",
		data : {
			"regionCode" : regionCode
		},
		dataType : "html"
	});
	request.done(function(msg) {
		console.log("done!" + msg);
		element.innerHTML = msg;
	});
}

function getLastRolloutDate() {
	var regionCode = document.getElementById("regionCodeR").value;
	console.log("region");
	var request = $.ajax({
		url : "lastRolloutDate",
		method : "GET",
		data : {
			"regionCode" : regionCode
		},
		dataType : "html"
	});
	request.done(function(msg) {
		console.log("done!" + msg);
		document.getElementById("rolloutDateContext").innerHTML = msg;
	});
}

function cancelMonthBooking() {
	var regionCode = document.getElementById("regionCodeC").value;
	console.log("region");
	var request = $.ajax({
		url : "cancelBooking",
		method : "GET",
		data : {
			"regionCode" : regionCode
		},
		dataType : "html"
	});
	request.done(function(msg) {
		console.log("done!" + msg);
		document.getElementById("clearBookingContext").innerHTML = msg;
	});
}

function checkAvailableSlots() {
	var regionCode = document.getElementById("regionCodeB").value;
	console.log("region");
	var request = $.ajax({
		url : "checkAvailableSlotsNum",
		method : "GET",
		data : {
			"regionCode" : regionCode
		},
		dataType : "html"
	});
	request.done(function(msg) {
		console.log("done!" + msg);
		document.getElementById("availableSlotsContext").innerHTML = msg;
	});
}