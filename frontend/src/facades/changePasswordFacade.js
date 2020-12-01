import { handleHttpErrors, makeOptions } from "../utils/fetchUtils";
import { passwordURL as url } from "../utils/settings";

function changePassword() {
    const putNewPassword = (oldPassword, newPassword) => {
        const options = makeOptions("PUT", true, {
            oldPW: oldPassword,
            newPW: newPassword
        });
        return fetch(url, options).then(handleHttpErrors);
    };
    return { putNewPassword };
}


const facade = changePassword();
export default facade;