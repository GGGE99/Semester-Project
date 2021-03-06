import { allCurrencyURL } from "../utils/settings";
import React, { useState, useEffect } from "react";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";

export default function AllCurrency({ setCurrency, user }) {
  const [currencyArray, setCurrencyArray] = useState([]);
  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(allCurrencyURL, options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data);
        setCurrencyArray({ ...data.rates });
      });
  }, []);

  function onChange(evt) {
    setCurrency(evt.target.value);
  }

  return (
    <div className="mt-2">
      <select name="Currency" onChange={onChange}>
      {user.favCurrency !== "" ? (
          <option value="" selected>
            {user.favCurrency} - Your default
          </option>
        ) : (
          <option value="" selected>
            Choose currency
          </option>
        )}
        {Object.entries(currencyArray).map(([key, value]) => {
          return <option key={key}>{key}</option>;
        })}
      </select>
    </div>
  );
}
