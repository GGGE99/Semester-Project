import React, { useState } from "react";
import { Jumbotron, Row, Col, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import facade from "../facades/LoginFacade";
import { getUserByJwt, setToken } from "../utils/token";
import { Redirect } from "react-router";

function LoginDisplay({ user, setUser }) {
  const init = { username: "", password: "" };
  const [loginCredentials, setLoginCredentials] = useState(init);
  const [err, setErr] = useState("");

  const performLogin = (evt) => {
    evt.preventDefault();
    login(loginCredentials.username, loginCredentials.password, setUser);
  };
  const onChange = (evt) => {
    setLoginCredentials({
      ...loginCredentials,
      [evt.target.id]: evt.target.value,
    });
  };

  const login = (user, pass, setUser) => {
    facade
      .login(user, pass)
      .then((res) => {
        setToken(res.token);
        setUser({ ...getUserByJwt() });
      })
      .catch((err) => {
        if (err.status) {
          err.fullError.then((e) => {
            setErr(e.message);
          });
        } else {
          setErr("Network error");
        }
      });
  };
  return (
    <Row>
      <Col></Col>
      <Col>
        <Jumbotron className="mt-2 text-center">
          {!user.username ? (
            <>
              <Form.Group controlId="formBasicEmail">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  id="username"
                  type="name"
                  placeholder="Enter name"
                  onChange={onChange}
                />
                <Form.Label>Password</Form.Label>
                <Form.Control
                  id="password"
                  type="Password"
                  placeholder="Enter Password"
                  onChange={onChange}
                />

                <button className="btn btn-primary m-2" onClick={performLogin}>
                  login
                </button>
              </Form.Group>
              {err && <p className="text-danger">{err}</p>}

              <Link to="/signup">Sign-up</Link>
            </>
          ) : (
            <Redirect to="/" />
          )}
        </Jumbotron>
      </Col>
      <Col></Col>
    </Row>
  );
}

export default LoginDisplay;
