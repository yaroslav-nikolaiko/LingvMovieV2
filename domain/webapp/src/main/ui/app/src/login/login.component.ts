import {Component, OnInit} from "@angular/core";
import {REACTIVE_FORM_DIRECTIVES, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "./login.service";
import {Router} from "@angular/router";


@Component({
    selector: 'login',
    templateUrl: 'app/src/login/login.html',
    directives: [REACTIVE_FORM_DIRECTIVES],
    providers: [LoginService]
})
export class LoginComponent implements OnInit{
    form: FormGroup;
    user: any = {};

    constructor(private formBuilder: FormBuilder,
                private router: Router,
                private loginService: LoginService) {}

    ngOnInit() {
        this.form = this.formBuilder.group({});
    }

    login(){
        console.log("Login ....");
        console.log(JSON.stringify(this.user));
        this.loginService.login(this.user);
    }

    loginFacebook(){
        console.log("Login Facebook ....");
        //this.router.navigateByUrl()
        this.loginService.loginFacebook();
    }

    hello(){
        this.loginService.hello();
    }


}