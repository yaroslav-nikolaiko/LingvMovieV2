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
var core_1 = require('@angular/core');
var user_service_1 = require("./user.service");
var encoder_1 = require("../utils/encoder");
var router_1 = require("@angular/router");
var UsersComponent = (function () {
    function UsersComponent(userService) {
        this.userService = userService;
    }
    UsersComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.userService.getUsers().subscribe(function (users) { return _this.users = users; });
    };
    UsersComponent.prototype.delete = function (user) {
        var _this = this;
        if (confirm("Are you sure you want to delete " + user.name + "?")) {
            var index = this.users.indexOf(user);
            this.userService.delete(user).subscribe(function () {
                _this.users.splice(index, 1);
            }, function (e) { return console.log(e); });
        }
    };
    UsersComponent.prototype.encodeURL = function (href) {
        return encoder_1.CustomUriEncoder.encode(href);
    };
    UsersComponent = __decorate([
        core_1.Component({
            selector: 'users',
            templateUrl: "app/templates/users.html",
            directives: [router_1.ROUTER_DIRECTIVES],
            providers: [user_service_1.UserService]
        }), 
        __metadata('design:paramtypes', [user_service_1.UserService])
    ], UsersComponent);
    return UsersComponent;
}());
exports.UsersComponent = UsersComponent;
//# sourceMappingURL=users.component.js.map