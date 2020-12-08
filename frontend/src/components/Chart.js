import { Line } from "react-chartjs-2";
import { Row, Col, Form, Button } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import { Typeahead } from "react-bootstrap-typeahead";
import "react-bootstrap-typeahead/css/Typeahead.css";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";
import { baseURL } from "../utils/settings";

export default function Charts() {
  const [coinNames, setCoinNames] = useState([]);
  const [charData, setCharData] = useState([{labels: "err"}]);
  const [multiSelections, setMultiSelections] = useState([]);
  const [chart, setChart] = useState({});


  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch(baseURL + "/api/chart/data", options)
      .then(handleHttpErrors)
      .then((data) => {
        setCharData([ ...data ]);
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
        setCharData([ ...data ]);
      });
  };


const colors = ["#96B615","#45C94C","#D7F23F","#555247","#E30D7D","#F03A65","#FBBC4F","#92784A","#8AFDC4","#5B768E","#38F725","#C217E5","#688D1C","#896763","#BCE327","#16F0D2","#694703","#D7B55C","#2D594C","#D09C3C","#35235F","#5C9A4A","#D11976","#5A3B48","#8191DC","#E11057","#0A3957","#4295B1","#C1B0CF","#95027E","#E5F054","#76FB04","#347C93","#D65972","#907A2E","#81482A","#D90D50","#28495E","#D352CC","#45CC0F","#A80E12","#9EC9B1","#9DAE7F","#D5327F","#EC5A85","#93EA2F","#CBE949","#BE75DF","#5A48CC"]

  useEffect(() => {
    let datasets = []
    console.log(charData[0].labels);

    let labels = charData[0].labels;


    charData.forEach((data, i) => {
      console.log(i)
      datasets.push({
        label: data.name,
        data: data.data,
        fill: false,
        lineTension: 0.5,
        backgroundColor: colors[i],
        borderColor: colors[i],
        borderWidth: 2,
      })
    });


    let newChart = {
      labels,
      datasets
    }

    setChart(newChart)
  }, [charData]);


  // const data1 = {
  //   labels: charData.labels,
  //   datasets: [
  //     {
  //       label: "My data",
  //       data: charData.data,
  //       fill: false,
  //       lineTension: 0.5,
  //       backgroundColor: "#112233",
  //       borderColor: "#112233",
  //       borderWidth: 2,
  //     },
  //   ],
  // };

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
        <Button className="btn btn-block" onClick={getChart}>
          Get chart
        </Button>
      </Col>
      <Col>
        <Line
          data={chart}
          options={{
            title: {
              display: true,
              text: charData.name || "Bitcoin",
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
