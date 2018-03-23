# SHBS
Simple Hotel Booking System

## I. Technology Stack
```
- Spring Framework 4
- Java 8
- MySQL 5.7
```

## II. How to Run
```
1. clone this repository
2. install JDK 1.8
3. install MySQL 5.7
    - run at localhost:3306 
    - create databases with name 'shbs' and 'shbs-test'
    - create user with name 'shbs' and password 'shbs' and grant it access to shbs and shbs-test database
4. run ./gradlew bootRun
5. by default, application will run at localhost:8080
``` 

## III. API Documentation
This section describes the available API endpoints with example request and response

### 1. Room Type

#### a. get available room types
``` 
GET /room-types/available?start=2018-02-20&end=2018-10-30
```
```
Response :
[
    {
        "id": 1,
        "type": "executive",
        "description": "executive room only for 1 person",
        "image": "",
        "available_quantity": 26,
        "price": 1000
    },
    {
        "id": 2,
        "type": "regular",
        "description": "regular room",
        "image": "",
        "available_quantity": 40,
        "price": 400
    }
]
```

### 2. Reservation

#### a. create reservation
``` 
POST /reservations
Request body :
{
    "room_type_id": 1,
    "customer_id": 1,
    "quantity": 10,
    "start_date": "2019-11-28",
    "end_date": "2019-11-30",
}
```
```
Response :
{
    "id": 1,
    "room_type_id": 1,
    "customer_id": 1,
    "quantity": 10,
    "start_date": "2019-11-28",
    "end_date": "2019-11-30",
    "cancelled": false,
    "created_at": "2018-03-11",
    "updated_at": "2018-03-11"
}
```

#### b. get reservation
``` 
GET /reservations/1
```
```
Response :
{
    "id": 1,
    "room_type_id": 1,
    "customer_id": 1,
    "quantity": 10,
    "start_date": "2019-11-28",
    "end_date": "2019-11-30",
    "cancelled": false,
    "created_at": "2018-03-11",
    "updated_at": "2018-03-11"
}
```

#### c. update reservation
``` 
PATCH /reservations/1
Request body :
{
    "room_type_id": 1,
    "customer_id": 1,
    "quantity": 5,
    "start_date": "2019-11-28",
    "end_date": "2019-11-30",
}
```
```
Response :
{
    "id": 1,
    "room_type_id": 1,
    "customer_id": 1,
    "quantity": 5,
    "start_date": "2019-11-28",
    "end_date": "2019-11-30",
    "cancelled": false,
    "created_at": "2018-03-11",
    "updated_at": "2018-03-11"
}
```

#### d. cancel reservation
``` 
PATCH /reservations/1/cancel
```
```
Response :
{
    "id": 1,
    "room_type_id": 1,
    "customer_id": 1,
    "quantity": 10,
    "start_date": "2019-11-28",
    "end_date": "2019-11-30",
    "cancelled": true,
    "created_at": "2018-03-11 19:23:06",
    "updated_at": "2018-03-11 19:23:06"
}
```

## IV. Admin Dashboard
This section describes the list of available dashboard.

### 1. Login and Logout
Features :
```
a. login
b. logout
```
Default admin user credentials :
```
username : admin
password : admin
```

### 2. Room Type Dashboard
Features :
```
a. list room types
b. create room type
c. update room type
d. delete room type
```

### 3. Reservation
Features
```
a. list reservations
```

### 4. Admin User
Features :
```
a. list admin users
b. create admin user
c. change password
```
