import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import './index.css';
// import  Game  from "./tic_tac_toe.js"
// import FavoriteColor from "./use_state_1"
// import {Garage} from "./car"
// import { Goal } from './goal';
// import { MyForm } from './forms';
import Layout from "./pages/layout";
import Home from "./pages/home";
import Blogs from "./pages/blogs";
import Contact from "./pages/contact";
import NoPage from "./pages/nopage";
import './pages/app.css';


export default function App() {
    return (
        <>
         <h2>Hello</h2>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<Home />} />
                    <Route path="blogs" element={<Blogs />} />
                    <Route path="contact" element={<Contact />} />
                    <Route path="*" element={<NoPage />} />
                </Route>
            </Routes>
        </BrowserRouter>
        </>
    );
  }

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);


