/**
 * Created by ztxs on 15-9-1.
 */
/*global define*/
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/identity/group.html',
    'collections/groups',
    'domReady!'
], function ($, _, Backbone,Handlebars,ViewTemplate,GroupsCollection) {
    'use strict';

    // Our overall **AppView** is the top-level piece of UI.
    var GroupView = Backbone.View.extend({

        el:'#appView',
        collection:new GroupsCollection(),
        template:Handlebars.compile(ViewTemplate),//替换underscoe的模板  _.template(ViewTemplate),
        events: {
            'click .btn':		'testBtnClick'
        },

        initialize: function () {
            this.listenTo(this.collection, "sync", this.render);
            this.collection.fetch();
        },

        render: function () {
            //Collection.at 根据数组编号获取模型
            //Collection.get 根据id字段获取数据
            //console.log(this.collection.at(0));

            //将collection的数据转为json并向模板输出
            this.$el.append(this.template(this.collection.toJSON()));

            return this;
        },

        testBtnClick:function(){
            alert("测试");
        }
    });

    return GroupView;
});