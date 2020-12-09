import React, { useState, useEffect } from "react";
import { allCoinsURL, baseURL } from "../utils/settings";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import Table from "react-bootstrap/Table";
import { Button, Col, Row } from "react-bootstrap";
import Currency from "./Currency";

export default function AllCoins({ user }) {
  const [coin, setCoin] = useState([]);
  const [fCoin, setFCoin] = useState([]);
  const [currency, setCurrency] = useState("");
  const [nameBool, setNameBool] = useState(false);
  const [priceBool, setPriceBool] = useState(false);
  const [volumeBool, setVolumeBool] = useState(false);


  useEffect(() => {
    if (currency === "") {
      setCurrency(user.favCurrency);
      const options = makeOptions("GET", true);
      fetch(allCoinsURL + "/" + user.favCurrency, options)
        .then(handleHttpErrors)
        .then((data) => {
          console.log(data);
          setCoin([...data]);
        });
    } else {
      const options = makeOptions("GET", true);
      fetch(allCoinsURL + "/" + currency, options)
        .then(handleHttpErrors)
        .then((data) => {
          console.log(data);
          setCoin([...data]);
        });
    }
  }, [currency, ], []);

  // useeffect til favorite coin table
  useEffect(() => {
    if (currency === "") {
      const options = makeOptions("GET", true);
      fetch(
        baseURL + "/api/crypto/" + user.favCoin + "/" + user.favCurrency,
        options
      )
        .then(handleHttpErrors)
        .then((data) => {
          console.log(data);
          setFCoin([{ ...data }]);
        });
    } else {
      const options = makeOptions("GET", true);
      fetch(
        baseURL + "/api/crypto/" + user.favCoin + "/" + currency,
        options
      )
        .then(handleHttpErrors)
        .then((data) => {
          console.log(data);
          setFCoin([{ ...data }]);
        });
    }
  }, [currency]);

/*   useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(allCoinsURL, options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data);
        setCoin([...data]);
      });
  }, []); */

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
      setPriceBool(true);
      setVolumeBool(false);
      setNameBool(false);
      setCoin([...coin.sort((a, b) => compare(a.price, b.price))]);
    } else if (sortValue === "price" && priceBool) {
      setPriceBool(false);
      setVolumeBool(false);
      setNameBool(false);
      setCoin([...coin.sort((a, b) => compareReverse(a.price, b.price))]);
    }
    if (sortValue === "volume" && !volumeBool) {
      setPriceBool(false);
      setVolumeBool(true);
      setNameBool(false);
      setCoin([...coin.sort((a, b) => compare(a.volume, b.volume))]);
    } else if (sortValue === "volume" && volumeBool) {
      setPriceBool(false);
      setVolumeBool(false);
      setNameBool(false);
      setCoin([...coin.sort((a, b) => compareReverse(a.volume, b.volume))]);
    }
    if (sortValue === "name" && !nameBool) {
      setPriceBool(false);
      setVolumeBool(false);
      setNameBool(true);
      setCoin([
        ...coin.sort((a, b) =>
          compare(b.name.toLowerCase(), a.name.toLowerCase())
        ),
      ]);
    } else if (sortValue === "name" && nameBool) {
      setPriceBool(false);
      setVolumeBool(false);
      setNameBool(false);
      setCoin([
        ...coin.sort((a, b) =>
          compare(a.name.toLowerCase(), b.name.toLowerCase())
        ),
      ]);
    }
  }

  return (
    <Row>
      <Col>
        <Currency currency={currency} setCurrency={setCurrency} user={user} />
      </Col>

      <Col>
        <h1>Favorite coin</h1>
        <Table>
          <thead>
            <tr>
              <td>
                <h4>Name</h4>
              </td>
              <td>
                <h4>Price</h4>
              </td>
              <td>
                <h4>Volume</h4>
              </td>
              <td>
                <h4>Last Update</h4>
              </td>
            </tr>
          </thead>
          <tbody>
            {fCoin.map((p) => {
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
      <Col>
        <h1>All coins</h1>
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
              <td>
                <h4>Last Update</h4>
              </td>
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
