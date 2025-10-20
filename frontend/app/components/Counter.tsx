import { Button } from "@radix-ui/themes";
import useCounter from "../hooks/useCounter";
import { useEffect } from "react";

interface Props {
    defaultValue: number;
    onCounterUpdate?: (value: number) => void;
}

const Counter = ({ defaultValue = 0, onCounterUpdate }: Props) => {
  const { counter, update, increment, decrement } = useCounter(defaultValue);

  useEffect(() => {
    onCounterUpdate?.(counter);
  }, [counter, onCounterUpdate]);

  return (
    <div>
      <Button onClick={decrement}>-</Button>
      <input type="number" onChange={(evt: React.ChangeEvent<HTMLInputElement>) => update(parseInt(evt.target.value))} value={counter} />
      <Button onClick={increment}>+</Button>
    </div>
  );
}

export default Counter;
