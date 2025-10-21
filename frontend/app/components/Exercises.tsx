import { Button, Heading } from "@radix-ui/themes";
import { useEffect, useState } from "react";

interface Props {
    workoutId: string;
}

const Exercises = ({ workoutId }: Props) => {
  const [exercises, setExercises] = useState<Exercise[]>([]);

  useEffect(() => {
    const getExercises = async () => {
        try {
            const response = await fetch('http://localhost:8090/api/v1/exercises?page=1&size=2&sort=name,asc');
            const data = await response.json();

            setExercises(data.content);
        } catch (error) {
            console.error(error);
        }
    }

    getExercises();
  }, []);

  const handleWorkoutExerciseAdd = async (exerciseId: string) => {
    try {
        const response = await fetch('http://localhost:8090/api/v1/workout-exercises', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                exerciseId,
                workoutSessionId: workoutId
            })
        });

        const data = await response.json();
    } catch (error) {
        console.error(error);
    }
  }

  return (
    <div>
        <Heading>Exercises</Heading>
        <div className="grid grid-cols-2">
            {exercises.map(exercises => (
                <div key={exercises.id}>
                    <img src={exercises.imageUrl} alt={exercises.name} />
                    <p>{exercises.name}</p>
                    <p>{exercises.description}</p>
                    <Button onClick={handleWorkoutExerciseAdd.bind(null, exercises.id)}>Add</Button>
                </div>
            ))}
        </div>
    </div>
  );
};

export default Exercises;
