import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
    index("routes/home.tsx"),
    route("sport/workouts", "routes/topics/sport.tsx")
] satisfies RouteConfig;
