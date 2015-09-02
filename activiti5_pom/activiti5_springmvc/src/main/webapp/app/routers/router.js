/*global define*/
define([
	'jquery',
	'backbone'
], function ($, Backbone) {
	'use strict';

	var AppRouter = Backbone.Router.extend({
		routes: {
			'*filter': 'setFilter',
			"" : "index",
			"/teams" : "getTeams",
			"/teams/:country" : "getTeamsCountry",
			"/teams/:country/:name ": "getTeam",
			"*error" : "fourOfour"
		},

		index: function(){
			// Homepage
		},

		getTeams: function() {
			// List all teams
		},
		getTeamsCountry: function(country) {
			// Get list of teams for specific country
		},
		getTeam: function(country, name) {
			// Get the teams for a specific country and with a specific name
		},
		fourOfour: function(error) {
			// 404 page
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
