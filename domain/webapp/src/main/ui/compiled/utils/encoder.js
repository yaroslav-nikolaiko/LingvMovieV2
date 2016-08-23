"use strict";
var CustomUriEncoder = (function () {
    function CustomUriEncoder() {
    }
    CustomUriEncoder.encode = function (text) {
        return text.split(":").join("!d!")
            .split("/").join("!s!");
    };
    CustomUriEncoder.decode = function (text) {
        return text.split("!d!").join(":")
            .split("!s!").join("/");
    };
    return CustomUriEncoder;
}());
exports.CustomUriEncoder = CustomUriEncoder;
//# sourceMappingURL=encoder.js.map