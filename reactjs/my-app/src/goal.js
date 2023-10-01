import React from 'react';

function  MissedGoal() {
    return <h1>MISSED!</h1>;
}

function MadeGoal(props) {
    return (
        <>
            {
                props.flag && <h1>Goal!</h1>
            }
        </>
    );
}

function Goal(props) {
    const isGoal = props.isGoal
    if (isGoal) {
        return <MadeGoal flag={true}/>
    }
    return <MissedGoal />
}

export {Goal}