import {Component} from '@angular/core';
import {NavbarComponent} from "./navbar.component";
import {ROUTER_DIRECTIVES} from "@angular/router";

@Component({
    selector: 'my-app',
    template: `
    <my-navbar></my-navbar>
    <div class="container">
        <router-outlet></router-outlet>
    </div>
    
    `,
    directives: [ROUTER_DIRECTIVES, NavbarComponent]
})
export class AppComponent {

    
}
