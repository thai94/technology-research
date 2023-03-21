import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Link, browserHistory, IndexRoute, useParams } from 'react-router'

function Home(props) {
   let { id } = useParams();

   return (
      <div>
         <h1>Home... {id}</h1>
      </div>
   )
}

 export default Home;