import favFacade from "../facades/favoritesFacade"

export function favoriteCoinCurrency(fav) {

    favFacade
        .postFavorites(fav)
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
