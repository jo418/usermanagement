#! /bin/bash
curl -i -X POST \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21ldGhpbmciLCJwcm92SWQiOjcsIm9yZ0lkIjoxLCJpYXQiOjE2ODkyNDkwMjAsImV4cCI6MTY4OTI1NjIyMH0.wFJHGgvQYyQ6zzEyn3uyskYN75y3vK-UusMwFAI6ohg" \
-d '{
  "organisation_id": 1,
  "email": "john.doe@company.com",
  "name": {
     "firstName": "John",
     "lastName": "Doe"},
  "externalId": "1001",
  "password": "aksdffdjkg"
}' http://localhost:8080/api/organisations/1/provisioner/7/users -w "%{http_code}\n"
