#! /bin/bash
curl -i -X POST \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21ldGhpbmciLCJwcm92SWQiOjcsIm9yZ0lkIjoxLCJpYXQiOjE2ODkyMzU2MjEsImV4cCI6MTY4OTI0MjgyMX0.PlivndXNE-6jHnzdBuTpKYLw2MnTr5QdWLSo2lJiWWw" \
http://localhost:8080/api/organisations/1/provisioner/7/users/activate/1001 -w "%{http_code}\n"
