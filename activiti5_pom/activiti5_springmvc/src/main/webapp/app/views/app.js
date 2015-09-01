/*global define*/
define([
	'jquery',
	'underscore',
	'backbone',
	'common',
	'text!templates/app.html',
	'views/layout/north',
	'views/layout/center',
	'views/layout/west',
	'views/layout/east',
	'domReady!',
	'jqueryui',
	'jqueryuiLayout'

], function ($, _, Backbone, Common,appTemplate,NorthView,CenterView,WestView,EastView) {
	'use strict';

	// Our overall **AppView** is the top-level piece of UI.
	var AppView = Backbone.View.extend({

		el:'#appView',

		template: _.template(appTemplate),
		events: {
			'click #testbtn':		'testBtnClick'
		},

		initialize: function () {
			this.$northView=new NorthView();
			this.$centerView=new CenterView();
			this.$westView=new WestView();
			this.$eastView=new EastView();
			//this.$('#testbtn').button();
			this.render();
		},

		render: function () {
			this.$el.append(this.template());
			this.$("#layoutNorth").append(this.$northView.el);
			this.$("#layoutCenter").append(this.$centerView.el);
			this.$("#layoutWest").append(this.$westView.el);
			this.$("#layoutEast").append(this.$eastView.el);
			this.$el.layout({
				resizerClass: 'ui-state-default',
				west__size: 300
				, east__size: 300
				// RESIZE Accordion widget when panes resize
				//, west__onresize: $.layout.callbacks.resizePaneAccordions
				//, east__onresize: function (pane, $Pane) {
				//	jQuery("#list5").jqGrid('setGridWidth', $Pane.innerWidth() - 20);
				//}

			});

		},

		testBtnClick:function(){
			//alert("测试");
		}
	});

	return AppView;
});
