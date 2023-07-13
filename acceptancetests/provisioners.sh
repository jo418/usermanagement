#! /bin/bash
curl -i -X POST -H "Content-Type: application/json" -d '{
  "externalId": 7,
  "name": "provisioner123"
}' http://localhost:8080/api/organisations/1/provisioners -w "%{http_code}\n"
