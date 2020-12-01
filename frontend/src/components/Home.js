import React, { useState, useEffect } from "react";
import { allCoinsURL } from "../utils/settings";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import Table from "react-bootstrap/Table";
import { Button, Col, Row } from "react-bootstrap";
import Currency from "./Currency";

export default function AllCoins() {
  const [coin, setCoin] = useState([]);
  const [currency, setCurrency] = useState("");
  const [nameBool, setNameBool] = useState(false)
  const [priceBool, setPriceBool] = useState(false)
  const [volumeBool, setVolumeBool] = useState(false)

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

  function compare(a, b) {
    if (a < b) return 1;
    else if (a > b) return -1;
    return 0;
  }
  function compareReverse(a, b) {
    if (a < b) return -1;
    else if (a > b) return 1;
    return 0;
  }
  function sort(sortValue) {
    if (sortValue === "price" && !priceBool) {
      setPriceBool(true)
      setVolumeBool(false)
      setNameBool(false)
      setCoin([...coin.sort((a, b) => compare(a.price, b.price))]);
    }
    else if (sortValue === "price" && priceBool) {
      setPriceBool(false)
      setVolumeBool(false)
      setNameBool(false)
      setCoin([...coin.sort((a, b) => compareReverse(a.price, b.price))])
    }
    if (sortValue === "volume" && !volumeBool) {
      setPriceBool(false)
      setVolumeBool(true)
      setNameBool(false)
      setCoin([...coin.sort((a, b) => compare(a.volume, b.volume))]);
    }
    else if (sortValue === "volume" && volumeBool) {
      setPriceBool(false)
      setVolumeBool(false)
      setNameBool(false)
      setCoin([...coin.sort((a, b) => compareReverse(a.volume, b.volume))]);
    }
    if (sortValue === "name" && !nameBool){
      setPriceBool(false)
      setVolumeBool(false)
      setNameBool(true)
      setCoin([...coin.sort((a, b) => compare(b.name.toLowerCase(), a.name.toLowerCase())),]);
    }
    else if(sortValue === "name" && nameBool){
      setPriceBool(false)
      setVolumeBool(false)
      setNameBool(false)
      setCoin([...coin.sort((a, b) => compare(a.name.toLowerCase(), b.name.toLowerCase())),]);
    }
  }

  return (
    <Row>
      <Col>
        <Currency currency={currency} setCurrency={setCurrency} />
      </Col>
      <Col>
        <Table>
          <thead>
            <tr>
              <td onClick={() => sort("name")}>
                <Button className="btn btn-warning">Name</Button>
              </td>
              <td onClick={() => sort("price")}>
                <Button className="btn btn-warning">Price</Button>
              </td>
              <td onClick={() => sort("volume")}>
                <Button className="btn btn-warning">Volume</Button>
              </td>
              <td>Last Updated</td>
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
                  <td>{p.volume}</td>
                  <td>{p.lastUpdated}</td>
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
