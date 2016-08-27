/**
 * Created by yaroslav on 27.08.16.
 */
export class CookieUtils{
    static saveAuthTokenCookieToLocalStorage(){
        var auth_token = CookieUtils.getCookie("auth_token");
        auth_token = auth_token.replace(/['"]+/g, '');
        console.log("Here goes auth_token = "+ atob(auth_token));
        if(auth_token)
            localStorage.setItem("auth_token", atob(auth_token));
    }

    static getCookie(name: string) {
        let ca: Array<string> = document.cookie.split(';');
        let caLen: number = ca.length;
        let cookieName = name + "=";
        let c: string;

        for (let i: number = 0; i < caLen; i += 1) {
            c = ca[i].replace(/^\s\+/g, "");
            if (c.indexOf(cookieName) == 0) {
                return c.substring(cookieName.length, c.length);
            }
        }
        return "";
    }

    static deleteCookie(name) {
        this.setCookie(name, "", -1);
    }

    static setCookie(name: string, value: string, expireDays: number, path: string = "") {
        let d:Date = new Date();
        d.setTime(d.getTime() + expireDays * 24 * 60 * 60 * 1000);
        let expires:string = "expires=" + d.toUTCString();
        document.cookie = name + "=" + value + "; " + expires + (path.length > 0 ? "; path=" + path : "");
    }

}