/**
 * Created by ztxs on 15-9-1.
 */
/*global require*/
'use strict';

// Require.js allows us to configure shortcut alias
require.config({
    // The shim config allows us to configure dependencies for
    // scripts that do not call define() to register a module
    shim: {
        bootstrap:{
            deps: [
                'jquery'
            ],
            exports:'bootstrap'
        },
        underscore: {
            exports: '_'
        },
        backbone: {
            deps: [
                'underscore',
                'jquery'
            ]
        }
    },
    paths: {
        bootstrap:'../lib/bootstrap/bootstrap',
        jquery: '../lib/jquery/jquery',
        underscore: '../lib/underscore/underscore',
        backbone: '../lib/backbone/backbone',
        text: '../lib/requirejs-text/text',
        domReady:'../lib/requirejs-domReady/domReady',
        handlebars:'../lib/handlebars/handlebars'
    }
});

require([
    'backbone',
    'views/group/groups',
    'routers/router',
    'bootstrap'
], function (backbone,GroupView,AppRouter) {
    /*jshint nonew:false*/
    // Initialize routing and start Backbone.history()
    var router = new AppRouter();

    //20150902测试，在Router的setFilter中要想捕获param，必须设置pushState=true
    backbone.history.start({pushState : true});
/**启用 HTML5 特性 pushState 的配置调用 start() 方法。
 * 对于那些支持 pushState 的浏览器，Backbone 将监视 popstate 事件以触发一个新状态。
 * 如果浏览器不能支持 HTML5 特性，那么 onhashchange 活动会被监视。
 * 如果浏览器不支持该事件，轮询技术将监视 URL 散列片段的任何更改*/
    // Initialize the application view
    new GroupView();
});