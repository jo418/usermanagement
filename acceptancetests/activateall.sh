#! /bin/bash
curl -i -X POST \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21ldGhpbmciLCJwcm92SWQiOjcsIm9yZ0lkIjoxLCJpYXQiOjE2ODkyNDkwMjAsImV4cCI6MTY4OTI1NjIyMH0.wFJHGgvQYyQ6zzEyn3uyskYN75y3vK-UusMwFAI6ohg" \
-d '{"ids": [1001, 1002, 1003]
}' http://localhost:8080/api/organisations/1/provisioner/7/users/activateall -w "%{http_code}\n"
