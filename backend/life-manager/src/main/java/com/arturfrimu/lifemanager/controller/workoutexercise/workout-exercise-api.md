# Find workout-exercise with pagination

## Request

```
POST http://localhost:8090/api/v1/workout-exercises
Content-Type: application/json

{
  "workoutSessionId": "4819a345-7149-4d28-a510-7da99366c48b",
  "exerciseId": "ebb72c0e-ba35-403a-a31d-67655fe2cb8e",
  "notes": "Today i'm happy"
}
```

## Response

```json
{
  "id": "56964b54-0035-448c-9b47-68dbc05eeae6",
  "workoutSessionId": "4819a345-7149-4d28-a510-7da99366c48b",
  "exerciseId": "ebb72c0e-ba35-403a-a31d-67655fe2cb8e",
  "orderIndex": 0,
  "notes": "Today i'm happy",
  "created": "2025-10-20T23:21:40.516966Z",
  "updated": "2025-10-20T23:21:40.516966Z"
}
```