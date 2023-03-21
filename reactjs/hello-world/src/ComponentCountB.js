import React, { useState, useEffect } from 'react';
import useCount from './hook/useCount'

function ComponentCountB(props) {

    const {count, increaseCount} = useCount()

    return (
        <div>
            <p> You clicked {count} times</p>
            <button onClick={increaseCount}>Click</button>
        </div>
    )
}

export default ComponentCountB