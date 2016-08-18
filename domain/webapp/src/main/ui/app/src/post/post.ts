import {Observable} from "rxjs/Rx";
import {HalEntity} from "../hal.client/hal.entity";
/**
 * Created by yaroslav on 11.07.16.
 */




export class Comment{
    name: string;
    body: string;
}

export class Post implements HalEntity{
    static pages = 3;
    title: string;
    body: string;

    comments: ()=>Observable<Comment[]>;
}