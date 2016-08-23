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
var forms_1 = require("@angular/forms");
var login_service_1 = require("./login.service");
var router_1 = require("@angular/router");
var LoginComponent = (function () {
    function LoginComponent(formBuilder, router, loginService) {
        this.formBuilder = formBuilder;
        this.router = router;
        this.loginService = loginService;
        this.user = {};
    }
    LoginComponent.prototype.ngOnInit = function () {
        this.form = this.formBuilder.group({});
    };
    LoginComponent.prototype.login = function () {
        console.log("Login ....");
        console.log(JSON.stringify(this.user));
        this.loginService.login(this.user);
    };
    LoginComponent.prototype.loginFacebook = function () {
        console.log("Login Facebook ....");
        //this.router.navigateByUrl()
    };
    LoginComponent.prototype.hello = function () {
        this.loginService.hello();
    };
    LoginComponent = __decorate([
        core_1.Component({
            selector: 'login',
            templateUrl: 'app/src/login/login.html',
            directives: [forms_1.REACTIVE_FORM_DIRECTIVES],
            providers: [login_service_1.LoginService]
        }), 
        __metadata('design:paramtypes', [forms_1.FormBuilder, router_1.Router, login_service_1.LoginService])
    ], LoginComponent);
    return LoginComponent;
}());
exports.LoginComponent = LoginComponent;
//# sourceMappingURL=login.component.js.map