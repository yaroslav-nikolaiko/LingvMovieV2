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
        user.grant_type = "password";
        headers.append('Authorization', 'Basic '+btoa('any:'));
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.http.post("api/oauth/token",
            this.transformRequest(user),
            {
                headers: headers
            }
        ).subscribe(
            response=>{localStorage.setItem("auth_token", response.text())},
            response=>{console.log('Error '+response)});
    }

    hello(){
        let headers = new Headers(this.headers.toJSON());
        headers.append('Authorization', 'Bearer '+JSON.parse(localStorage.getItem("auth_token")).access_token);
        headers.append('Content-Type', 'application/json');
        this.http.get("api/hello",
            {
                headers: headers
            }
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