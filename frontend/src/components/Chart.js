import { Line } from "react-chartjs-2";
import { Row, Col, Form, Button } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import { Typeahead } from "react-bootstrap-typeahead";
import "react-bootstrap-typeahead/css/Typeahead.css";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import { baseURL } from "../utils/settings";

export default function Charts() {
  const [coinNames, setCoinNames] = useState([]);
  const [data, setData] = useState({ labels: [], data: [], name: "" });
  const [multiSelections, setMultiSelections] = useState([]);

  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(baseURL + "/api/chart/data", options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data);
        setData({ ...data });
      });

    fetch(baseURL + "/api/crypto/every/name", options)
      .then(handleHttpErrors)
      .then((data) => {
        setCoinNames([...data.sort()]);
      });
  }, []);

  const getChart = () => {
    const options = makeOptions("GET", true);
    fetch(baseURL + "/api/chart/" + multiSelections, options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data);
        setData({ ...data });
      });
  }

  const data1 = {
    labels: data.labels,
    datasets: [
      {
        label: "My data",
        data: data.data,
        fill: false,
        lineTension: 0.5,
        backgroundColor: "#112233",
        borderColor: "#112233",
        borderWidth: 2,
      },
    ],
  };

  return (
    <Row>
      <Col>
        <Form.Group>
          <Form.Label>Single Selection</Form.Label>
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
        <Button className="btn btn-block" onClick={getChart}>Get chart</Button>
      </Col>
      <Col>
        <Line
          data={data1}
          options={{
            title: {
              display: true,
              text: data.name || "Bitcoin",
              fontSize: 20,
            },
            legend: {
              display: false,
              position: "right",
            },
          }}
        />
      </Col>
    </Row>
  );
}
