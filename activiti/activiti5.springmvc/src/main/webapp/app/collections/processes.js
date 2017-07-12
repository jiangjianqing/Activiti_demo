/**
 * Created by ztxs on 15-9-9.
 */
'use strict';
define([
    'underscore',
    'backbone',
    'models/process'
],function(_,Backbone,ProcessModel){
   var ProcessCollection=Backbone.Collection.extend({
       url:ctx+"/backbone/process",
       model:ProcessModel
   }) ;
    return ProcessCollection;
});