import {Injectable} from "@angular/core";
import {Http, Headers} from "@angular/http";
import {FacebookService, FacebookLoginResponse, FacebookInitParams} from "ng2-facebook-sdk/dist/index";



@Injectable()
export class LoginService{
    headers: Headers;

    constructor(private http: Http, private fb: FacebookService){
        this.headers = new Headers();
        this.headers.append('X-Forwarded-Host', location.host);

        let fbParams: FacebookInitParams = {
            appId: '216860685383630',
            version: 'v2.7'
        };
        this.fb.init(fbParams);
    }

    login(user: any){
        let headers = new Headers(this.headers.toJSON());
        user.grant_type = "password";
        //user.grant_type = "authorization_code";
        headers.append('Authorization', 'Basic '+btoa('any:'));
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.http.post("api/account-service/oauth/token",
            this.transformRequest(user),
            {headers: headers}
        ).subscribe(
            response=>{localStorage.setItem("auth_token", response.text())},
            response=>{console.log('Error '+response)});
    }

    loginFacebook(){
        /*let headers = new Headers(this.headers.toJSON());
        headers.append('X-Requested-With', 'XMLHttpRequest');
        this.http.get("api/account-service/login/facebook",
            {headers: headers}
        ).subscribe(
            response=>{console.log('Success '+response)},
            response=>{console.log('Error '+response)});*/

        this.fb.login().then(
            (response: FacebookLoginResponse) => {
                this.fb.api("/"+response.authResponse.userID).then(
                    (user: any) => {
                        var facebookData: any = response.authResponse;
                        facebookData.name = user.name;
                        this.exchangeFacebookTokenToAppToken(facebookData);
                    },
                    (error: any) => console.error(error)
                );
            },
            (error: any) => console.error(error)
        );
    }

    private exchangeFacebookTokenToAppToken(facebookData: any){
        let headers = new Headers(this.headers.toJSON());
        facebookData.grant_type = "facebook_token";
        //user.grant_type = "authorization_code";
        headers.append('Authorization', 'Basic '+btoa('any:'));
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.http.post("api/account-service/oauth/token",
            this.transformRequest(facebookData),
            {headers: headers}
        ).subscribe(
            response=>{localStorage.setItem("auth_token", response.text())},
            response=>{console.log('Error '+response)});
    }

    hello(){
        let headers = new Headers(this.headers.toJSON());
        if(localStorage.getItem("auth_token")){
            var token = JSON.parse(localStorage.getItem("auth_token")).access_token;
            headers.append('Authorization', `Bearer ${token}`);
        }
        headers.append('Content-Type', 'application/json');
        this.http.get("api/account-service/hello", {headers: headers}
        ).subscribe(
            response=>{console.log('Succes '+response.text())},
            response=>{console.log('Error '+response)});
    }

    private transformRequest(obj: any) {
        var str = [];
        for(var p in obj)
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        return str.join("&");
    }
}