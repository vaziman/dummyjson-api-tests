#  Full API Test Cases for DummyJSON

---

##  AUTHENTICATION (`/auth/login`, `/auth/me`)
| TC ID | Title                                         | Steps                                                             | Expected Result                                              |
| ----- | --------------------------------------------- | ----------------------------------------------------------------- | ------------------------------------------------------------ |
| TC001 | Login with valid credentials                  | POST /auth/login with valid username/password                     | 200 OK, response contains `accessToken`                      |
| TC002 | Login with invalid credentials                | POST /auth/login with wrong username/password                     | 400 Bad Request, message: `"Invalid credentials"`            |
| TC003 | Login with empty credentials                  | POST /auth/login with empty username/password                     | 400 Bad Request, message: `"Username and password required"` |
| TC004 | Get current user with valid token             | GET /auth/me with valid bearer token                              | 200 OK, user object returned                                 |
| TC005 | Get current user with invalid/expired token   | GET /auth/me with expired, empty, or malformed token              | 401 Unauthorized, message: `"Invalid/Expired Token!"`        |
| TC006 | Login with missing username field             | POST /auth/login with only password in request body               | 400 Bad Request, message: `"Username and password required"` |
| TC007 | Login with missing password field             | POST /auth/login with only username in request body               | 400 Bad Request, message: `"Username and password required"` |
| TC008 | Login with incorrect request body structure   | POST /auth/login with invalid JSON structure                      | 400 Bad Request or appropriate validation error              |
| TC009 | Login with extra fields in request body       | POST /auth/login with additional unexpected fields                | 200 OK or ignored extra fields, login still succeeds         |
| TC010 | Login with SQL injection in username          | POST /auth/login with `username="' OR 1=1 --"`                    | 400 Bad Request or handled as invalid credentials            |
| TC011 | Login with XSS payload in username            | POST /auth/login with `username="<script>alert(1)</script>"`      | 400 Bad Request or input sanitized                           |
| TC012 | Login using invalid HTTP method               | GET /auth/login instead of POST                                   | 405 Method Not Allowed                                       |
| TC013 | Login with Content-Type missing or invalid    | POST /auth/login without or with wrong Content-Type header        | 415 Unsupported Media Type or 400 Bad Request                |
| TC014 | Get current user without Authorization header | GET /auth/me without Authorization header                         | 401 Unauthorized                                             |
| TC015 | Get current user with token in wrong format   | GET /auth/me with `Authorization: Token abc123` instead of Bearer | 401 Unauthorized                                             |
| TC016 | Login with whitespace-only username/password  | POST /auth/login with `"   "` as username and password            | 400 Bad Request, treated as empty                            |

---

##  USERS (`/users`, `/users/{id}`, `/users/search`)

| TC ID | Title                          | Steps                         | Expected Result                              |
|-------|--------------------------------|-------------------------------|-----------------------------------------------|
| TC017 | Get all users                  | GET /users                    | 200 OK, list of users                         |
| TC018 | Get users with limit/skip      | GET /users?limit=50&skip=10   | 200 OK, returns 20 users skipping first 10    |
| TC019 | Get users with invalid limit   | GET /users?limit=-208         | 200 OK, empty list                            |
| TC020 | Get user by valid ID           | GET /users/1                  | 200 OK, user with ID 1                        |
| TC021 | Get user by invalid ID         | GET /users/-1                 | 404 Not Found, error message                  |
| TC022 | Search user by full first name | GET /users/search?q=Emily     | 200 OK, contains "Emily" in firstName         |
| TC023 | Search by partial name         | GET /users/search?q=em        | 200 OK, names containing "em"                 |
| TC024 | Search by lowercase name       | GET /users/search?q=emily     | 200 OK, case-insensitive match                |
| TC025 | Search with no match           | GET /users/search?q=asdfgh    | 200 OK, empty user list                       |
| TC026 | Sort users ASC                 | GET /users?sort=firstName     | 200 OK, sorted A→Z                            |
| TC027 | Sort users DESC                | GET /users?sort=id&order=desc | 200 OK, sorted Z→A or high→low ID             |

---

##  PRODUCTS (`/products`, `/products/{id}`, `/products/search`)

| TC ID | Title                                      | Steps                                                         | Expected Result                             |
|-------|--------------------------------------------|----------------------------------------------------------------|----------------------------------------------|
| TC017 | Get all products                           | GET /products                                                  | 200 OK, list of products                     |
| TC018 | Get product by valid ID                    | GET /products/1                                                | 200 OK, valid product returned               |
| TC019 | Get product by invalid ID                  | GET /products/-1                                               | 404 Not Found                                |
| TC020 | Search product by full name                | GET /products/search?q=iPhone                                  | 200 OK, products with “iPhone” in title      |
| TC021 | Search product by partial name             | GET /products/search?q=lap                                     | 200 OK, products with "lap" in title         |
| TC022 | Search product no match                    | GET /products/search?q=asdfgh                                  | 200 OK, empty product list                   |
| TC023 | Get products with limit and skip           | GET /products?limit=5&skip=10                                  | 200 OK, 5 products starting from 11th        |
| TC024 | Sort products by price ascending           | GET /products?sort=price                                       | 200 OK, sorted by price low→high             |
| TC025 | Sort products by rating descending         | GET /products?sort=rating&order=desc                           | 200 OK, sorted high→low                      |

---

##  CARTS (`/carts`, `/carts/{id}`, `/carts/user/{userId}`)

| TC ID | Title                                     | Steps                                                         | Expected Result                           |
|-------|-------------------------------------------|----------------------------------------------------------------|--------------------------------------------|
| TC026 | Get all carts                             | GET /carts                                                     | 200 OK, list of carts                      |
| TC027 | Get cart by valid ID                      | GET /carts/1                                                   | 200 OK, cart object returned               |
| TC028 | Get cart by invalid ID                    | GET /carts/-1                                                  | 404 Not Found                              |
| TC029 | Get user’s cart by userId                 | GET /carts/user/5                                              | 200 OK, user’s cart returned               |
| TC030 | Add product to cart                       | POST /carts/add with valid body                                | 200 OK, updated cart returned              |
| TC031 | Add product with missing fields           | POST /carts/add with incomplete JSON                           | 400 Bad Request                            |

---

##  TODOS (`/todos`, `/todos/{id}`, `/todos/user/{userId}`)

| TC ID | Title                                     | Steps                                                     | Expected Result                             |
|-------|-------------------------------------------|------------------------------------------------------------|----------------------------------------------|
| TC032 | Get all todos                             | GET /todos                                                 | 200 OK, list of todos                        |
| TC033 | Get todo by valid ID                      | GET /todos/1                                               | 200 OK, todo item returned                   |
| TC034 | Get todo by invalid ID                    | GET /todos/-1                                              | 404 Not Found                                |
| TC035 | Get todos by userId                       | GET /todos/user/1                                          | 200 OK, todos belonging to user              |
| TC036 | Validate completed todos                  | GET /todos?completed=true                                  | 200 OK, only completed todos                 |
| TC037 | Validate uncompleted todos                | GET /todos?completed=false                                 | 200 OK, only incomplete todos                |

---

##  POSTS (`/posts`, `/posts/{id}`, `/posts/user/{userId}`)

| TC ID | Title                                     | Steps                                                     | Expected Result                            |
|-------|-------------------------------------------|------------------------------------------------------------|---------------------------------------------|
| TC038 | Get all posts                             | GET /posts                                                 | 200 OK, list of posts                       |
| TC039 | Get post by valid ID                      | GET /posts/1                                               | 200 OK, post returned                       |
| TC040 | Get post by invalid ID                    | GET /posts/-1                                              | 404 Not Found                               |
| TC041 | Get posts by userId                       | GET /posts/user/5                                          | 200 OK, list of user’s posts                |
| TC042 | Search posts by keyword                   | GET /posts/search?q=love                                   | 200 OK, list of matching posts              |

---

##  COMMENTS (`/comments`, `/comments/{id}`, `/comments/post/{postId}`)

| TC ID | Title                                     | Steps                                                     | Expected Result                            |
|-------|-------------------------------------------|------------------------------------------------------------|---------------------------------------------|
| TC043 | Get all comments                          | GET /comments                                              | 200 OK, list of comments                    |
| TC044 | Get comment by valid ID                   | GET /comments/1                                            | 200 OK, comment object returned             |
| TC045 | Get comment by invalid ID                 | GET /comments/-1                                           | 404 Not Found                               |
| TC046 | Get comments by postId                    | GET /comments/post/3                                       | 200 OK, list of comments for post           |

---

##  COMMON NEGATIVE CASES

| TC ID | Title                                         | Steps                                                    | Expected Result                        |
|-------|-----------------------------------------------|-----------------------------------------------------------|-----------------------------------------|
| TC047 | Send request with missing required fields     | POST /products with missing body                          | 400 Bad Request                         |
| TC048 | Use unsupported HTTP method                   | PUT /products                                             | 405 Method Not Allowed                  |
| TC049 | Access secured endpoint without token         | GET /auth/me without Authorization header                 | 401 Unauthorized                        |
| TC050 | Send malformed JSON                           | POST /carts/add with broken JSON                          | 400 Bad Request                         |

---

##  Notes


