import { Button, Container, Table } from "@radix-ui/themes";
import { useLoaderData } from "react-router";

const Sport = () => {
  const { data } = useLoaderData();
  const workouts: Workout[] = data.content;

  return (
    <section>
      <Container>
        <Button>New Workout</Button>

        <Table.Root>
          <Table.Header>
            <Table.Row>
              <Table.ColumnHeaderCell>#</Table.ColumnHeaderCell>
              <Table.ColumnHeaderCell>Name</Table.ColumnHeaderCell>
              <Table.ColumnHeaderCell>Notes</Table.ColumnHeaderCell>
              <Table.ColumnHeaderCell>Last updated</Table.ColumnHeaderCell>
            </Table.Row>
          </Table.Header>

          <Table.Body>
            {workouts.map((workout, idx) => (
              <Table.Row key={workout.id}>
                <Table.RowHeaderCell>{idx}</Table.RowHeaderCell>
                <Table.Cell>{workout.name}</Table.Cell>
                <Table.Cell>{workout.notes}</Table.Cell>
                <Table.Cell>{new Date(workout.updated).toLocaleString()}</Table.Cell>
              </Table.Row>
            ))}
          </Table.Body>
        </Table.Root>
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

export default Sport;
