import {Observable} from "rxjs/Rx";
import {Post} from "../post/post";
import {HalEntity} from "../hal.client/hal.entity";
/**
 * Created by yaroslav on 04.07.16.
 */

export class Address{
    street: string;
    suite: string;
    city: string;
    zip: string;
}


export class User implements HalEntity{
    id: number;
    name: string;
    email: string;
    address = new Address();

    posts: ()=>Observable<Post[]>;
}
