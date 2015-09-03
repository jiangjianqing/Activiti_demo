/**
 * Created by cz_jjq on 9/3/15.
 */
define([
    'underscore',
    'backbone'
],function(_,Backbone){
    'use strict';

    var ViewState=Backbone.Model.extend({
        defaults: {  // default attributes
            title: '',
            completed: false
        },
    });
    return ViewState;
})