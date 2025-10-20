import { Heading } from "@radix-ui/themes";

interface Props {
    exercises: Exercise[]
}

const Exercises = ({ exercises }: Props) => {
    console.log(exercises)
  return (
    <div>
        <Heading>Exercises</Heading>
        {exercises.map(exercises => (
            <>
                <div key={exercises.id}>
                    <p>{exercises.name}</p>
                    <p>{exercises.description}</p>
                </div>
            </>
        ))}
    </div>
  );
};

export default Exercises;
