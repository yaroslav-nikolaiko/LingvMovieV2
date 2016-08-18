export class CustomUriEncoder{
    static encode(text: string){
        return text.split(":").join("!d!")
            .split("/").join("!s!");
    }

    static decode(text: string){
        return text.split("!d!").join(":")
            .split("!s!").join("/");
    }
}