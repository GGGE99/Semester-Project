import React, { useState, useEffect } from "react";
import { allCoinsURL } from "../utils/settings";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import Table from "react-bootstrap/Table";
import { Col, Row } from "react-bootstrap";

export default function AllCoins() {
  const [coin, setCoin] = useState([]);
  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(allCoinsURL, options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data.all);
        setCoin([...data]);
      });
  }, []);

  return (
    <Row>
      <Col></Col>
      <Col>
        <Table>
          <thead>
            <tr>
              <td>Name</td>
              <td>Price</td>
              <td>Last Updated</td>
              <td>Volume</td>
            </tr>
          </thead>
          <tbody>
            {coin.map((p) => {
              return (
                <tr key={p.name}>
                  <td>{p.name}</td>
                  <td>{p.price}</td>
                  <td>{p.lastUpdated}</td>
                  <td>{p.volume}</td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </Col>
      <Col></Col>
    </Row>
  );
}
