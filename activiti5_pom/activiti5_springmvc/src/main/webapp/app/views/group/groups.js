/**
 * Created by ztxs on 15-9-1.
 */
/*global define*/
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/identity/groups.html',
    'collections/groups',
    'views/group/group',
    'domReady!'
], function ($, _, Backbone,Handlebars,ViewTemplate,GroupsCollection,GroupItemView) {
    'use strict';

    // Our overall **AppView** is the top-level piece of UI.
    var GroupView = Backbone.View.extend({
        /*
        * 图提供一个由 el 属性定义的 HTML 元素。
        * 该属性可以是由 tagName、className 和 id 属性相组合而构成的，或者是通过其本身的 el 值形成的
        * el、tagName、className 和 id 属性为空，那么会默认将一个空的 DIV 分配给 el。
        * */

        el:'#appView',
        //viewState用于保存视图状态，做到view Data-Less
        viewState:new Backbone.Model(),
        //model : new App.Models.Team();
        collection:new GroupsCollection(),
        template:Handlebars.compile(ViewTemplate),//替换underscoe的模板  _.template(ViewTemplate),
        /*
        * events 属性的每个项均由两部分构成：
         左边部分指定事件类型和触发事件的选择器。
         右边部分定义了事件处理函数。
         event context= this view
        * */
        events: {
            "click a[name='del']":"OnDelGroupClick"
        },

        initialize: function () {
            /*
            * 当模型发生更改时，会自动触发 render() 方法，从而节省数行代码。
            * 从 Backbone 0.5.2 开始，bind()  on()方法就开始接受使用第三个参数来定义回调函数的对象。
            * 在 Backbone 0.5.2 之前的版本中，必须使用 underscore.js 中的 bindAll 函数
            * _.bindAll(this, "render");
            * */
            //this.model.bind("change", this.render, this);



            //做到Dom只在model发生改变时才改变
            this.listenTo(this.viewState,"change",this.render());
            //更好的版本，绑定具体的事件
            //this.listenTo(this.viewState, 'change:readMore', this.renderReadMore);
            //this.listenTo(this.collection, "remove", this.render);
            this.render();

            this.listenTo(this.collection, "reset", this.addAll);
            this.collection.fetch({reset:true});
        },

        render: function () {

            //Collection.at 根据数组编号获取模型
            //Collection.get 根据id字段获取数据
            //console.log(this.collection.at(0));

            //将collection的数据转为json并向模板输出
            //this.$el.html(this.template(this.collection.toJSON()));
            this.$el.html(this.template());

            this.$groupList=this.$("tbody");

            return this;
        },
        addOne:function(group){
            //console.log(group);
            var view = new GroupItemView({ model: group });
            this.$groupList.append(view.render().el);
        },
        addAll:function(){
            this.$groupList.empty();
            this.collection.each(this.addOne,this);
        },

        OnDelGroupClick:function(event){
            //20150902 经测试 context=this view
            var $dlgDelGroup=this.$el.find("#dlgDelGroup");
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
                    //$tr.remove();
                    //20150902屏蔽，$tr.remove交由GroupItemView完成
                    //这个方法在简单页面中可用

                    //20150902,经测试，这里this=jquery.ajax.options
                    //console.log(this);
                    //注意：服务器端一定要返回json数据，否则永远都是触发fail，应该是parse出错导致,backbone默认请求的都是json数据

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