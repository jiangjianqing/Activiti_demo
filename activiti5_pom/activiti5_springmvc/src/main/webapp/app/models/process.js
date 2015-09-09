/**
 * Created by ztxs on 15-9-9.
 */
'use strict';
define([
    'underscore',
    'backbone'
],function(_,Backbone){
    var ProcessModel=Backbone.Model.extend({
        //idAttribute:'deploymentId'
        // 20150909 这里修改了id的字段名，以方便model.destroy()时直接删除流程
        //但不符合规范，因为deploymentId指向的是部署id，故屏蔽
    });
    return ProcessModel;
});