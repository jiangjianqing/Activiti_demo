/**
 * Created by ztxs on 15-9-2.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'text!templates/identity/group.html'
], function ($, _, Backbone,Handlebars, ViewTemplate) {
    'use strict';

    var GroupView = Backbone.View.extend({

        tagName:  'tr',

        template: Handlebars.compile(ViewTemplate),

        // The DOM events specific to an item.
        /*
        events: {
            'click .toggle':	'toggleCompleted',
            'dblclick label':	'edit',
            'click .destroy':	'clear',
            'keypress .edit':	'updateOnEnter',
            'keydown .edit':	'revertOnEscape',
            'blur .edit':		'close'
        },*/
        //注意：这里的Model是由外面创建时传递进来的
        //重点内容：这里将页面的维护与逻辑进行了分离！！！！
        initialize: function () {
            //model的change触发render，重新渲染
            this.listenTo(this.model, 'change', this.render);
            //model的destroy事件出发view的remove，这里可以认为是调用了this.$el.remove
            this.listenTo(this.model, 'destroy', this.remove);
        },

        // Re-render the titles of the todo item.
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            this.$el.attr("data-id",this.model.id);//把groupId放这里主要是为了体验parent
            return this;
        },

    });

    return GroupView;
});