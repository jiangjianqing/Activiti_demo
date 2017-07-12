/**
 * Created by ztxs on 15-9-9.
 */
'use strict';
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/process/process.html'
],function($, _, Backbone,Handlebars, ViewTemplate){
    var ProcessView=Backbone.View.extend({
        tagName:'tr',
        template:Handlebars.compile(ViewTemplate),
        events:{
            "click a[name='delete']":"onDel",
            "click a[name='start']":"onStart"
        },
        initialize:function(){
            this.listenTo(this.model,"change",this.render);
            this.listenTo(this.model,"destroy",this.remove);
        },
        render:function(){
            this.$el.html(this.template({
                ctx:ctx,
                pd:this.model.toJSON()
            }));
            return this;
        },
        onDel:function(event){
            console.log(event.currentTarget.href);
            //this.model.destroy();//20150909 不能调用model.del触发删除，因为只能删除部署

            $.ajax(event.currentTarget.href,{
                context:this
            }).done(function(){
                this.model.destroy();
            });
            //console.log("process deleted!");
            return false;
        },
        onStart:function(){
            console.log("process started!");
            return false;
        }
    });
    return ProcessView;
});