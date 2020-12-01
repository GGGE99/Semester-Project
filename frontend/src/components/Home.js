import React, { useState, useEffect } from "react";
import { allCoinsURL } from "../utils/settings";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import Table from "react-bootstrap/Table";
import { Button, Col, Row } from "react-bootstrap";
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

   function compare(a,b){
      if(a < b) return 1
      else if(a > b) return -1
      return 0
  }

  function sort(sortValue){
    if(sortValue === "price")setCoin([...coin.sort((a,b) => compare(a.price, b.price))])
    else if(sortValue === "volume")setCoin([...coin.sort((a,b) => compare(a.volume, b.volume))])
    else if(sortValue === "name")setCoin([...coin.sort((a,b) => compare(a.name.toLowerCase(), b.name.toLowerCase()))])
    
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
             
              <td onClick={()=>sort("name")}><Button className="btn btn-warning">Name</Button></td>
              <td onClick={()=>sort("price")}><Button className="btn btn-warning">Price</Button></td>
              <td>Last Updated</td>
              <td onClick={()=>sort("volume")}><Button className="btn btn-warning">Volume</Button></td>
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
