import { Container } from "@radix-ui/themes";
import { useLoaderData } from "react-router";
import Exercises from "~/components/Exercises";

const Workout = () => {
  const { workout } = useLoaderData();

  return (
    <section>
        <Container>
            {workout ? (
                <div className="grid grid-cols-5">
                    <div className="col-span-3">
                        {workout.workoutExercises.map(({ exercise }) => (
                            <div key={exercise.id}>
                                {exercise.name}
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
