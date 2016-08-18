import {Injectable} from "@angular/core";
import {User} from "./user";
import {Observable} from "rxjs/Rx";
import {Response} from "@angular/http";
import {HalClient} from "../hal.client/hal.client";
import {HalOptions} from "../hal.client/hal.options";
import {HalEntity} from "../hal.client/hal.entity";


@Injectable()
export class UserService{

    constructor(private halClient: HalClient){

    }

    getUsers(): Observable<User[]>{
        return this.halClient.getList('accounts');
    }

    get(href: string): Observable<User>{
        return this.halClient.get(href);
    }

    getByComment(comment: HalEntity): Observable<User>{
        let options:HalOptions = {
            search: "byComment",
            params:{
                href: comment._links['self'].href
            }
        };
        return this.halClient.getList("accounts", options);
    }


    save(user: User){
        if(user['_links'])
            return this.halClient.update(user);
        else
            return this.halClient.save('accounts', user) ;
    }
    
    delete(user: User): Observable<Response>{
        return this.halClient.delete(user);
    }
}