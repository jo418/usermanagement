# User Management Demo Application

The purpose of this application is to demonstrate an initial user management system
implementation with Java. Providers can ask for JWT token from the system.
With the token users can be created to the system by provider system.

### Technology choices
This application has been implemented with the following technology:
- Java 17, although also older Java versions may be feasible
- Spring as Java backend framework
- IDEA for editing, debugging and invoking the backend service
- jjwt to implement JTW token generation and validation by parsing tokens
- H2 as memory database
- JPA to save and read objects into and from H2. I am just sorry that JDBC was not used, so
there is no SQL visible. JPA is easier to use with small applications, and because we don't have any need to optimize the code with SQL.
Code can be optimized for performance ad clarity later, if there never ever is such need.
The JPA library is javax.persistence.
- lombok library to implement Java objects more easily
- junit for verifying JWT token generation and parsing
- bash to use curl commands to perform acceptance testing
- backend can be invoked from IDE as the state of this work is development, and not production.
To make a production grade, we could use Docker to make an easily deployable and
invocable image. Maybe later, but at the time being the development resources
are needed elsewhere.

### Acceptance testing

Acceptance testing can be made witch curl scripts that are stored in the acceptancetest folder.
The curl scripts in an appropriate oder of application are the following:
- provisioners.sh
- endusers.sh
- allinactive.sh
- oneinactive.sh
- activateone.sh
- activateall.sh

Start backend service from IDEA.

![Invoke backend](images/StartBackendFromIdea.png)

Introduce provisioner to the system

```bash
cd acceptancetest
./provisioners.sh
```
The expected output is:
```text
{"id":1,"name":"provisioner123","secret":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21ldGhpbmciLCJwcm92SWQiOjcsIm9yZ0lkIjoxLCJpYXQiOjE2ODkyNDkwMjAsImV4cCI6MTY4OTI1NjIyMH0.wFJHGgvQYyQ6zzEyn3uyskYN75y3vK-UusMwFAI6ohg","externalId":7}200
```
First the provisioner JSON object is returned. It contains JWT token in the secret field. You are supposed to 
copy it and to paste it into authorization header of the all other verification scripts. The last number, here 200 is the http status of the request.
Now, when you have copied and pasted the jwt token into the enduser.sh script, it can be run as follows:
```bash
./enusers.sh
```
Teh expected output is:
```text
{"id":1,"email":"john.doe@company.com","organisationId":1,"name":{"firstName":"John","lastName":"Doe"},"externalId":1001,"password":"aksdffdjkg","active":false}201
```
The data content of enduser object is returned as JSON. The http status of the request is CREATED, that is 201. The status of active status of so created enuser is false.
Next one create more end users, if one edits the request body in the enuser.sh script of makes other similar copies of it and runs them.
But for happy day scenario we can use the rest of the endpoints. Just copy and paste the JWT token into the authorization header in the scripts first.

To show all inactive users, type
```bash
./allinactive.sh
```
To show one inactive user as selected with id, to set id, edit the id in the endpoint http://localhost:8080/api/organisations/1/provisioner/7/users/inactive/1001. Then use
```bash
./oneinactive.sh
```
To activate one user by its id, use
```bash
./activateone.sh
```
To activate all inactive endusers by list of ids, after editing the jwt token, use
```bash
./activateall.sh
```
In error cases, there will be an appropriate http status given. Among the usual 400, 401, and 404, there will be 418 given, if no enduser is found to be activated. 