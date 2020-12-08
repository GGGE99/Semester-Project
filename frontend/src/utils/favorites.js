import favFacade from "../facades/favoritesFacade"
import { updateTokenURL } from "./settings"
import { handleHttpErrors, makeOptions } from "./fetchUtils";
import { getUserByJwt, setToken } from "./token";

export function favoriteCoinCurrency(fav, setUser) {

    favFacade
        .postFavorites(fav)
        .then(() => {
            const options = makeOptions("GET", true);
            fetch(updateTokenURL, options)
                .then(handleHttpErrors)
                .then((res) => {
                    setToken(res.token);
                    setUser({ ...getUserByJwt() });
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
