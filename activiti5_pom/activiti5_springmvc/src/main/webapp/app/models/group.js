/**
 * Created by ztxs on 15-9-1.
 */
define([
    'underscore',
    'backbone'
], function (_, Backbone) {
    'use strict';

    var Group = Backbone.Model.extend({
        defaults: {
            title: '',
            completed: false
        },

        // Toggle the `completed` state of this todo item.
        toggle: function () {
            this.save({
                completed: !this.get('completed')
            });
        }
    });

    return Group;
});
