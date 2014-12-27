(function() {
	$(document).ready(
			function() {
				var Message = Backbone.Model.extend({
					idAttribute : 'id',
					defaults : {
						content : '',
					},
					validate : function(attributes, options) {
						if (attributes.content === '') {
							return 'content must be not empty.';
						}
					},
				});

				var MessageList = Backbone.Collection.extend({
					model : Message,
					url : './webapi/message',
					invalid : function(model, error) {
						console.log('invalid list', mode, error);
					}
				});

				var ItemView = Backbone.View.extend({
					tagName : 'tr',
					tmpl : _.template($('#tmpl-item').html()),
					events : {
						'click .delete' : 'onDelete',
					},
					initialize : function() {
						_.bindAll(this, 'render', 'onDelete', 'onDestroy');
						this.listenTo(this.model, 'change', this.render);
						this.listenTo(this.model, 'destroy', this.onDestroy);
					},
					onDelete : function() {
						this.model.destroy();
					},
					onDestroy : function() {
						this.remove();
					},
					render : function() {
						var obj = this.model.toJSON();
						obj.displayedCreateTime = (new Date(obj.createTime))
								.toLocaleTimeString().replace(/ JST$/, '');
						this.$el.html(this.tmpl(obj));
						return this;
					},
				});

				var ListView = Backbone.View
						.extend({
							initialize : function() {
								this.listenTo(this.collection, 'add',
										this.addItemView);
								var _this = this;
								this.collection.fetch({
									reset : true,
								}).done(function() {
									_this.render();
								});
							},
							render : function() {
								this.collection.each(function(item) {
									this.addItemView(item);
								}, this);
								return this;
							},
							addItemView : function(item) {
								this.$el.append(new ItemView({
									model : item,
								}).render().el);
							},
						});

				var AppView = Backbone.View.extend({
					events : {
						'click #postBtn' : 'onPost',
					},
					initialize : function() {
						_.bindAll(this, 'render', 'onPost');
						this.$content = $('#addForm [name="content"]');
						this.collection = new MessageList();
						this.listView = new ListView({
							el : $('#list'),
							collection : this.collection,
						});
						this.render();
					},
					render : function() {
						this.$content.val('');
					},
					onPost : function() {
						if (this.$content.val().length == 0)
							return;
						this.collection.create({
							content : this.$content.val(),
						}, {
							wait : true,
						});
						this.render();
					},
				});

				var appView = new AppView({
					el : $('#app'),
				});
			});
}());