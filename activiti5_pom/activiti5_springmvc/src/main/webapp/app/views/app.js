/*global define*/
define([
	'jquery',
	'underscore',
	'backbone',
	'text!templates/app.html',
	'domReady!'

], function ($, _, Backbone,appTemplate) {
	'use strict';

	// Our overall **AppView** is the top-level piece of UI.
	var AppView = Backbone.View.extend({

		el:'body',

		template: _.template(appTemplate),
		events: {
			'click #testbtn':		'testBtnClick'
		},

		initialize: function () {

		},

		render: function () {
			this.$el.empty();
			this.$el.html(this.template());
			return this;
		},

		testBtnClick:function(){
			//alert("测试");
		}
	});

	return AppView;
});
