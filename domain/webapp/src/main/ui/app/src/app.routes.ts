import {provideRouter, RouterConfig} from '@angular/router';
import {UserFormComponent} from "./user/user.form.component";
import {CanDeactivateUserForm} from "./utils/can.deacrivate.user.form";
import {PostsComponent} from "./post/posts.component";
import {UsersComponent} from "./user/users.component";
import {LoginComponent} from "./login/login.component";

export const routes: RouterConfig = [
    {
        path: '',
        redirectTo: "users",
        pathMatch: "full"
    },
    { path: 'users', component: UsersComponent },
    { path: 'users/:href', component: UserFormComponent, canDeactivate: [CanDeactivateUserForm] },
    { path: 'posts', component: PostsComponent },
    { path: 'login', component:  LoginComponent}
];



export const APP_ROUTER_PROVIDERS = [
    provideRouter(routes)
];