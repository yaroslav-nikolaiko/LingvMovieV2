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
var forms_1 = require("@angular/forms");
var validators_1 = require("../utils/validators");
var router_1 = require("@angular/router");
var user_service_1 = require("./user.service");
var user_1 = require("./user");
var encoder_1 = require("../utils/encoder");
var UserFormComponent = (function () {
    function UserFormComponent(formBuilder, route, router, userService) {
        this.formBuilder = formBuilder;
        this.route = route;
        this.router = router;
        this.userService = userService;
        this.canDeactivate = false;
        this.user = new user_1.User();
    }
    UserFormComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.route.params
            .map(function (params) { return params['href']; })
            .map(function (href) { return _this.decodeURL(href); })
            .filter(function (href) { return href != 'new'; })
            .flatMap(function (href) { return _this.userService.get(href); })
            .do(function (user) { return user.address = user.address ? user.address : new user_1.Address(); })
            .subscribe(function (user) { return _this.user = user; });
        this.form = this.formBuilder.group({
            name: ['', forms_1.Validators.required],
            email: ['', validators_1.BasicValidators.email],
            address: this.formBuilder.group({
                street: [],
                suite: [],
                zip: [],
                city: []
            })
        });
    };
    UserFormComponent.prototype.save = function () {
        var _this = this;
        this.userService.save(this.user).subscribe(function () {
            _this.canDeactivate = true;
            _this.router.navigateByUrl("/");
        }, function (error) { return console.log(error); });
    };
    UserFormComponent.prototype.decodeURL = function (href) {
        return encoder_1.CustomUriEncoder.decode(href);
    };
    UserFormComponent = __decorate([
        core_1.Component({
            selector: 'users',
            templateUrl: "app/templates/user-form.html",
            directives: [forms_1.REACTIVE_FORM_DIRECTIVES],
            providers: [user_service_1.UserService]
        }), 
        __metadata('design:paramtypes', [forms_1.FormBuilder, router_1.ActivatedRoute, router_1.Router, user_service_1.UserService])
    ], UserFormComponent);
    return UserFormComponent;
}());
exports.UserFormComponent = UserFormComponent;
//# sourceMappingURL=user.form.component.js.map