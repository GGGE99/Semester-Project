import React, { useState, useEffect } from "react";
import { allCoinsURL, allCurrencyURL } from "../utils/settings";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import { Button } from "react-bootstrap";

export default function Profile({ setFav }) {
  const [coinArray, setCoinArray] = useState([]);
  const [currencyArray, setCurrencyArray] = useState([]);
  const [favCoin, setFavCoin] = useState([]);
  const [favCurrency, setFavCurrency] = useState([]);

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
        console.log(data);
        setCurrencyArray({ ...data.rates });
      });
  }, []);

  function onChangeCoin(evt) {
    setFavCoin(evt.target.value);
  }

  function onChangeCurrency(evt) {
    setFavCurrency(evt.target.value);
  }

  function onSave() {
    setFav({ favCoin, favCurrency });
  }

  return (
    <div>
      profile pictøre
      <hr />
      Here change favorite cryptocoin plz: dropdown menu Her
      <div>
        <select name="Coin" onChange={onChangeCoin}>
          {Object.entries(coinArray).map(([key, value]) => {
            return <option key={key}>{value.name}</option>;
          })}
        </select>
      </div>
      <hr />
      change default currency pls: dropdown menu
      <div>
        <select name="Currency" onChange={onChangeCurrency}>
          {Object.entries(currencyArray).map(([key, value]) => {
            return <option key={key}>{key}</option>;
          })}
        </select>
      </div>
      <hr />
      Save button here pløøz
      <Button onClick={onSave}> Save </Button>
    </div>
  );
}
