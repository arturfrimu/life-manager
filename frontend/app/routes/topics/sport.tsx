import { Box, Container } from "@radix-ui/themes";
import { useLoaderData } from "react-router";
import Exercises from "~/components/Exercises";

const Sport = () => {
  const data = useLoaderData();

  return (
    <Box>
      <Container>
        <div className="grid grid-cols-2">
          <div>
            List
          </div>

          {data.data?.content?.length > 0 && <Exercises exercises={data.data.content} />}
        </div>
      </Container>
    </Box>
  );
};

export const loader = async () => {
  const response = await fetch("http://localhost:8090/api/v1/exercises?page=0&size=10&sort=name,asc");
  const data = await response.json();

  return {
    data
  }
}

export default Sport;
