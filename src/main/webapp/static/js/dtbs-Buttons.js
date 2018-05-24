function myAddEvent(obj, ev, fn) {
	if (obj.attachEvent) {
		obj.attachEvent('on' + ev, fn);
	} else {
		obj.addEventListener(ev, fn, false);
	}
}

function ajax(url, fnSucc, fnFaild)
{
	if(window.XMLHttpRequest)
	{
		var oAjax=new XMLHttpRequest();
	}
	else
	{
		var oAjax=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	oAjax.open('GET', url, true);
	
	
	oAjax.send();
	
	oAjax.onreadystatechange=function ()
	{
		if(oAjax.readyState==4)
		{
			if(oAjax.status==200)
			{
				fnSucc(oAjax.responseText);
			}
			else
			{
				if(fnFaild)
				{
					fnFaild();
				}
			}
		}
	};
}

var url = "/getLocation?regionCode=";
var url1 = "/getSlotsVisualJson?regionCode=";

function initialButtons(){
	
	var tempTest = doTest();
	
	var buttons = document.getElementsByClassName("regions");
	var locationsEle = document.getElementById("locations");
	var size = buttons.length;
	// console.log(size);
	for (var i = 0; i < size; i++) {
		buttons[i].onclick = function() {
			var regionCode = this.innerHTML;
			releaseButton(buttons);
			this.disabled = true;

			ajax(url + regionCode, function(str) {
				// console.log("call back");
				// console.log(str);
				str = str.replace("[", "");
				str = str.replace("]", "");
				var parts = str.split(',');
				console.log(parts.length);
				while (locationsEle.firstChild) {
					// console.log(locationsEle.firstChild);
					locationsEle.removeChild(locationsEle.firstChild);
				}
				var partsLength = parts.length;
				for (var j = 0; j < partsLength && partsLength > 1; j++) {
					var button = document.createElement("button");
					var textNode = document.createTextNode(parts[j]);
					button.appendChild(textNode);
					locationsEle.append(button);
					myAddEvent(button, 'click', function(ev) {
						var oEvent = ev || event;
						console.log(oEvent.toElement.innerHTML);
						// alert(url
						// +regionCode+"&locationId="+oEvent.toElement.innerHTML);
						releaseButton(this.parentElement
								.getElementsByTagName("button"));
						this.disabled = true;
						console.log(url1 + regionCode + "&locationId="
								+ this.innerHTML);
						console.log("testJson");
						tempTest(url1 + regionCode + "&locationId="
								+ this.innerHTML);
					});
				}
			}, function() {
				console.log("error");
			});
		}
	}
}


function releaseButton(regionsButtons) {
	for (var i = 0; i < regionsButtons.length; i++) {
		regionsButtons[i].disabled = false;
	}
}