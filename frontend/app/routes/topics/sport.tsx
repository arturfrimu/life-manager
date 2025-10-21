import { Button, Container, Table } from "@radix-ui/themes";
import { Form, redirect, useLoaderData, useNavigate } from "react-router";

const Sport = () => {
  const { data } = useLoaderData();
  const navigate = useNavigate();

  const workouts: Workout[] = data.content;

  const handleWorkoutDelete = async (id: string) => {
    try {
      const response = await fetch(`http://localhost:8090/api/v1/workouts/${id}`, {
        method: 'DELETE'
      });

      if (response.status !== 204) {
        console.log('Failed to delete workout\n', response);
      }
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <section>
      <Container>
        <Form method="post">
          <Button type="submit">New Workout</Button>
        </Form>

        {workouts.length > 0 && (
          <Table.Root>
            <Table.Header>
              <Table.Row>
                <Table.ColumnHeaderCell>#</Table.ColumnHeaderCell>
                <Table.ColumnHeaderCell>Name</Table.ColumnHeaderCell>
                <Table.ColumnHeaderCell>Notes</Table.ColumnHeaderCell>
                <Table.ColumnHeaderCell>Last updated</Table.ColumnHeaderCell>
                <Table.ColumnHeaderCell>-</Table.ColumnHeaderCell>
              </Table.Row>
            </Table.Header>

            <Table.Body>
              {workouts.map((workout, idx) => (
                <Table.Row key={workout.id} onDoubleClick={() => navigate(`/sport/workouts/${workout.id}`)} className="cursor-pointer">
                  <Table.RowHeaderCell>{idx}</Table.RowHeaderCell>
                  <Table.Cell>{workout.name}</Table.Cell>
                  <Table.Cell>{workout.notes}</Table.Cell>
                  <Table.Cell>{new Date(workout.updated).toLocaleString()}</Table.Cell>
                  <Table.Cell>
                    <Button onClick={handleWorkoutDelete.bind(null, workout.id)}>Delete</Button>
                  </Table.Cell>
                </Table.Row>
              ))}
            </Table.Body>
          </Table.Root>
        )}
      </Container>
    </section>
  );
};

export const loader = async () => {
  const response = await fetch("http://localhost:8090/api/v1/workouts?page=0&size=10&sort=name,asc");
  const data = await response.json();

  return {
    data
  }
}

export const action = async () => {
  const response = await fetch('http://localhost:8090/api/v1/workouts', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' }
  });

  const data = await response.json();

  return redirect(`/sport/workouts/${data.id}`);
}

export default Sport;
