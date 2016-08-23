"use strict";
// ************************************************************************
var CanDeactivateUserForm = (function () {
    function CanDeactivateUserForm() {
    }
    CanDeactivateUserForm.prototype.canDeactivate = function (component, route, state) {
        if (component.form.dirty && !component.canDeactivate)
            return confirm("Are you sure?");
        return true;
    };
    return CanDeactivateUserForm;
}());
exports.CanDeactivateUserForm = CanDeactivateUserForm;
//# sourceMappingURL=can.deacrivate.user.form.js.map