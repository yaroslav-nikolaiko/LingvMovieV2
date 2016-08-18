import {Component, OnInit,} from '@angular/core';
import {REACTIVE_FORM_DIRECTIVES, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {BasicValidators} from "../utils/validators";
import {Router, ActivatedRoute} from "@angular/router";
import {UserService} from "./user.service";
import {User, Address} from "./user";
import {CustomUriEncoder} from "../utils/encoder";

@Component({
    selector: 'users',
    templateUrl: "app/templates/user-form.html",
    directives: [REACTIVE_FORM_DIRECTIVES],
    providers: [UserService]
})
export class UserFormComponent implements OnInit{
    form: FormGroup;
    canDeactivate = false;
    user = new User();

    constructor(private formBuilder: FormBuilder,
                private route: ActivatedRoute,
                private router:Router,
                private userService: UserService) {}

    ngOnInit() {
        this.route.params
            .map(params=>params['href'])
            .map(href => this.decodeURL(href))
            .filter(href=>href != 'new')
            .flatMap(href=>this.userService.get(href))
            .do(user=>user.address = user.address ? user.address : new Address())
            .subscribe(user=>this.user = user);

        this.form = this.formBuilder.group({
            name: ['', Validators.required],
            email: ['', BasicValidators.email],
            address: this.formBuilder.group({
                street: [],
                suite: [],
                zip: [],
                city: []
            })
        });
    }

    save(){
        this.userService.save(this.user).subscribe(
            ()=>{
                this.canDeactivate = true;
                this.router.navigateByUrl("/");
            },
                error=>console.log(error));
    }

    decodeURL(href: string){
        return CustomUriEncoder.decode(href);
    }
}