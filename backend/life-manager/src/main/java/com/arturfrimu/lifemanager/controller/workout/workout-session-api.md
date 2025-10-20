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

## Request

```http request
GET http://localhost:8090/api/v1/workouts/4819a345-7149-4d28-a510-7da99366c48b
```

## Response

```json
{
  "id": "4819a345-7149-4d28-a510-7da99366c48b",
  "userId": "11111111-1111-1111-1111-111111111111",
  "name": "Full Body Strength",
  "notes": "Focus on compound lifts: squat, bench, deadlift.",
  "startedAt": "2025-10-20T21:10:58.847921Z",
  "completedAt": "2025-10-20T22:10:58.847921Z",
  "workoutExercises": [
    {
      "id": "600bb549-6d4e-43f7-98cf-78161c4a9481",
      "orderIndex": 0,
      "notes": "Today i'm happy",
      "exercise": {
        "id": "8fd71eae-435f-4c1f-b0c2-8f6f131e0ca3",
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
      "id": "4b8fcf71-29b8-4b72-baad-e768adbd5f15",
      "orderIndex": 1,
      "notes": "Today i'm happy",
      "exercise": {
        "id": "ebb72c0e-ba35-403a-a31d-67655fe2cb8e",
        "name": "Pull-Up",
        "type": "Bodyweight",
        "description": "Upper body pull exercise using bar.",
        "imageUrl": "https://liftmanual.com/wp-content/uploads/2023/04/pull-up.jpg",
        "sets": []
      },
      "created": "2025-10-20T23:13:19.236537Z",
      "updated": "2025-10-20T23:13:19.236537Z"
    },
    {
      "id": "56964b54-0035-448c-9b47-68dbc05eeae6",
      "orderIndex": 2,
      "notes": "Today i'm happy",
      "exercise": {
        "id": "ebb72c0e-ba35-403a-a31d-67655fe2cb8e",
        "name": "Pull-Up",
        "type": "Bodyweight",
        "description": "Upper body pull exercise using bar.",
        "imageUrl": "https://liftmanual.com/wp-content/uploads/2023/04/pull-up.jpg",
        "sets": [
          {
            "id": "cac053fe-6e75-4c6b-bafa-f436a33b29db",
            "setIndex": 0,
            "reps": 12,
            "weight": 80.00,
            "durationSeconds": 60,
            "distanceMeters": 60.00,
            "completed": false,
            "notes": "",
            "created": "2025-10-20T23:32:37.389943Z",
            "updated": "2025-10-20T23:32:37.389943Z"
          },
          {
            "id": "e5c0aa60-0d2c-4713-a247-6a698bac4825",
            "setIndex": 1,
            "reps": 12,
            "weight": 80.00,
            "durationSeconds": 60,
            "distanceMeters": 60.00,
            "completed": false,
            "notes": "",
            "created": "2025-10-20T23:33:07.330672Z",
            "updated": "2025-10-20T23:33:07.330672Z"
          },
          {
            "id": "566488ee-cfaf-4a7e-be9a-27aaea7192c1",
            "setIndex": 2,
            "reps": 12,
            "weight": 80.00,
            "durationSeconds": 60,
            "distanceMeters": 60.00,
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

