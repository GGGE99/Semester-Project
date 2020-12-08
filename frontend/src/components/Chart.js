import { Line } from "react-chartjs-2";
import {
  Row,
  Col,
  Form,
  Button,
  Jumbotron,
  Table,
  Spinner,
  Nav,
  Navbar,
} from "react-bootstrap";
import React, { useState, useEffect } from "react";
import { Typeahead } from "react-bootstrap-typeahead";
import "react-bootstrap-typeahead/css/Typeahead.css";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import { baseURL } from "../utils/settings";

export default function Charts() {
  const [coinNames, setCoinNames] = useState([]);
  const [charData, setCharData] = useState([{ labels: "err" }]);
  const [multiSelections, setMultiSelections] = useState([]);
  const [chart, setChart] = useState({});
  const [tableCoin, setTableCoin] = useState({});
  const [tableCoinHistory, setTableCoinHistory] = useState({});

  useEffect(() => {
    if (tableCoin) {
      const options = makeOptions("GET", true);
      fetch(baseURL + "/api/chart/" + tableCoin.name, options)
        .then(handleHttpErrors)
        .then((data) => {
          setTableCoinHistory({...data[0]});
        });
    }
  }, [tableCoin]);
  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(baseURL + "/api/chart/data", options)
      .then(handleHttpErrors)
      .then((data) => {
        setCharData([...data]);
      });

    fetch(baseURL + "/api/crypto/every", options)
      .then(handleHttpErrors)
      .then((data) => {
        setCoinNames([
          ...data.sort((a, b) => {
            let aName = a.name;
            let bName = b.name;

            if (aName.toLowerCase() > bName.toLowerCase()) return 1;
            else if (aName.toLowerCase() < bName.toLowerCase()) return -1;
            return 0;
          }),
        ]);
      });
  }, []);

  const getChart = () => {
    let names = multiSelections.map((coin) => coin.name);
    const options = makeOptions("GET", true);
    fetch(baseURL + "/api/chart/" + names, options)
      .then(handleHttpErrors)
      .then((data) => {
        setCharData([...data]);
      });
  };

  const colors = [
    "#96B615",
    "#45C94C",
    "#D7F23F",
    "#555247",
    "#E30D7D",
    "#F03A65",
    "#FBBC4F",
    "#92784A",
    "#8AFDC4",
    "#5B768E",
    "#38F725",
    "#C217E5",
    "#688D1C",
    "#896763",
    "#BCE327",
    "#16F0D2",
    "#694703",
    "#D7B55C",
    "#2D594C",
    "#D09C3C",
    "#35235F",
    "#5C9A4A",
    "#D11976",
    "#5A3B48",
    "#8191DC",
    "#E11057",
    "#0A3957",
    "#4295B1",
    "#C1B0CF",
    "#95027E",
    "#E5F054",
    "#76FB04",
    "#347C93",
    "#D65972",
    "#907A2E",
    "#81482A",
    "#D90D50",
    "#28495E",
    "#D352CC",
    "#45CC0F",
    "#A80E12",
    "#9EC9B1",
    "#9DAE7F",
    "#D5327F",
    "#EC5A85",
    "#93EA2F",
    "#CBE949",
    "#BE75DF",
    "#5A48CC",
  ];

  useEffect(() => {
    let datasets = [];

    let labels = charData[0].labels;

    charData.forEach((data, i) => {
      datasets.push({
        label: data.name,
        data: data.data,
        fill: false,
        lineTension: 0.5,
        backgroundColor: colors[i],
        borderColor: colors[i],
        borderWidth: 2,
      });
    });

    let newChart = {
      labels,
      datasets,
    };

    setChart(newChart);
  }, [charData]);

  return (
    <Row>
      <Col md={8}>
        <Row className="w-100" style={{ position: "fixed" }}>
          <Col md={4}>
            {multiSelections.map((coin) => {
              return (
                <Jumbotron className="m-2">
                  <p>Name: {coin.name} </p>
                  <p>Rank: {coin.rank} </p>
                  <p>Price: {coin.price} </p>
                  <p>Volume:{coin.volume} </p>
                  <p>Currency: {coin.currency} </p>
                  <p>LastUpdate: {coin.lastUpdated} </p>
                </Jumbotron>
              );
            })}
          </Col>
          <Col md={4}>
            {!coinNames[0] && (
              <div className="text-center m-2">
                <Spinner animation="border" variant="primary" />
                <Spinner animation="border" variant="secondary" />
                <Spinner animation="border" variant="success" />
                <Spinner animation="border" variant="danger" />
                <Spinner animation="border" variant="warning" />
                <Spinner animation="border" variant="info" />
                <Spinner animation="border" variant="light" />
                <Spinner animation="border" variant="dark" />
                <Spinner animation="grow" variant="primary" />
                <Spinner animation="grow" variant="secondary" />
                <Spinner animation="grow" variant="success" />
                <Spinner animation="grow" variant="danger" />
                <Spinner animation="grow" variant="warning" />
                <Spinner animation="grow" variant="info" />
                <Spinner animation="grow" variant="light" />
                <Spinner animation="grow" variant="dark" />
              </div>
            )}

            <Form.Group>
              <Form.Label>Choose your coins</Form.Label>
              <Typeahead
                id="basic-typeahead-single"
                labelKey="name"
                multiple
                onChange={setMultiSelections}
                options={coinNames}
                placeholder="Choose a coin"
                selected={multiSelections}
              />
            </Form.Group>
            <Button className="btn btn-block" onClick={getChart}>
              Get chart
            </Button>
            <Line
              data={chart}
              options={{
                title: {
                  display: true,
                  text:
                    charData.map((data) => data.name).toString() || "Unknown",
                  fontSize: 20,
                },
                legend: {
                  display: false,
                  position: "right",
                },
              }}
            />
            {tableCoinHistory.labels && (
              <Table striped bordered hover>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Price</th>
                  </tr>
                </thead>
                <tbody>
                  {tableCoinHistory.labels.map((label, i) => {
                    return (
                      <tr>
                        <td>{label}</td>
                        <td>{tableCoinHistory.data[i]}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </Table>
            )}
          </Col>
        </Row>
      </Col>
      <Col md={4}>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Rank</th>
              <th>Name</th>
              <th>Price</th>
              <th>Currency</th>
            </tr>
          </thead>
          <tbody>
            {coinNames.map((coin) => {
              return (
                <tr
                  onClick={() => {
                    setMultiSelections([...multiSelections, coin]);
                    setTableCoin({ ...coin });
                  }}
                >
                  <td>{coin.rank}</td>
                  <td>{coin.name}</td>
                  <td>{coin.price}</td>
                  <td>{coin.currency}</td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </Col>
    </Row>
  );
}
