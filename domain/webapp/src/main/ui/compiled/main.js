"use strict";
var platform_browser_dynamic_1 = require('@angular/platform-browser-dynamic');
var app_component_1 = require('./app.component');
var app_routes_1 = require("./app.routes");
var forms_1 = require("@angular/forms");
var can_deacrivate_user_form_1 = require("./utils/can.deacrivate.user.form");
var http_1 = require("@angular/http");
var hal_client_1 = require("./hal.client/hal.client");
platform_browser_dynamic_1.bootstrap(app_component_1.AppComponent, [
    can_deacrivate_user_form_1.CanDeactivateUserForm,
    app_routes_1.APP_ROUTER_PROVIDERS,
    http_1.HTTP_PROVIDERS,
    hal_client_1.HalClient,
    forms_1.disableDeprecatedForms(),
    forms_1.provideForms()
])
    .catch(function (err) { return console.error(err); });
//# sourceMappingURL=main.js.map