import { useState, createContext, useContext, useEffect, useReducer , memo, useCallback } from "react";
import Todos from "./todo";

const UserContext = createContext();


function Component1() {
    const [user, setUser] = useState("Jesse Hall");
    return (
        <UserContext.Provider value={user}>
          <h1>{`Hello ${user}!`}</h1>
          <Component2 />
        </UserContext.Provider>
      );
}


function Component2() {
    return (
      <>
        <h1>Component 2</h1>
        <Component3 />
      </>
    );
}

function Component3() {
    return (
      <>
        <h1>Component 3</h1>
        <Component4 />
      </>
    );
  }

  function Component4() {
    return (
      <>
        <h1>Component 4</h1>
        <Component5 />
      </>
    );
  }

  function Component5() {
    const user = useContext(UserContext);
  
    return (
      <>
        <h1>Component 5</h1>
        <h2>{`Hello ${user} again!`}</h2>
      </>
    );
  }

  const initialTodos = [
    {
      id: 1,
      title: "Todo 1",
      complete: false,
    },
    {
      id: 2,
      title: "Todo 2",
      complete: false,
    },
  ];
  
  const reducer = (state, action) => {
    switch (action.type) {
      case "COMPLETE":
        return state.map((todo) => {
          if (todo.id === action.id) {
            return { ...todo, complete: !todo.complete };
          } else {
            return todo;
          }
        });
      default:
        return state;
    }
  };
//   function Todos() {
//     const [todos, dispatch] = useReducer(reducer, initialTodos);
  
//     const handleComplete = (todo) => {
//       dispatch({ type: "COMPLETE", id: todo.id });
//     };
  
//     return (
//       <>
//         {todos.map((todo) => (
//           <div key={todo.id}>
//             <label>
//               <input
//                 type="checkbox"
//                 checked={todo.complete}
//                 onChange={() => handleComplete(todo)}
//               />
//               {todo.title}
//             </label>
//           </div>
//         ))}
//       </>
//     );
//   }
  


const Home = () => {
    const [count, setCount] = useState(0)
    const [calculation, setCalculation] = useState(0);
    const [todos, setTodos] = useState([]);
    useEffect(() => {
        setCalculation(count * 2)
    }, [count]);

    const addTodo = useCallback(() => {
        setTodos((t) => [...t, "New Todo"]);
      }, []);

    return (
        <>
            <p>Count: {count}</p>
            <p>calculation: {calculation}</p>
            <button onClick={() => setCount((c) => c + 1)}>+</button>
            <h1 style={{ color: "red", backgroundColor: "lightblue"}}>Home</h1>
            <Component1></Component1>
            {/* <Todos /> */}
            <Todos todos={todos} addTodo={addTodo} />
        </>
    )
};

export default Home;