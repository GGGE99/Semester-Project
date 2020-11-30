import React, { useState, useEffect } from "react";
import { allCoinsURL } from "../utils/settings";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import Table from "react-bootstrap/Table";
import { Col, Row } from "react-bootstrap";
import Currency from "./Currency";

export default function AllCoins() {
  const [coin, setCoin] = useState([]);
  const [currency, setCurrency] = useState("");

  useEffect(() => {
    if (currency !== "") {
      const options = makeOptions("GET", true);
      fetch(allCoinsURL + "/" + currency, options)
        .then(handleHttpErrors)
        .then((data) => {
          console.log(data);
          setCoin([...data]);
        });
    }
  }, [currency]);

  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(allCoinsURL, options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data);
        setCoin([...data]);
      });
  }, []);

  return (
    <Row>
      <Col>
        <Currency currency={currency} setCurrency={setCurrency} />
      </Col>
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
                  <td>
                    {p.price} {currency}
                  </td>
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
