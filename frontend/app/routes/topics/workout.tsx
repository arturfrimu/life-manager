import { Button, Container } from "@radix-ui/themes";
import { useLoaderData } from "react-router";
import Exercises from "~/components/Exercises";

const Workout = () => {
  const { workout } = useLoaderData();

  const handleSetAdd = async (workoutExerciseId: string) => {
    try {
        const response = await fetch('http://localhost:8090/api/v1/sets', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                reps: 10,
                weight: 10,
                workoutExerciseId
            })
        });
        const data = await response.json();

        return data;
    } catch (error) {
        console.error(error);
    }
  }

  const handleSetComplete = async (setId: string) => {
    try {
        const response = await fetch(`http://localhost:8090/api/v1/sets/${setId}/toggle-completed`, {
            method: 'PATCH'
        });

        console.log(response)
        const data = await response.json();
        console.log(data)

        return data;
    } catch (error) {
        console.error(error);
    }
  }

  return (
    <section>
        <Container>
            {workout ? (
                <div className="grid grid-cols-5">
                    <div className="col-span-3">
                        {workout.workoutExercises.map(({ id, exercise }) => (
                            <div key={exercise.id}>
                                <p>{exercise.name}</p>
                                <div>
                                    {exercise.sets.map(set => (
                                        <div key={set.id}>
                                            <div>Weight: {set.weight}</div>
                                            <div>Reps: {set.reps}</div>
                                            <div>
                                                <input type="checkbox" onChange={handleSetComplete.bind(null, set.id)} checked={set.completed} />
                                            </div>
                                        </div>
                                    ))}
                                </div>
                                <Button onClick={handleSetAdd.bind(null, id)}>New set</Button>
                            </div>
                        ))}
                    </div>
                    <div className="col-span-2">
                        <Exercises workoutId={workout.id} />
                    </div>
                </div>
            ) : <p>Workout not found</p>}
        </Container>
    </section>
  );
};

export const loader = async ({ params: { id: workoutId } }) => {
    const response = await fetch(`http://localhost:8090/api/v1/workouts/${workoutId}`);
    const workout = await response.json();

    return {
        workout
    }
}

export default Workout;
