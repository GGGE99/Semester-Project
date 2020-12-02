import React, { useEffect, useState } from "react";
import Navbar from "./components/Navbar";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Container } from "react-bootstrap";
import { Jokes, Signup, Login, Home, Currency, Personal } from "./components";
import { getUserByJwt, setToken } from "./utils/token";
import { loginMethod, logoutMethode } from "./utils/loginUtils";
import { changePW } from "./utils/changePasswordUtils";
import Profile from "./components/Profile";

function App() {
  const init = { username: "", roles: [] };
  const [user, setUser] = useState({ ...init });
  const [fav, setFav] = useState({ favCoin: "", FavCurrency: "" });
  const login = (user, pass) => loginMethod(user, pass, setUser);
  const logout = () => logoutMethode(setUser, init);
  const changePassword = (oldPW, newPW) => changePW(oldPW, newPW);

  useEffect(() => {
    if (getUserByJwt()) {
      setUser(getUserByJwt());
    }
  }, []);

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
            <Route path="/profile">
              <Profile fav={fav} setFav={setFav} />
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
          </Container>
        </Switch>
      </Router>
    </>
  );
}

export default App;
