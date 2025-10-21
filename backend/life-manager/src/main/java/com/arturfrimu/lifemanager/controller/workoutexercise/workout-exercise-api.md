# Find workout-exercise with pagination

## Request

```
POST http://localhost:8090/api/v1/workout-exercises
Content-Type: application/json
```

```json
{
  "workoutSessionId": "{{WORKOUT_SESSION_ID}}",
  "exerciseId": "{{EXERCISE_ID}}",
  "notes": "Your notes here..."
}
```

## Response

```json
{
  "id": "{{WORKOUT_EXERCISE_ID}}",
  "workoutSessionId": "{{WORKOUT_SESSION_ID}}",
  "exerciseId": "{{EXERCISE_ID}}",
  "orderIndex": 0,
  "notes": "Your notes here...",
  "created": "2025-10-20T23:21:40.516966Z",
  "updated": "2025-10-20T23:21:40.516966Z"
}
```