/**
 * Created by ztxs on 15-9-1.
 */
define([
    'underscore',
    'backbone',
    'models/group'
], function (_, Backbone, Group) {
    'use strict';

    var GroupsCollection = Backbone.Collection.extend({
        // Reference to this collection's model.
        url: ctx+'/backbone/group',
        model: Group,

        // Filter down the list of all todo items that are finished.
        completed: function () {
            return this.where({completed: true});
        },

        // Filter down the list to only todo items that are still not finished.
        remaining: function () {
            return this.where({completed: false});
        },

        // We keep the Todos in sequential order, despite being saved by unordered
        // GUID in the database. This generates the next order number for new items.
        nextOrder: function () {
            return this.length ? this.last().get('order') + 1 : 1;
        },

        // Todos are sorted by their original insertion order.
        //comparator: 'order'
    });

    return GroupsCollection;
});