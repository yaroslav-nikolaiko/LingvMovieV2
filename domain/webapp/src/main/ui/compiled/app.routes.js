"use strict";
var router_1 = require('@angular/router');
var user_form_component_1 = require("./user/user.form.component");
var can_deacrivate_user_form_1 = require("./utils/can.deacrivate.user.form");
var posts_component_1 = require("./post/posts.component");
var users_component_1 = require("./user/users.component");
var login_component_1 = require("./login/login.component");
exports.routes = [
    {
        path: '',
        redirectTo: "users",
        pathMatch: "full"
    },
    { path: 'users', component: users_component_1.UsersComponent },
    { path: 'users/:href', component: user_form_component_1.UserFormComponent, canDeactivate: [can_deacrivate_user_form_1.CanDeactivateUserForm] },
    { path: 'posts', component: posts_component_1.PostsComponent },
    { path: 'login', component: login_component_1.LoginComponent }
];
exports.APP_ROUTER_PROVIDERS = [
    router_1.provideRouter(exports.routes)
];
//# sourceMappingURL=app.routes.js.map