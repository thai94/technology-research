import React from 'react';

function Car(props) {
    return <h2>I am a {props.brand}!</h2>
}

function Garage() {
    const car = {model: "Ford", name:"my name"}
    const cars = ['Ford', 'BMW', 'Audi']
    const carClick = (name) => {
        alert("Hello world" + name)
    }
    return (
        <>
            <h1>Who lives in my garage?</h1>
            <ul>
                {cars.map((car) => <Car key={car} brand={car}></Car>)}
            </ul>
            <button onClick={() => carClick("Thai")}>Hello</button>
        </>
    )
}

export {Car, Garage} 