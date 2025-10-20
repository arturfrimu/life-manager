import { Button, DataList } from "@radix-ui/themes";
import { useState } from "react";

interface Exercise {
  id: string;
  title: string;
  image: string;
}

interface ExerciseSet {
  id: string;
  exerciseId: string;
  weight: number;
  reps: number;
  duration?: number;
}

const exercises: Exercise[] = [
  {
    id: 'ex1',
    title: 'Push Ups',
    image: ''
  },
  {
    id: 'ex2',
    title: 'Squats',
    image: ''
  },
  {
    id: 'ex3',
    title: 'Plank',
    image: ''
  },
  {
    id: 'ex4',
    title: 'Lunges',
    image: ''
  },
  {
    id: 'ex5',
    title: 'Burpees',
    image: ''
  },
  {
    id: 'ex6',
    title: 'Pull Ups',
    image: ''
  },
  {
    id: 'ex7',
    title: 'Deadlift',
    image: ''
  },
  {
    id: 'ex8',
    title: 'Bench Press',
    image: ''
  },
  {
    id: 'ex9',
    title: 'Mountain Climbers',
    image: ''
  },
  {
    id: 'ex10',
    title: 'Bicycle Crunches',
    image: ''
  }
];

const useCounter = (defaultValue: number) => {
  const [counter, setCounter] = useState(defaultValue);

  const update = (value: number) => setCounter(value);
  
  return {
    update,
    counter
  }
}

const Counter = ({ defaultValue }: { defaultValue: number }) => {
  const { counter, update } = useCounter(defaultValue);

  return (
    <div>
      <Button onClick={() => update(counter - 1)}>-</Button>
      <div>{counter}</div>
      <Button onClick={() => update(counter + 1)}>+</Button>
    </div>
  )
}

const Sport = () => {
  const [workout, setWorkout] = useState<{
    exercises: Exercise[]
  }>({ exercises: [] });
  const [sets, setSets] = useState<ExerciseSet[]>([]);

  const addExercise = (exercise: Exercise) => {
    setWorkout(prev => ({ ...prev, exercises: [...prev.exercises, exercise] }));
  }

  const addSet = (set: ExerciseSet) => {
    setSets(prev => [...prev, set]);
  }

  return (
    <section>
      <div className="grid grid-cols-2">
        <div>
          <ul>
            {workout.exercises.map(exercise => (
              <li key={exercise.id} className="border">
                <span className="text-2xl font-bold">{exercise.title}</span>

                <ul className="grid grid-cols-5">
                  {sets.filter(set => set.exerciseId === exercise.id).map(set => (
                    <li key={set.id}>

                      <DataList.Root>
                        <DataList.Item align="center">
                          <DataList.Label>Weight</DataList.Label>
                          <DataList.Value>
                            <Counter defaultValue={set.weight} />
                          </DataList.Value>
                        </DataList.Item>
                        <DataList.Item align="center">
                          <DataList.Label>Reps</DataList.Label>
                          <DataList.Value>
                            <Counter defaultValue={set.reps} />
                          </DataList.Value>
                        </DataList.Item>
                        {(set.duration || 0) > 0 && (
                          <DataList.Item align="center">
                            <DataList.Label>Weight</DataList.Label>
                            <DataList.Value>{set.weight}</DataList.Value>
                          </DataList.Item>
                        )}
                      </DataList.Root>
                    </li>
                  ))}
                </ul>

                <Button onClick={addSet.bind(null, {
                  id: crypto.randomUUID(),
                  exerciseId: exercise.id,
                  reps: 10,
                  weight: 15
                })}>+ set</Button>
              </li>
            ))}
          </ul>

          <div>
            Workout List
          </div>
        </div>

        <div className="flex gap-1">
          {exercises.map(exercise => (
            <Button key={exercise.id} onClick={addExercise.bind(null, exercise)}>{exercise.title}</Button>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Sport;
