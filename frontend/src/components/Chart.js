import { Line } from "react-chartjs-2";
import { Row } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import { makeOptions, handleHttpErrors } from "../utils/fetchUtils";

export default function Charts() {
  const [data, setData] = useState({labels: [], data: []});

  useEffect(() => {
    const options = makeOptions("GET", true);
    fetch("http://localhost:8080/jpareststarter/api/chart/data", options)
      .then(handleHttpErrors)
      .then((data) => {
        console.log(data);
        setData({...data});
      });
  });

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
      <Line
        data={data1}
        options={{
          title: {
            display: true,
            text: "Bitcoin",
            fontSize: 20,
          },
          legend: {
            display: false,
            position: "right",
          },
        }}
      />
    </Row>
  );
}
