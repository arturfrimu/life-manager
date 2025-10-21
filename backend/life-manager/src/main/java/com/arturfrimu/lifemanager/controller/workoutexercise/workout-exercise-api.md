# Workout Exercise API

## Create Workout Exercise

Adds an exercise to a workout session. The order index is automatically calculated based on existing exercises in the workout.

### Request

```http request
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

### Request Fields

- `workoutSessionId` (required) - ID of the workout session
- `exerciseId` (required) - ID of the exercise to add
- `notes` (optional) - Additional notes for this exercise in the workout

### Response

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

### Notes

- The `orderIndex` is **automatically calculated** based on existing exercises in the workout
- If no exercises exist, `orderIndex` starts at 0
- Each new exercise gets the next sequential index