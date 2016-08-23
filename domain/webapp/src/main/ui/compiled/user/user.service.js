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
var UserService = (function () {
    function UserService(halClient) {
        this.halClient = halClient;
    }
    UserService.prototype.getUsers = function () {
        return this.halClient.getList('accounts');
    };
    UserService.prototype.get = function (href) {
        return this.halClient.get(href);
    };
    UserService.prototype.getByComment = function (comment) {
        var options = {
            search: "byComment",
            params: {
                href: comment._links['self'].href
            }
        };
        return this.halClient.getList("accounts", options);
    };
    UserService.prototype.save = function (user) {
        if (user['_links'])
            return this.halClient.update(user);
        else
            return this.halClient.save('accounts', user);
    };
    UserService.prototype.delete = function (user) {
        return this.halClient.delete(user);
    };
    UserService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [hal_client_1.HalClient])
    ], UserService);
    return UserService;
}());
exports.UserService = UserService;
//# sourceMappingURL=user.service.js.map