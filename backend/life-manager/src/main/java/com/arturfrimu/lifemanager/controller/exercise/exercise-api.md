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