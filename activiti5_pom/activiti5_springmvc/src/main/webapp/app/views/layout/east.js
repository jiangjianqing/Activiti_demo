/**
 * Created by jiangjianqing on 2015/7/23.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/layout/east.html',
    'jqueryui',
    'jstree'
],function($,_,Backbone,Handlebars,eastTemplate){
    'use strict';

    var EastView=Backbone.View.extend({
        tagName:'div',
        template: Handlebars.compile(eastTemplate),
        initialize:function(){
            this.render();
        },
        render:function(){
            this.$el.append(this.template());
            this.$('#tree1').jstree({ 'core' : {
                'data' : [
                    'Simple root node--jstree',
                    {
                        'text' : 'Root node 2',
                        'state' : {
                            'opened' : true,
                            'selected' : true
                        },
                        'children' : [
                            { 'text' : 'Child 1' },
                            'Child 2'
                        ]
                    }
                ]
            } });
            return this;
        }
    });
    return EastView;
});