/*global define*/
define([
	'jquery',
	'backbone'
], function ($, Backbone) {
	'use strict';

	var AppRouter = Backbone.Router.extend({
		initialize: function () {
			console.log("Route initialize");
		},
		routes: {
			//'*filter': 'setFilter',//*filter会拦截所有的请求
			"" : "index",
			"teams" : "getTeamList",
			"teams/:country" : "getTeamsCountry",
			"teams/:country/:name": "getTeam",
			"*error" : "fourOfour"
		},

		index: function(){
			// Homepage
			console.log("route.index ");
		},

		getTeamList: function() {
			// List all teams
			console.log("route.getTeamList ");
		},
		getTeamsCountry: function(country) {
			console.log("route.getTeamsCountry "+country);
		},
		getTeam: function(country, name) {
			console.log(String.format("route.getTeam country={0},name={1}",country,name));
		},
		fourOfour: function(error) {
			console.log("route.fourOfour 404 "+error);
		},

		setFilter: function (param) {
			// Set the current filter to be used
			//Common.TodoFilter = param || '';
			console.log("route.setFilter invoked,param="+param);
			// Trigger a collection filter event, causing hiding/unhiding
			// of the Todo view items
			//Todos.trigger('filter');
		}

		/**
		 *
		 创建的每个状态可以为书签。当 URL 碰到类似下面情况时，会调用这 5 个活动（index、getTeams、getTeamsCountry、getTeamCountry 和 fourOfour）。

		 http://www.example.com 触发 index()
		 http://www.example.com/#/teams 触发 getTeams()
		 http://www.example.com/#/teams/country1 触发 getTeamsCountry() 传递 country1 作为参数
		 http://www.example.com/#/teams/country1/team1 触发 getTeamCountry() 传递 country1 和 team1 作为参数
		 http://www.example.com/#/something 触发 fourOfour() 以作 * （星号）使用。

		 */

	});

	return AppRouter;
});
