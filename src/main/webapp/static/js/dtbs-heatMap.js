
function initailHeatMap() {
	var margin = {
			top : 50,
			right : 0,
			bottom : 100,
			left : 50
		},
		width = 800 - margin.left - margin.right,
		height = 460 - margin.top - margin.bottom,
		gridSize = Math.floor(width / 27), //25==> how columns are going to to displayed.
		legendElementWidth = gridSize * 2,
		buckets = 9,
		colors = [ "#ffffd9", "#7fcdbb", "#081d58" ],
		times = [ "08:00", "08:15", "09:10", "10:05", "10:20", "11:15", "12:10", "13:50", "14:45", "15:00", "15:55", "16:50" ],
		days = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27" ];
	datasets = [ "static/resource/LocationId-87.tsv", "static/resource/LocationId-88.tsv", "static/resource/LocationId-91.tsv", "static/resource/LocationId-92.tsv", "static/resource/LocationId-94.tsv", "static/resource/LocationId-95.tsv", "static/resource/LocationId-96.tsv" ];

	var domain = [ 0, 1, 2 ];
	var generator = d3.scaleLinear()
		.domain([ 0, (domain.length - 1) / 2, domain.length - 1 ])
		.range([
			"#ffffd9",
			"#7fcdbb",
			"#081d58" ]
	).interpolate(d3.interpolateCubehelix);
	var range = d3.range(domain.length).map(generator);
	var quantile = d3.scaleQuantile()
		.domain(domain)
		.range(range);

	var svg = d3.select("#chart").append("svg")
		.attr("width", width + margin.left + margin.right)
		.attr("height", height + margin.top + margin.bottom)
		.append("g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	var dayLabels = svg.selectAll(".dayLabel")
		.data(days)
		.enter().append("text")
		.text(function(d) {
			return d;
		})
		.attr("x", function(d, i) {
			return i * gridSize;
		})
		.attr("y", 0)
		.style("text-anchor", "middle")
		.attr("transform", "translate(" + gridSize / 1.5 + ", -6)")
		.attr("class", function(d, i) {
			return ((i >= 0 && i <= 27) ? "dayLabel mono axis axis-workweek" : "dayLabel mono axis");
		});

	var timeLabels = svg.selectAll(".timeLabel")
		.data(times)
		.enter().append("text")
		.text(function(d) {
			return d;
		})
		.attr("x", 0)
		.attr("y", function(d, i) {
			return i * gridSize;
		})
		.style("text-anchor", "end")
		.attr("transform", "translate(-6," + gridSize / 1.5 + ")")
		.attr("class", function(d, i) {
			return ((i >= 0 && i <= 27) ? "timeLabel mono axis axis-worktime" : "timeLabel mono axis");
		});
	heatmapChart(datasets[0]);

	var datasetpicker = d3.select("#dataset-picker").selectAll(".dataset-button")
		.data(datasets);

	datasetpicker.enter()
		.append("input")
		.attr("value", function(d) {
			return "Dataset " + d
		})
		.attr("type", "button")
		.attr("class", "dataset-button")
		.on("click", function(d) {
			heatmapChart(d);
		});
}
var heatmapChart = function(tsvFile) {
	d3.tsv(tsvFile,
		function(d) {
			//console.log("hour: "+d.hour);
			//	console.log("value: "+d.value);
			//console.log("day: "+d.day);
			return {
				day : +d.day,
				hour : +d.hour,
				value : +d.value
			};
		},
		function(error, data) {
			var colorScale = quantile;
			var cards = svg.selectAll(".hour")
				.data(data, function(d) {
					return d.day + ':' + d.hour;
				});

			cards.append("DTBS");

			cards.enter().append("rect")
				.attr("x", function(d) {
					return (d.day - 1) * gridSize;
				})
				.attr("y", function(d) {
					return (d.hour - 1) * gridSize;
				})
				.attr("rx", 4)
				.attr("ry", 4)
				.attr("class", "hour bordered")
				.attr("width", gridSize)
				.attr("height", gridSize)
				.style("fill", colors[1]);

			cards.transition().duration(1000)
				.style("fill", function(d) {
					return colorScale(d.value);
				});
			cards.select("title").text(function(d) {
				return d.value;
			});
			cards.exit().remove();

			var legend = svg.selectAll(".legend")
				.data([ 0 ].concat(colorScale.quantiles()), function(d) {
					return d;
				});

			legend.enter().append("g")
				.attr("class", "legend");

			legend.append("rect")
				.attr("x", function(d, i) {
					return legendElementWidth * i;
				})
				.attr("y", height + 30)
				.attr("width", legendElementWidth)
				.attr("height", gridSize / 2)
				.style("fill", function(d, i) {
					return colors[i];
				});

			legend.append("text")
				.attr("class", "mono")
				.text(function(d) {
					console.log(d);return ((d < 0.2) ? "Unavail" : ((d > 1) ? "Occupied" : "Vacant"))
				})
				.attr("x", function(d, i) {
					return legendElementWidth * i + 15;
				})
				.attr("y", height + gridSize + 30);

			legend.exit().remove();
		});
};