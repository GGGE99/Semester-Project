import React, { useEffect, useState } from "react";
import Navbar from "./components/Navbar";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Container } from "react-bootstrap";
import { Jokes, Signup, Login, Home, Charts, Personal } from "./components";
import { getUserByJwt, setToken } from "./utils/token";
import { loginMethod, logoutMethode } from "./utils/loginUtils";
import { changePW } from "./utils/changePasswordUtils";
import Profile from "./components/Profile";
import { favoriteCoinCurrency } from "./utils/favorites";
import Converter from "./components/Converter";

function App() {
  const init = { username: "", roles: [], favCoin: "", favCurrency: "" };
  const [user, setUser] = useState({ ...init });
  const [fav, setFav] = useState({ favCoin: "", favCurrency: "" });
  const login = (user, pass) => loginMethod(user, pass, setUser);
  const logout = () => logoutMethode(setUser, init);
  const changePassword = (oldPW, newPW) => changePW(oldPW, newPW);

  useEffect(() => {
    if (getUserByJwt()) {
      setUser(getUserByJwt());
    }
  }, []);

  useEffect(() => {
    favoriteCoinCurrency(fav);
  }, [fav]);

  return (
    <>
      <Router>
        <Navbar user={user} logout={logout} />
        <Switch>
          <Container fluid>
            <Route path="/" exact>
              <Home fav={fav} setFavCoin={setFav} />
            </Route>
            <Route path="/jokes">
              <Jokes />
            </Route>
            <Route path="/charts">
              <Charts />
            </Route>
            <Route path="/profile">
              <Profile setFav={setFav} />
            </Route>
            <Route path="/settings">
              <Personal changePW={changePassword} user={user} />
            </Route>
            <Route path="/products" />
            <Route path="/signin">
              <Login login={login} user={user} logout={logout} />
            </Route>
            <Route path="/signup">
              <Signup />
            </Route>
            <Route path="/converter">
              <Converter  />
            </Route>
          </Container>
        </Switch>
      </Router>
    </>
  );
}

export default App;
