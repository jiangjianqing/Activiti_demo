/**
 * Created by jiangjianqing on 2015/7/23.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/layout/north.html',
    'jqueryui',
    'jqueryThemeHelper'
],function($,_,Backbone,Handlebars,northTemplate){
    'use strict';

    var NorthView=Backbone.View.extend({
        tagName:'div',
        template: Handlebars.compile(northTemplate),//替换underscoe的模板  _.template(northTemplate),
        initialize:function(){
            this.render();
        },
        render:function(){
            this.$el.append(this.template({appName:"Backbone Demo"}));
            this.$("#theme_changer").Themehelper();
            console.log("northview被渲染");
            return this;
        }
    });

    return NorthView;
});