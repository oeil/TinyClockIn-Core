# TinyClockIn-Core
Standalone application backend that exposes a RESTful API for a super easy clock-in/clock-out system to track employees activity at Corporations.

## REST API Specifications

### Authenticate User
> POST api/auth/

Retrives an authorization Token.

Request Body (JSON)
```
{
  “email”: “user.name@domain.com”
}
```
Response Body (JSON
```
{
  “email”: “user.name@domain.com”,
  “token”: “TOKEN_ID”
}
```

When a request is made, the API always returns a token. If this is the first time a request is made for a particular email, the user is automatically created and a token is returned.

For any subsequent requests on **secured** endpoints, provide a valid Token Id via the standard Authorization header of the request.

#### Header Example
> Authorization: Bearer **TOKEN_ID**

### Perform Check-in/Checkout Action (secured)
> POST api/actions

Request Body (JSON)
```
{
  “type”: 1,
  “description”: “some comments”,
  “workstation”: 42
}
```
**Fields**
- **type**: Integer used to specify the kind of action
  - 0 = Check-out
  - 1 = Check-in
- **description**: (max 200 chars) (Optional) String used to provide a free form text to describe the action
- **workstation**: Integer value used to identify a workstation, user must provide this number (value between 10 and 1000 is valid)


Response Body (JSON)
```
{
  “id”: 1,
  “timestamp”: “2017-04-17T10:06:51.465Z”
  “type”: 1,
  “description”: “some comments”,
  “workstation”: 42
}
```
**Fields**
- **id**: unique identifier of the action resource
- **timestamp**: (ISO8601) date time of the action

### Retrieve All Actions (secured)
> GET api/actions

Returns all previous actions performed (for signed user).

Response Body (JSON)
```
[{
    “id”: 1,
    “timestamp”: “2017-04-17T08:06:51.465Z”
    “type”: 1,
    “description”: “good morning”,
    “workstation”: 42
  },
  {
    “id”: 2,
    “timestamp”: “2017-04-17T11:30:42.547Z”
    “type”: 0,
    “description”: “check out for lunch”,
    “workstation”: 101
}]
```

### Retrieve Latest Action (secured)
> GET api/actions?latestOnly=true

Returns only the last action performed by user.

Response Body (JSON)
```
[{
  “id”: 2,
  “timestamp”: “2017-04-17T11:30:42.547Z”
  “type”: 0,
  “description”: “check out for lunch”,
  “workstation”: 101
}]
```

## Build Project
```
mvn clean package
```

## Run Application
```
java -jar tinyclockin-core-VERSION.jar
```
> Note: config.properties file must exist next to the JAR file, a default config file will be generated otherwise.
