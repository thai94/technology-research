import { useState, useCallback } from 'react';

function useCount() {

    const [count, setCount] = useState(0)
    // const increaseCount = useCallback(() => {
    //     setCount(count + 1)
    // })

    function increaseCount(params) {
        setCount(count + 1)
    }

    return {
        count, increaseCount
    }
}

export default useCount