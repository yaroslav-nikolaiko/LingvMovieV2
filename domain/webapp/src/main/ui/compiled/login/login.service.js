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
var http_1 = require("@angular/http");
var LoginService = (function () {
    function LoginService(http) {
        this.http = http;
        this.headers = new http_1.Headers();
        this.headers.append('X-Forwarded-Host', location.host);
    }
    LoginService.prototype.login = function (user) {
        var headers = new http_1.Headers(this.headers.toJSON());
        user.grant_type = "password";
        headers.append('Authorization', 'Basic ' + btoa('any:'));
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.http.post("api/oauth/token", this.transformRequest(user), {
            headers: headers
        }).subscribe(function (response) { localStorage.setItem("auth_token", response.text()); }, function (response) { console.log('Error ' + response); });
    };
    LoginService.prototype.hello = function () {
        var headers = new http_1.Headers(this.headers.toJSON());
        headers.append('Authorization', 'Bearer ' + JSON.parse(localStorage.getItem("auth_token")).access_token);
        headers.append('Content-Type', 'application/json');
        this.http.get("api/hello", {
            headers: headers
        }).subscribe(function (response) { console.log('Succes ' + response); }, function (response) { console.log('Error ' + response); });
    };
    LoginService.prototype.transformRequest = function (obj) {
        var str = [];
        for (var p in obj)
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        return str.join("&");
    };
    LoginService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], LoginService);
    return LoginService;
}());
exports.LoginService = LoginService;
//# sourceMappingURL=login.service.js.map