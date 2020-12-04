import { handleHttpErrors, makeOptions } from "../utils/fetchUtils";
import { favoritesURL as url } from "../utils/settings";

 function favoritesToDB() {
    const postFavorites = (fav) => {
        console.log(fav)
        const options = makeOptions("POST", true, 
            fav
        )
        return fetch(url, options).then(handleHttpErrors);
    }
    return { postFavorites };
}

const favFacade = favoritesToDB();
export default favFacade;