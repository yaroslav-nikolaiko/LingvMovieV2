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
var hal_client_1 = require("../hal.client/hal.client");
var PostService = (function () {
    function PostService(halClient) {
        this.halClient = halClient;
    }
    PostService.prototype.getPosts = function () {
        var options = {
            params: {
                size: 2
            }
        };
        return this.halClient.getList('posts', options);
    };
    PostService.prototype.getByUser = function (user) {
        var options = {
            search: "byAccount",
            params: {
                size: 2,
                href: user._links['self'].href
            }
        };
        return this.halClient.getList('posts', options);
    };
    PostService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [hal_client_1.HalClient])
    ], PostService);
    return PostService;
}());
exports.PostService = PostService;
//# sourceMappingURL=post.service.js.map