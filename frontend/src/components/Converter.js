import {allCoinsURL, allCurrencyURL} from "../utils/settings"
import React, { useEffect, useState } from "react";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import { Button } from "react-bootstrap";
import Currency from "./Currency"


export default function Convert(){
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
          console.log(data)
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

    const convert= () =>{
      const convertedResult = coin * currency * amount;
      setResult(convertedResult);
    }
    
    
    return(
        <div>
            <select name="Coin" onChange={onChangeCoin}>
          {coinArray.map((value) => {
            return <option value={value.price}key={value.name}>{value.name}</option>;
          })}
          </select>
          <select name="Currency"onChange={onChangeCurrency}> 
          {Object.entries(currencyArray).map(([key, value]) => {
            return <option value ={value}key={key}>{key}</option>;
          })}
        </select>
            <input type="text" onChange={onChangeAmount}></input>
          <Button onClick={convert} className="btn btn-warning">Convert</Button>
        <p>{result}</p>
        </div>
    )
}