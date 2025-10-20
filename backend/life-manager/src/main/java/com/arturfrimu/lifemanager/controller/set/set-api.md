# Create set 

## Request

```http request
GET http://localhost:8090/api/v1/set?page=1&size=2&sort=name,asc
```

## Response

```json
{
  "content": [
    {
      "id": "098aff4b-0a6e-4f43-9453-13db08c677bd",
      "name": "Burpee",
      "type": "Cardio",
      "description": "Full-body exercise combining squat, push-up, and jump.",
      "createdByUserId": "11111111-1111-1111-1111-111111111111",
      "created": "2025-10-20T20:46:23.486600Z",
      "updated": "2025-10-20T20:46:23.486600Z"
    },
    {
      "id": "8b18fd8a-3be0-49e7-8253-46bef937759a",
      "name": "Cycling",
      "type": "Cardio",
      "description": "Leg endurance exercise performed on a bike.",
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