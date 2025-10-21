# Find workout-sessions with pagination

## Request

```http request
GET http://localhost:8090/api/v1/workouts?page=0&sort=name,asc
```

## Response

```json
{
  "content": [
    {
      "id": "7eb506fd-10e4-4417-ac7a-ba619d70422c",
      "userId": "11111111-1111-1111-1111-111111111111",
      "name": "Full Body Strength",
      "notes": "Focus on compound lifts: squat, bench, deadlift.",
      "startedAt": "2025-10-20T20:01:34.056830Z",
      "completedAt": "2025-10-20T21:01:34.056830Z",
      "created": "2025-10-20T22:01:34.056830Z",
      "updated": "2025-10-20T22:01:34.056830Z"
    },
    {
      "id": "7e55cc71-2e49-4965-918b-7967fdea3fab",
      "userId": "11111111-1111-1111-1111-111111111111",
      "name": "Upper Body Push",
      "notes": "Bench press, shoulder press, triceps dips.",
      "startedAt": "2025-10-19T19:01:34.056830Z",
      "completedAt": "2025-10-19T20:01:34.056830Z",
      "created": "2025-10-20T22:01:34.056830Z",
      "updated": "2025-10-20T22:01:34.056830Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

# Find workout-sessions by id

## Request

```http request
GET http://localhost:8090/api/v1/workouts/4819a345-7149-4d28-a510-7da99366c48b
```

## Response

```json
{
  "id": "{{WORKOUT_ID}}",
  "userId": "11111111-1111-1111-1111-111111111111",
  "name": "Full Body Strength",
  "notes": "Focus on compound lifts: squat, bench, deadlift.",
  "startedAt": "2025-10-20T21:10:58.847921Z",
  "completedAt": "2025-10-20T22:10:58.847921Z",
  "workoutExercises": [
    {
      "id": "{{WORKOUT_EXERCISE_ID}}",
      "orderIndex": 0,
      "notes": "Your notes here...",
      "exercise": {
        "id": "{{EXERCISE_ID}}",
        "name": "Tricep Dips",
        "type": "Bodyweight",
        "description": "Triceps exercise using parallel bars or bench.",
        "imageUrl": "https://training.fit/wp-content/uploads/2020/03/arnold-dips.png",
        "sets": []
      },
      "created": "2025-10-20T23:12:33.455014Z",
      "updated": "2025-10-20T23:12:33.455014Z"
    },
    {
      "id": "{{WORKOUT_EXERCISE_ID}}",
      "orderIndex": 2,
      "notes": "Your notes here...",
      "exercise": {
        "id": "{{EXERCISE_ID}}",
        "name": "Pull-Up",
        "type": "Bodyweight",
        "description": "Upper body pull exercise using bar.",
        "imageUrl": "https://liftmanual.com/wp-content/uploads/2023/04/pull-up.jpg",
        "sets": [
          {
            "id": "{{SET_ID}}",
            "setIndex": 0,
            "reps": 12,
            "weight": 80.00,
            "completed": false,
            "notes": "",
            "created": "2025-10-20T23:32:37.389943Z",
            "updated": "2025-10-20T23:32:37.389943Z"
          },
          {
            "id": "{{SET_ID}}",
            "setIndex": 1,
            "reps": 12,
            "weight": 80.00,
            "completed": false,
            "notes": "",
            "created": "2025-10-20T23:33:07.330672Z",
            "updated": "2025-10-20T23:33:07.330672Z"
          },
          {
            "id": "{{SET_ID}}",
            "setIndex": 2,
            "reps": 12,
            "weight": 80.00,
            "completed": false,
            "notes": "",
            "created": "2025-10-20T23:33:12.035331Z",
            "updated": "2025-10-20T23:33:12.035331Z"
          }
        ]
      },
      "created": "2025-10-20T23:21:40.516966Z",
      "updated": "2025-10-20T23:21:40.516966Z"
    }
  ],
  "created": "2025-10-20T23:10:58.847921Z",
  "updated": "2025-10-20T23:10:58.847921Z"
}
```

