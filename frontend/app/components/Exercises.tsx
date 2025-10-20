import { Heading } from "@radix-ui/themes";

interface Props {
    exercises: Exercise[]
}

const Exercises = ({ exercises }: Props) => {
  return (
    <div>
        <Heading>Exercises</Heading>
        {exercises.map(exercises => (
            <>
                <div key={exercises.id}>
                    <img src={exercises.imageUrl} alt={exercises.name} />
                    <p>{exercises.name}</p>
                    <p>{exercises.description}</p>
                </div>
            </>
        ))}
    </div>
  );
};

export default Exercises;
