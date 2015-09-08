/**
 * Created by ztxs on 15-9-8.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/layout/report.html'
],function($, _, Backbone,Handlebars, ViewTemplate){
    var ReportView=Backbone.View.extend({
        template:Handlebars.compile(ViewTemplate),
        render:function(){
            this.$el.html(this.template());
            return this;
        }
    });
    return ReportView;
})