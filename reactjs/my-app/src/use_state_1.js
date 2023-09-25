import { useState } from "react";

function FavoriteColor() {
    const [color, setColor] = useState("red")
    const flipColor = () => {
        if (color == "red") {
            setColor("blue")
        } else {
            setColor("red")
        }
    }

    return (
        <>
            <h1>My favorite color is {color}</h1>
            <button
                type="button"
                onClick={flipColor}>Blue
            </button>

        </>
    )
}

export default FavoriteColor