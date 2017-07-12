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
        },
        highcharts: {
            deps: ['jquery'],
            exports: 'Highcharts'
        }
    },
    paths: {
        bootstrap:'../lib/bootstrap/bootstrap',
        jquery: '../lib/jquery/jquery',
        underscore: '../lib/underscore/underscore',
        backbone: '../lib/backbone/backbone',
        text: '../lib/requirejs-text/text',
        domReady:'../lib/requirejs-domReady/domReady',
        handlebars:'../lib/handlebars/handlebars',
        highcharts: '../lib/highcharts/highcharts'
    }
});

require([
    'backbone',
    'routers/router',
    'bootstrap',
    'domReady'
], function (backbone,AppRouter) {
    //设定页面标题
    $("head>title").text("Activiti Process Explorer");
    // Initialize routing and start Backbone.history()
    //一个应用中只有一个router，所有的路由都有其管理，这里将其管理的el范围传递进去
    new AppRouter('#appView');


    //20150906,使用Html5的pushState特性，服务器端也必须设定Rewrite attributes，尚未测试
    backbone.history.start(/*{pushState : true,root: '/'}*/);


});