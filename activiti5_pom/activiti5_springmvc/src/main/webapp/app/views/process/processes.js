/**
 * Created by ztxs on 15-9-9.
 */
'use strict';
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/process/processes.html',
    'collections/processes',
    'views/process/process',
    'models/process'
],function($, _, Backbone,Handlebars,ViewTemplate,ProcessCollection,ProcessView,ProcessModel){
    var ProcessesView=Backbone.View.extend({
        collection:new ProcessCollection(),
        template:Handlebars.compile(ViewTemplate),
        events:{
            "submit":"onSubmitUpload"
        },
        initialize:function(){
            this.listenTo(this.collection,"reset",this.addAll);
            //有流程删除后，必须重新加载，因为删除是以部署为单位，可能一次部署了多个资源
            this.listenTo(this.collection,"remove",this.addAll);
        },
        render:function(){
            this.$el.html(this.template({
                ctx:ctx
            }));
            this.$processList=this.$("tbody");
            this.$formUpload=this.$("form");

            this.collection.fetch({reset:true});
            return this;
        },
        addOne:function(process){
            var view=new ProcessView({model:process});
            this.$processList.append(view.render().$el);
        },
        addAll:function(){
            this.$processList.empty();
            this.collection.each(this.addOne,this);
        },
        onSubmitUpload:function(){
            var formData=new FormData(this.$formUpload[0]);
            $.ajax(this.$formUpload.prop('action'),{
                context:this,
                data:formData,
                processData:false,
                type:"POST",
                contentType:false//this.$formUpload.prop('enctype') 20150909不设置false会报错
            }).done(function(){
                this.collection.fetch({reset:true});
            });
            //console.log("submit "+this.$formUpload.prop("action"));
            return false;//必须拦截原来的event
        }
    });
    return ProcessesView;
});