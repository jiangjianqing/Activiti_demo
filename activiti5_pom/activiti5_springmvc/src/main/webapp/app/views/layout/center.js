/**
 * Created by jiangjianqing on 2015/7/23.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/layout/center.html',
    'jqueryui',
    'jqgrid',
    'mockjax'
],function($,_,Backbone,Handlebars,centerTemplate){
    'use strict';

    var CenterView=Backbone.View.extend({
        tagName:'div',
        template: Handlebars.compile(centerTemplate),
        initialize:function(){
            $.mockjax({
                url: "server.php?q=2",
                dataType: "json",
                response: function (settings) {
                    this.responseText = JSON.stringify(
                        {
                            "page":"1","total":2,"records":"13",
                            "rows":[
                                {"id":"13","cell":["13","2007-10-06","Client 3","1000.00","0.00","1000.00",null]},
                                {"id":"12","cell":["12","2007-10-06","Client 2","700.00","140.00","840.00",null]},
                                {"id":"11","cell":["11","2007-10-06","Client 1","600.00","120.00","720.00",null]},
                                {"id":"10","cell":["10","2007-10-06","Client 2","100.00","20.00","120.00",null]},
                                {"id":"9","cell":["9","2007-10-06","Client 1","200.00","40.00","240.00",null]},
                                {"id":"8","cell":["8","2007-10-06","Client 3","200.00","0.00","200.00",null]},
                                {"id":"7","cell":["7","2007-10-05","Client 2","120.00","12.00","134.00",null]},
                                {"id":"6","cell":["6","2007-10-05","Client 1","50.00","10.00","60.00",""]},
                                {"id":"5","cell":["5","2007-10-05","Client 3","100.00","0.00","100.00","no tax at all"]},
                                {"id":"4","cell":["4","2007-10-04","Client 3","150.00","0.00","150.00","no tax"]}
                            ],
                            "userdata":{"amount":3220,"tax":342,"total":3564,"name":"Totals:"}
                        }

                    );
                }
            });

            this.render();

        },
        render:function(){
            this.$el.append(this.template());
            this.$("#tabs").tabs();
            this.$("#list").jqGrid({
                url: 'server.php?q=2',
                datatype: "json",
                colNames: ['Inv No', 'Date', 'Client', 'Amount', 'Tax', 'Total', 'Notes'],
                colModel: [{name: 'id', index: 'id', width: 60, sorttype: "int"}, {
                    name: 'invdate',
                    index: 'invdate',
                    width: 90,
                    sorttype: "date"
                }, {name: 'name', index: 'name', width: 100}, {
                    name: 'amount',
                    index: 'amount',
                    width: 80,
                    align: "right",
                    sorttype: "float"
                }, {name: 'tax', index: 'tax', width: 80, align: "right", sorttype: "float"}, {
                    name: 'total',
                    index: 'total',
                    width: 80,
                    align: "right",
                    sorttype: "float"
                }, {name: 'note', index: 'note', width: 150, sortable: false}],
                rowNum: 10,
                rowList: [10, 20, 30],
                pager:'#pager1',
                sortname: 'id',
                viewrecords: true,
                sortorder: "desc",
                //regional : 'en',
                caption: "测试表格"

            });//.navGrid('#pager1',{edit:false,add:false,del:false});
            //this.$("#list").jqGrid('navGrid', '#pager1', {edit: false, add: false, del: false});
            return this;
        }
    });

    return CenterView;
});