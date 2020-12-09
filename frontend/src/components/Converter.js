import { allCoinsURL, allCurrencyURL } from "../utils/settings";
import React, { useEffect, useState } from "react";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import Currency from "./Currency";
import { Row, Col, Form, Button, Jumbotron, Spinner } from "react-bootstrap";

export default function Convert() {
  const [coinArray, setCoinArray] = useState([]);
  const [currencyArray, setCurrencyArray] = useState([]);
  const [coin, setCoin] = useState("");
  const [currency, setCurrency] = useState("");
  const [amount, setAmount] = useState("");
  const [result, setResult] = useState("");

  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(allCoinsURL, options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data);
        setCoinArray([...data]);
      });

    fetch(allCurrencyURL, options)
      .then(handleHttpErrors)
      .then((data) => {
        setCurrencyArray({ ...data.rates });
      });
  }, []);

  function onChangeCoin(evt) {
    setCoin(evt.target.value);
  }

  function onChangeCurrency(evt) {
    setCurrency(evt.target.value);
  }

  function onChangeAmount(evt) {
    setAmount(evt.target.value);
  }

  const convert = () => {
    const convertedResult = coin * currency * amount;
    setResult(convertedResult);
  };

  return (
    <Row>
      <Col md={2}></Col>
      <Col md={8}>
        <Jumbotron className="text-center mt-1">
          <Row>
            <Col md={10} className="m-0">
              <Form.Control as="select" custom onChange={onChangeCoin}>
                {coinArray.map((value) => {
                  return (
                    <option value={value.price} key={value.name}>
                      {value.name}
                    </option>
                  );
                })}
              </Form.Control>
            </Col>
            <Col md={2} className="m-0">
              <Form.Control as="select" custom onChange={onChangeCurrency}>
                {Object.entries(currencyArray).map(([key, value]) => {
                  return (
                    <option value={value} key={key}>
                      {key}
                    </option>
                  );
                })}
              </Form.Control>
            </Col>
          </Row>
          <br />
          <Form.Control
            type="number"
            placeholder="Amount"
            onChange={onChangeAmount}
          />
          <br />
          <Button onClick={convert} className="btn btn-warning btn-block mb-1">
            Convert
          </Button>
          <Form.Control
            type="text"
            value={result}
            onChange={onChangeAmount}
            className="text-center"
            style={{backgroundColor: "#ffde7a"}}
          />
        </Jumbotron>
      </Col>
      <Col md={2}></Col>
    </Row>
  );
}
