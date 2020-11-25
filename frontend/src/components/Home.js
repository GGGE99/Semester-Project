import React, { useState, useEffect } from "react";
import { allCoinsURL } from "../utils/settings";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils"
import Table from 'react-bootstrap/Table';





export default function AllCoins() {
    const [coin, setCoin] = useState([])
    useEffect(() => {
        const options = makeOptions("GET", true);
        fetch(allCoinsURL, options)
            .then(handleHttpErrors)
            .then((data) => {
                console.log(data.all)
                setCoin([...data])


            })
    }, [])


    return (
        <div>
          <Table>
                <thead><tr><td>Name</td><td>Price</td><td>Last Updated</td><td>volume</td></tr></thead>
                <tbody>{coin.map((p) => {
                    return (
                    <tr key={p.name}><td>{p.name}</td><td>{p.price}</td><td>{p.lastUpdated}</td><td>{p.volume}</td></tr>
                    )
                })}</tbody>
            </Table>

        </div>
    )
}
            