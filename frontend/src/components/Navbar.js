import React from "react";
import { Link } from "react-router-dom";
import { Navbar, Nav } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import { Redirect } from "react-router";
import { useState } from "react";

function NavbarShow({ user, logout }) {
  const history = useHistory()

  return (
    <Navbar
      collapseOnSelect
      expand="lg"
      variant="dark"
      sticky="top"
      style={{ backgroundColor: "#1919E8" }}
    >
      <Link to="/" className="navbar-brand" style={{ color: "#FF8725" }}>
        BIT BANDEN <i className="fab fa-typo3" />
      </Link>
      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse id="responsive-navbar-nav">
        <Nav className="mr-auto">
          <Link to="/charts" className="nav-link">
            Charts
          </Link>

          <Link to="/converter" className="nav-link">
            Converter
          </Link>

          {localStorage.getItem("jwtToken") ? (
            <Link to="/profile" className="nav-link">
              Profile
            </Link>
          ) : (
            <></>
          )}

          {/* {localStorage.getItem("jwtToken") ? (
            <Link to="/settings" className="nav-link">
              Settings
            </Link>
          ) : (
              <></>
            )} */}
        </Nav>
        <Nav>
          {localStorage.getItem("jwtToken") ? (
            <>
              <h5 className="mr-2 text-light">{user.username}</h5>
              <button
                className="btn btn-danger"
                onClick={() => {
                  logout();
                  history.push("/")
                }}
              >
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
    </Navbar>
  );
}
export default NavbarShow;
