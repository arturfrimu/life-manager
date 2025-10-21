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