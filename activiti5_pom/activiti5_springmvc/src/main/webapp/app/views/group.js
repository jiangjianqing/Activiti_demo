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
            "click a[name='del']":"OnDelGroupClick"
        },

        initialize: function () {
            this.listenTo(this.collection, "sync", this.render);
            //this.listenTo(this.collection, "remove", this.render);
            this.collection.fetch();
        },

        render: function () {
            //Collection.at 根据数组编号获取模型
            //Collection.get 根据id字段获取数据
            //console.log(this.collection.at(0));

            //将collection的数据转为json并向模板输出
            this.$el.empty();
            this.$el.append(this.template(this.collection.toJSON()));

            return this;
        },

        OnDelGroupClick:function(event){
            var collection=this.collection;
            var $dlgDelGroup=this.$el.find("#dlgDelGroup");
            console.log($dlgDelGroup);
            var ShowDelGroupDialog=function(){
                var dfd=$.Deferred();
                $dlgDelGroup.modal("show");
                $dlgDelGroup.one("click","button",function(event){
                    if(event.currentTarget.id==="delGroup"){
                        dfd.resolve();
                    }else{
                        dfd.reject();
                    }
                    $dlgDelGroup.modal("hide");
                });

                return dfd.promise();
            }

            var $tr=$(event.currentTarget).parents("tr[data-id]");
            var groupId=$tr.data("id");
            var group=this.collection.get(groupId);
            ShowDelGroupDialog().done(function(){
                group.destroy().done(function(){
                    //注意：服务器端一定要返回json数据，否则永远都是触发fail，应该是parse出错导致,backbone默认请求的都是json数据
                    $tr.remove();
                    //经测试，这里已经不需要单独执行remove操作，backbone内部触发collection的删除操作
                    //console.log(collection.length);
                    //collection.remove(group);
                    //console.log(collection.length);
                })
            });
            //this.collection.remove(this.collection.get(groupId));collection.remove没有从服务器上删除数据

        }
    });

    return GroupView;
});