import favFacade from "../facades/favoritesFacade"

export function favoriteCoinCurrency(fav) {

    favFacade
        .postFavorites(fav)
        .then(() => {})
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
