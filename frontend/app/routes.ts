import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
    index("routes/home.tsx"),
    route("sport/workouts", "routes/topics/sport.tsx"),
    route("sport/workouts/:id", "routes/topics/workout.tsx")
] satisfies RouteConfig;
