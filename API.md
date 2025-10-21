# Life Manager API Documentation

# Exercise API

## Get All Exercises (with pagination)

Retrieves a paginated list of all exercises.

### Request

```http request
GET http://localhost:8090/api/v1/exercises?page=1&size=2&sort=name,asc
```

### Query Parameters

- `page` (optional) - Page number (default: 0)
- `size` (optional) - Page size (default: 20)
- `sort` (optional) - Sort field and direction, e.g., `name,asc` or `created,desc`

### Response

```json
{
  "content": [
    {
      "id": "{{EXERCISE_ID}}",
      "name": "Burpee",
      "type": "Cardio",
      "description": "Full-body exercise combining squat, push-up, and jump.",
      "imageUrl": "https://example.com/burpee.png",
      "createdByUserId": "11111111-1111-1111-1111-111111111111",
      "created": "2025-10-20T20:46:23.486600Z",
      "updated": "2025-10-20T20:46:23.486600Z"
    },
    {
      "id": "{{EXERCISE_ID}}",
      "name": "Cycling",
      "type": "Cardio",
      "description": "Leg endurance exercise performed on a bike.",
      "imageUrl": "https://example.com/cycling.png",
      "createdByUserId": "11111111-1111-1111-1111-111111111111",
      "created": "2025-10-20T20:46:23.486600Z",
      "updated": "2025-10-20T20:46:23.486600Z"
    }
  ],
  "page": 1,
  "size": 2,
  "totalElements": 15,
  "totalPages": 8,
  "first": false,
  "last": false
}
```

---

# Workout Session API

## Initialize Workout

Creates a new empty workout session with current date and time as the name.

### Request

```http request
POST http://localhost:8090/api/v1/workouts
```

No request body required.

### Response

```json
{
  "id": "{{WORKOUT_ID}}",
  "userId": "11111111-1111-1111-1111-111111111111",
  "name": "2025-10-21 17:30:45",
  "notes": null,
  "startedAt": "2025-10-21T15:30:45.123456Z",
  "completedAt": null,
  "created": "2025-10-21T15:30:45.123456Z",
  "updated": "2025-10-21T15:30:45.123456Z"
}
```

### Notes

- The workout name is automatically generated using format: `yyyy-MM-dd HH:mm:ss`
- The `startedAt` field is set to current timestamp
- The workout is created empty (no exercises)
- Uses the first available user from the database

---

## Get All Workout Sessions (with pagination)

Retrieves a paginated list of all workout sessions with basic information.

### Request

```http request
GET http://localhost:8090/api/v1/workouts?page=0&size=10&sort=name,asc
```

### Query Parameters

- `page` (optional) - Page number (default: 0)
- `size` (optional) - Page size (default: 20)
- `sort` (optional) - Sort field and direction, e.g., `name,asc`, `startedAt,desc`, `created,desc`

### Response

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

---

## Get Workout Session Details by ID

Retrieves complete workout session details including all exercises and sets. This endpoint uses optimized JOIN FETCH queries to load all related data efficiently.

### Request

```http request
GET http://localhost:8090/api/v1/workouts/{{WORKOUT_ID}}
```

### Response

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
      "orderIndex": 1,
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
            "notes": null,
            "created": "2025-10-20T23:32:37.389943Z",
            "updated": "2025-10-20T23:32:37.389943Z"
          },
          {
            "id": "{{SET_ID}}",
            "setIndex": 1,
            "reps": 12,
            "weight": 80.00,
            "completed": false,
            "notes": null,
            "created": "2025-10-20T23:33:07.330672Z",
            "updated": "2025-10-20T23:33:07.330672Z"
          },
          {
            "id": "{{SET_ID}}",
            "setIndex": 2,
            "reps": 12,
            "weight": 80.00,
            "completed": false,
            "notes": null,
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

### Performance

- Uses **2 optimized database queries** (not N+1)
- Fetches workout with exercises in first query
- Fetches all sets in second query
- All data is eagerly loaded and returned in a single response

### Error Responses

- `404 NOT FOUND` - Workout session with the specified ID does not exist

---

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

---

# Set API

## Create set 

### Request

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

### Response

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

---

## Toggle Completed

Toggles the completed status of a set. If the set is marked as completed, it will be marked as not completed and vice versa.

### Request

```http request
PATCH http://localhost:8090/api/v1/sets/{{SET_ID}}/toggle-completed
```

No request body required.

### Response

```json
{
  "id": "{{SET_ID}}",
  "workoutExerciseId": "{{WORKOUT_EXERCISE_ID}}",
  "setIndex": 0,
  "reps": 10,
  "weight": 100,
  "completed": true,
  "notes": null,
  "created": "2025-10-20T23:48:55.638317Z",
  "updated": "2025-10-20T23:50:12.123456Z"
}
```

### Behavior

- `completed: false` → `completed: true`
- `completed: true` → `completed: false`

---

## Adjust Weight

Adjusts the weight of a set by adding the specified adjustment value to the current weight. Supports both positive (increase) and negative (decrease) adjustments.

### Request

```http request
PATCH http://localhost:8090/api/v1/sets/{{SET_ID}}/adjust-weight
```

```json
{
  "weightAdjustment": 2.5
}
```

### Response

```json
{
  "id": "{{SET_ID}}",
  "workoutExerciseId": "{{WORKOUT_EXERCISE_ID}}",
  "setIndex": 0,
  "reps": 10,
  "weight": 102.5,
  "completed": false,
  "notes": null,
  "created": "2025-10-20T23:48:55.638317Z",
  "updated": "2025-10-20T23:51:30.987654Z"
}
```

### Examples

**Increase weight:**
```json
{
  "weightAdjustment": 5.0
}
```
Result: `100 kg → 105 kg`

**Decrease weight:**
```json
{
  "weightAdjustment": -2.5
}
```
Result: `100 kg → 97.5 kg`

### Validation

- The resulting weight **cannot be negative**
- If the adjustment would result in a negative weight, a `400 BAD_REQUEST` error is returned
- Example error: `"Weight cannot be negative. Current weight: 2.5, adjustment: -5.0"`

