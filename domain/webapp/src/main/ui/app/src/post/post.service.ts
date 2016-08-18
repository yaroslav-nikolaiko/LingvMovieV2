import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Rx";
import {Post, Comment} from "./post";
import {PagingEntity} from "../hal.client/paging.entity";
import {User} from "../user/user";
import {HalClient} from "../hal.client/hal.client";
import {HalOptions} from "../hal.client/hal.options";
import {HalEntity} from "../hal.client/hal.entity";

@Injectable()
export class PostService {

    constructor(private halClient: HalClient){

    }

    getPosts(): Observable<PagingEntity<Post>> {
        let options: HalOptions = {
            params:{
                size: 2
            }
        };
        return this.halClient.getList('posts', options);
    }

    getByUser(user: HalEntity){
        let options: HalOptions = {
            search: "byAccount",
            params:{
                size: 2,
                href: user._links['self'].href
            }
        };
        return this.halClient.getList('posts', options);
    }
}