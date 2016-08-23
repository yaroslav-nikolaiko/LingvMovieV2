"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var post_service_1 = require('./post.service');
var spinner_component_1 = require("../utils/spinner.component");
var user_service_1 = require("../user/user.service");
var pagination_component_1 = require("../utils/pagination.component");
var encoder_1 = require("../utils/encoder");
var router_1 = require("@angular/router");
var PostsComponent = (function () {
    function PostsComponent(_postService, _userService) {
        this._postService = _postService;
        this._userService = _userService;
        this.posts = [];
        this.users = [];
        this.isLoading = true;
    }
    PostsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.loadAllPosts();
        this._userService.getUsers().subscribe(function (u) { return _this.users = u; });
    };
    PostsComponent.prototype.select = function (post) {
        var _this = this;
        this.comments = post.comments().map(function (c) {
            c.forEach(function (comment) { return _this._userService.getByComment(comment).subscribe(function (u) { return comment['user'] = u; }); });
            return c;
        });
        this.currentPost = post;
    };
    PostsComponent.prototype.reloadPostsForUser = function (user) {
        var _this = this;
        this.isLoading = true;
        if (!user) {
            this.loadAllPosts();
            return;
        }
        this._postService.getByUser(user)
            .subscribe(function (posts) {
            _this.pagingEntity = posts;
            _this.posts = posts.list;
        }, function (error) { return console.log(error); }, function () { return _this.isLoading = false; });
    };
    PostsComponent.prototype.loadAllPosts = function () {
        var _this = this;
        this._postService.getPosts()
            .subscribe(function (posts) {
            _this.pagingEntity = posts;
            _this.posts = posts.list;
        }, null, function () { return _this.isLoading = false; });
    };
    PostsComponent.prototype.onPageChanged = function (posts) {
        this.isLoading = false;
        this.posts = posts;
    };
    PostsComponent.prototype.postsLoading = function () {
        this.isLoading = true;
    };
    PostsComponent.prototype.encodeURL = function (href) {
        if (!href)
            return "#";
        return encoder_1.CustomUriEncoder.encode(href);
    };
    PostsComponent = __decorate([
        core_1.Component({
            templateUrl: 'app/templates/posts.html',
            providers: [post_service_1.PostService, user_service_1.UserService],
            directives: [spinner_component_1.SpinnerComponent, pagination_component_1.PaginationComponent, router_1.ROUTER_DIRECTIVES]
        }), 
        __metadata('design:paramtypes', [post_service_1.PostService, user_service_1.UserService])
    ], PostsComponent);
    return PostsComponent;
}());
exports.PostsComponent = PostsComponent;
//# sourceMappingURL=posts.component.js.map