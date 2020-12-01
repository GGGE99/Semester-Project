import facade from "../facades/changePasswordFacade"
import { getUserByJwt, setToken } from "./token";

export function changePW(oldPW, newPW) {

    facade
        .putNewPassword(oldPW, newPW)
        .then((res) => {
            res.json()
            
        })
        .catch((err) => {
            if (err.status) {
                err.fullError.then((e) => {
                    console.log(e.message);
                });
            } else {
                console.log("Network error");
            }
        });
};
