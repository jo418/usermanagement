#! /bin/bash
curl -i -X POST \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21ldGhpbmciLCJwcm92SWQiOjcsIm9yZ0lkIjoxLCJpYXQiOjE2ODkyNDMwMTcsImV4cCI6MTY4OTI1MDIxN30.3_jhfOOd_e718br3k-JcnOPwYOayKmKfLpUwO5A0BtI" \
-d '{"ids": [1001, 1002, 1003]
}' http://localhost:8080/api/organisations/1/provisioner/7/users/activateall -w "%{http_code}\n"
