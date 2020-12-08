import React from "react";
import { Link } from "react-router-dom";
import { Navbar, Nav } from "react-bootstrap";
import { useHistory } from "react-router-dom";

function NavbarShow({ user, logout }) {
  const history = useHistory();
  return (
    <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark" sticky="top">
      <Link to="/"  className="navbar-brand">
        BIT BANDEN <i className="fab fa-typo3" />
      </Link>
      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse id="responsive-navbar-nav">
        <Nav className="mr-auto">
          <Link to="/jokes" className="nav-link">
            Jokes
          </Link>

          <Link to="/charts" className="nav-link">
            Charts
          </Link>

          {localStorage.getItem("jwtToken") ? (
            <Link to="/profile" className="nav-link">
              Profile
            </Link>
          ) : (
              <></>
            )}

          {localStorage.getItem("jwtToken") ? (
            <Link to="/settings" className="nav-link">
              Settings
            </Link>
          ) : (
              <></>
            )}
        </Nav>
        <Nav>
          {localStorage.getItem("jwtToken") ? (
            <>
              <h5 className="mr-2 text-light">{user.username}</h5>
              <button className="btn btn-danger" onClick={() => {
                let path = ""
                logout()
                history.push(path)
              }
              }>
                Logout
              </button>
            </>
          ) : (
              <Link to="/signin">
                <button className="btn btn-primary">Sign In</button>
              </Link>
            )}
        </Nav>
      </Navbar.Collapse>
    </Navbar >
  );
}
export default NavbarShow;
