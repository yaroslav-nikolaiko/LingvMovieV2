import {Injectable} from "@angular/core";
import {Http, Headers} from "@angular/http";



@Injectable()
export class LoginService{
    headers: Headers;

    constructor(private http: Http){
        this.headers = new Headers();
        this.headers.append('X-Forwarded-Host', location.host);
    }

    login(user: any){
        let headers = new Headers(this.headers.toJSON());
        //user.grant_type = "password";
        user.grant_type = "authorization_code";
        headers.append('Authorization', 'Basic '+btoa('any:'));
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.http.post("api/oauth/token",
            this.transformRequest(user),
            {headers: headers}
        ).subscribe(
            response=>{localStorage.setItem("auth_token", response.text())},
            response=>{console.log('Error '+response)});
    }

    loginFacebook(){
        let headers = new Headers(this.headers.toJSON());
        headers.append('X-Requested-With', 'XMLHttpRequest');
        this.http.get("api/login/facebook",
            {headers: headers}
        ).subscribe(
            response=>{console.log('Success '+response)},
            response=>{console.log('Error '+response)});
    }

    hello(){
        let headers = new Headers(this.headers.toJSON());
        var token = JSON.parse(localStorage.getItem("auth_token")).access_token;
        //headers.append('Authorization', `Bearer ${token}`);
        headers.append('Content-Type', 'application/json');
        this.http.get("api/hello", {headers: headers}
        ).subscribe(
            response=>{console.log('Succes '+response)},
            response=>{console.log('Error '+response)});
    }

    private transformRequest(obj: any) {
        var str = [];
        for(var p in obj)
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        return str.join("&");
    }
}