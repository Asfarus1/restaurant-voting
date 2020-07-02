REST API using Hibernate/jjwt/Spring-Boot without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we assume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides new menu each day.

Request examples:

Default users for testing api:
1. username: 'User' password: 'u' roles: 'USER'
2. username: 'User' password: 'a' roles: 'USER,ADMIN'

TOKEN AUTHENTICATION SERVICE:
  
===
Returns access and refresh tokens by basic auth

request: curl -X POST -H 'Authorization: Basic dXNlcjp1' http://localhost:8080/auth/create-token

response: {
    "accessToken":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTkzNjA1ODI0LCJleHAiOjE1OTU0MDU4MjR9.bMglz8XwrVrufsvsL8bb0BRFlUnfcI0v0w8fS0pTyoQ",
    "refreshToken":"aa4fbcce-b2a9-4c7d-9a75-4bc403e9c7bb",
    "accessExpired":"2020-07-22T08:17:04.767+0000",
    "refreshExpired":"2021-07-01T12:17:04.767+0000"
}

===
Returns access and refresh tokens by refresh token

request: curl -X POST -H 'Content-Type: application/json' -d '{"username":"user","refreshToken":"aa4fbcce-b2a9-4c7d-9a75-4bc403e9c7bb"}' http://localhost:8080/auth/refresh-token

response: {
    "accessToken":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTkzNjEwODQwLCJleHAiOjE1OTU0MTA4NDB9.CN0ScnbDyOh8oxJNmktJL2dunk7sDefemUKHoK52JwM",
    "refreshToken":"984f71a3-ab9f-4fb5-8a03-838d7e6dd8ab",
    "accessExpired":"2020-07-22T09:40:40.125+0000",
    "refreshExpired":"2021-07-01T13:40:40.125+0000"
}


===
Creates new user and returns access and refresh tokens

request: curl -X POST -H 'Content-Type: application/json' -d '{"name":"new_user","password":"123"}' http://localhost:8080/auth/sign-up

response: {
    "accessToken":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXdfdXNlciIsImlhdCI6MTU5MzYxMTgxMCwiZXhwIjoxNTk1NDExODEwfQ.TI81hUdl0HspUKLaA43ZkHcuxr8OORT1k7BvSVqqsis",
    "refreshToken":"b4f9aa57-8a2b-49c1-a971-044a8e433ebc",
    "accessExpired":"2020-07-22T09:56:50.486+0000",
    "refreshExpired":"2021-07-01T13:56:50.486+0000"
}

===
Removes all refresh tokens for user authenticated with basic auth
  
request: curl -X POST -H 'Authorization: Basic dXNlcjp1' http://localhost:8080/auth/logout
response status: 202

<<<<<<<
REST API:
description:
http://localhost:8080/rest-api

example: curl -H 'Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTkzNjA1ODI0LCJleHAiOjE1OTU0MDU4MjR9.bMglz8XwrVrufsvsL8bb0BRFlUnfcI0v0w8fS0pTyoQ' http://localhost:8080/rest-api

====
Base entities:
http://localhost:8080/rest-api/dishes (search by title)
http://localhost:8080/rest-api/restaurants (search by title)
http://localhost:8080/rest-api/menus (search by date)

User role available methods : GET
Admin role available methods : GET, POST, PUT, DELETE

example: curl -H 'Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXdfdXNlciIsImlhdCI6MTU5MzYxMTgxMCwiZXhwIjoxNTk1NDExODEwfQ.TI81hUdl0HspUKLaA43ZkHcuxr8OORT1k7BvSVqqsis' http://localhost:8080/rest-api/dishes

===
User list:
http://localhost:8080/rest-api/users (search by username)

User role available methods : for access to current profile use GET "/account" or "/account/*"
Admin role available methods : GET, POST, PUT, DELETE

===
User actions:

PUT - http://localhost:8080/restaurants/{restaurantId}/have-lunch - select restaurant for lunch today
GET - http://localhost:8080/today-menus - returns pageable list of today menus in different restaurants
GET - http://localhost:8080/account - current user
GET - http://localhost:8080/account/lunches - current user lunch history

===
Lunch history:
http://localhost:8080/rest-api/lunches

User role available methods : GET
Admin role available methods : GET
