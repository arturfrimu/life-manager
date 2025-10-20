import { useState } from "react";

const useCounter = (defaultValue: number | undefined) => {
  const [counter, setCounter] = useState(defaultValue || 0);

  const update = (value: number) => setCounter(value);
  const increment = () => setCounter(prev => prev + 1);
  const decrement = () => setCounter(prev => prev - 1);
  
  return {
    counter,

    update,
    increment,
    decrement
  }
}

export default useCounter;
