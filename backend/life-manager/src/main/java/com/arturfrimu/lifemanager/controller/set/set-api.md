# Create set 

## Request

```http request
POST http://localhost:8090/api/v1/sets
```

```json
{
  "workoutExerciseId": "{{WORKOUT_EXERCISE_ID}}",
  "reps": 10,
  "weight": 100
}
```

## Response

```json
{
  "id": "{{SET_ID}}",
  "workoutExerciseId": "{{WORKOUT_EXERCISE_ID}}",
  "setIndex": 3,
  "reps": 12,
  "weight": 80,
  "completed": false,
  "notes": "",
  "created": "2025-10-20T23:48:55.638317Z",
  "updated": "2025-10-20T23:48:55.638317Z"
}
```