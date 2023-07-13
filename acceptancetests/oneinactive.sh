#! /bin/bash
curl -i -X GET \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21ldGhpbmciLCJwcm92SWQiOjcsIm9yZ0lkIjoxLCJpYXQiOjE2ODkyNDA0MTcsImV4cCI6MTY4OTI0NzYxN30.MaWy2wRo1Y4BL9G0vBKpNF0Z-Ylrrtz3gieohSSSZ-I" \
http://localhost:8080/api/organisations/1/provisioner/7/users/inactive/1001 -w "%{http_code}\n"
