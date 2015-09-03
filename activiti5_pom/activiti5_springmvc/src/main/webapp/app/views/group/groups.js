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
    'models/group',
    'models/ViewState',
    'domReady!'
], function ($, _, Backbone,Handlebars,ViewTemplate,GroupsCollection,GroupItemView,GroupModel,ViewStateModel) {
    'use strict';

    // Our overall **AppView** is the top-level piece of UI.
    var GroupView = Backbone.View.extend({
        /*
        * 图提供一个由 el 属性定义的 HTML 元素。
        * 该属性可以是由 tagName、className 和 id 属性相组合而构成的，或者是通过其本身的 el 值形成的
        * el、tagName、className 和 id 属性为空，那么会默认将一个空的 DIV 分配给 el。
        * */

        el:'#appView',
        //
        //
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
            "click a[name='del']":"onDelGroupClick",
            "click #btnShowAddGroupForm":"onShowGroupForm",
            "click a[name='modify']":"onShowGroupForm",
            "hidden.bs.modal #dlgInputGroupInfo":"onHideGroupForm",
            "click button[id='btnAddGroup']":"onAddGroupClick"
        },

        initialize: function () {
            /*
            * 当模型发生更改时，会自动触发 render() 方法，从而节省数行代码。
            * 从 Backbone 0.5.2 开始，bind()  on()方法就开始接受使用第三个参数来定义回调函数的对象。
            * 在 Backbone 0.5.2 之前的版本中，必须使用 underscore.js 中的 bindAll 函数
            * _.bindAll(this, "render");
            * */
            //this.model.bind("change", this.render, this);

            //viewState用于保存视图状态，做到view Data-Less
            this.viewState=new ViewStateModel();

            //做到Dom只在model发生改变时才改变
            //this.listenTo(this.viewState,"change",this.render());
            //更好的版本，绑定具体的事件
            this.listenTo(this.viewState, 'change:state', this.onStateChange);
            //this.listenTo(this.collection, "remove", this.render);
            this.render();

            this.listenTo(this.collection, "reset", this.addAll);
            this.collection.fetch({reset:true});
        },

        render: function () {

            //Collection.at 根据数组编号获取模型
            //Collection.get 根据id、cid字段获取数据
            //console.log(this.collection.at(0));

            //将collection的数据转为json并向模板输出
            //this.$el.html(this.template(this.collection.toJSON()));
            this.$el.html(this.template());

            this.$groupList=this.$("tbody");
            this.$formAddGroup=this.$('#formAddGrop');
            this.$dlgInputGroupInfo=this.$("#dlgInputGroupInfo");

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
        onStateChange:function(){
            if(this.viewState.has("state")){
                var state = this.viewState.get("state");
                console.log(state);
                if (state == "add") {
                    this.$dlgInputGroupInfo.find("#myModalLabel").text("Add Group");
                    this.$dlgInputGroupInfo.find("input[id$=id]").removeAttr("disabled");
                    this.$dlgInputGroupInfo.find("input[id]").val("");
                } else {
                    this.$dlgInputGroupInfo.find("#myModalLabel").text("Modify Group");
                    var groupId = this.viewState.get("groupId");
                    var group=this.collection.get(groupId);

                    var $idInput = this.$dlgInputGroupInfo.find("input[id$='id']");
                    $idInput.attr("disabled", "disabled");
                    $idInput.val(groupId);

                    var $nameInput = this.$dlgInputGroupInfo.find("input[id$='name']");
                    $nameInput.val(group.get("name"));

                    var type=group.get("type");
                    this.$dlgInputGroupInfo.find("input:radio").removeAttr("checked").filter(function(index,item){
                        if(item.value==type){
                            $(item).prop("checked","checked");
                        }
                    })
                }
            }
        },

        onDelGroupClick:function(event){
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

        },
        onShowGroupForm:function(event){
            if(event.currentTarget.tagName=="BUTTON" && event.currentTarget.id==="btnShowAddGroupForm"){
                this.viewState.set("state","add");
                this.viewState.unset("groupId");
            }else{
                var $tr=$(event.currentTarget).parents("tr[data-id]");
                this.viewState.set("groupId",$tr.data("id"));
                this.viewState.set("state","modify");
            }
            this.$dlgInputGroupInfo.modal("show");
        },
        onHideGroupForm:function(){
            this.viewState.unset("state");
        },
        onAddGroupClick:function(event){
            //console.log(this.$formAddGroup.serialize());
            //var form=new FormData(this.$formAddGroup.get(0));
            //console.log(form);
            //console.log(this.$formAddGroup.serializeArray());
            //var newGroup=new GroupModel(this.$formAddGroup.serializeArray());
            var fieldArray=this.$formAddGroup.serializeArray();
            console.log(fieldArray);
            var indexed_array = {};
            $.map(fieldArray, function(item, index){
                indexed_array[item['name']] = item['value'];
                //console.log("%o ,index=%d",n,index);
            });
            var isNew=this.viewState.get("state")==="add";
            var group=null;
            if(isNew){
                group=new GroupModel(indexed_array);
                this.collection.add(group);
            }else{
                group=this.collection.get(this.viewState.get("groupId"));
                group.set(indexed_array);
            }

            //var newGroup=this.collection.create(indexed_array);
            var $dlg=this.$dlgInputGroupInfo;
            var view=this;
            //20150903:由于Activiti5的Group要求id由用户输入，所以在新增的时候人工指定POST，以便add和modify保持一致
            //backbone默认情况下：POST =create  ，put=update
            group.save([],isNew?{type:"POST",context:this}:{}).done(function(){
                $dlg.modal("hide");
                if (isNew)
                    this.addOne(group);
            });
        }
    });

    return GroupView;
});