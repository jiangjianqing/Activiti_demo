/**
 * Created by jiangjianqing on 2015/7/23.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/layout/west.html',
    'jqueryui'
],function($,_,Backbone,westTemplate){
    'use strict';

    var WestView=Backbone.View.extend({
        tagName:'div',
        template: _.template(westTemplate),
        initialize:function(){
            this.render();
        },
        render:function(){
            this.$el.append(this.template());
            this.$('#accordion1').accordion({
                heightStyle: "fill"
            });
            return this;
        }
    });
    return WestView;
});