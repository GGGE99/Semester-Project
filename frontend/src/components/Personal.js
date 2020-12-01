import { Jumbotron, Row, Col, Form } from "react-bootstrap";
import React, { useState } from "react";


export default function Personal({ user, changePW }) {
    const username = user.username
    const init = { oldPW: "", newPW: "", newPW2: "" }
    const [changedPW, setChangedPW] = useState(init)
    const [errorMSG, setErrorMSG] = useState("")

    const changePassword = (evt) => {
        evt.preventDefault()
        if (!changedPW.newPW.match(changedPW.newPW2)) { setErrorMSG("please make sure the new passwords match")}
        else { changePW(changedPW.oldPW, changedPW.newPW) }
    }
    const onChange = (evt) => {
        setChangedPW({
            ...changedPW,
            [evt.target.id]: evt.target.value,
        });

        console.log(changedPW)
    };

    return (
        <div className="text-center">
            <h1>Welcome {username}</h1>
            <Row>
                <Col></Col>
                <Col>
                    <Jumbotron>
                        <Form.Group onChange={onChange}>
                            <Form.Label>Old Password</Form.Label>
                            <Form.Control
                                id="oldPW"
                                type="Password"
                                placeholder="Type old password"
                            />
                            <Form.Label>New Password</Form.Label>
                            <Form.Control
                                id="newPW"
                                type="Password"
                                placeholder="Type New Password"
                            />
                            <Form.Control
                                id="newPW2"
                                type="Password"
                                placeholder="Type New Password Again"
                            />
                            <div>{errorMSG}</div>
                            <button className="btn btn-primary m-2" onClick={changePassword}>
                                Change password
                </button>
                        </Form.Group>
                    </Jumbotron>
                </Col>
                <Col></Col>
            </Row>
        </div>
    )
}