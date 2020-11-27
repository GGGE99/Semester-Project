import { allCurrencyURL } from "../utils/settings";
import React, { useState, useEffect } from "react";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";


export default function AllCurrency() {
  const [currency, setCurrency] = useState([]);
  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(allCurrencyURL, options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data.rates);
        setCurrency([{...data.rates}]);
      });
  }, []);

  return (
    <div>
     <select name="Currency">
        {Object.entries(currency).map(([key, value]) => {
          return <option key={key}>{key}</option>;
        })}
    </select>
    </div>
  );
}


