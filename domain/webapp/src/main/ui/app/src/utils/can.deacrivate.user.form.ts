import {CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {Observable} from "rxjs/Rx";
import {UserFormComponent} from "../user/user.form.component";



// ************************************************************************
export class CanDeactivateUserForm implements CanDeactivate<UserFormComponent> {
    canDeactivate(component:UserFormComponent, route:ActivatedRouteSnapshot, state:RouterStateSnapshot):Observable<boolean>|boolean {
        if(component.form.dirty && ! component.canDeactivate)
            return confirm("Are you sure?");
        return true;
    }
}