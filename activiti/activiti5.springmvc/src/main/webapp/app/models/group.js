/**
 * Created by ztxs on 15-9-1.
 */
define([
    'underscore',
    'backbone'
], function (_, Backbone) {
    'use strict';

    var Group = Backbone.Model.extend({
        //urlRoot : '/specialTeams',//设置model对应的url
        defaults: {  // default attributes
            title: '',
            completed: false
        },
        validate : function(attributes){
            /*
            * 传递给该函数的惟一参数是一个 JavaScript 对象，该对象包含了 set() 方法更新的属性，
            * 以便验证那些属性的条件。如果从 validate() 方法中没有返回任何内容，那么验证成功。
            * 如果返回一个错误消息，那么验证失败，将无法执行 set() 方法。
            *
            * */
            //if (!!attributes && attributes.name === "teamX") {
                // Error message returned if the value of the "name"
                // attribute is equal to "teamX"
            //    return "Error!";
            //}
        },
        // Domain-specific methods go here
        // Toggle the `completed` state of this todo item.
        toggle: function () {
            this.save({
                completed: !this.get('completed')
            });
        }
    });

    return Group;
});
