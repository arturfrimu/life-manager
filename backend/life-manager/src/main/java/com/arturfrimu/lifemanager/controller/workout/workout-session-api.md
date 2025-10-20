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