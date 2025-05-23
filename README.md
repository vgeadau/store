# MY SPRING BOOT PROJECT
A RESTful API built with Spring Boot for managing products with basic authentication.

## 🚀 Features
- User authentication with JWT
- CRUD operations for products
- Exception handling and logging
- H2 in-memory database (for dev)

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Maven
- H2
- JUnit & Mockito

## ⚙️ Setup

### Prerequisites
- Java 17+
- Maven 3.x

### Run Locally

```bash
git clone https://github.com/vgeadau/store.git
cd store
mvn spring-boot:run
```

### THE TASK (AS RECEIVED)
- (1) Create a GitHub profile if you don't have one
- (2) Use git in a verbose manner, push even if you wrote only one class
- (3) Create a Java, maven based project, SpringBoot for the web part
- (4) No front-end, you can focus on backend structure
- (5) Implement some functions, for example: add-product, find-product, change-price or others
- (6) Implement a basic authentication mechanism and role based endpoint access
- (7) Design error mechanism and handling plus logging
- (8) Write unit tests, at least for one class
- (9) Use Java 17+ features
- (10) Add a small Readme to document the project
- (11) Other information

### CHOICES REGARDING TASK (SOME IMPLEMENTATION DETAILS)
- (1) - Project location: https://github.com/vgeadau/store (branch main)
- (2) - There is a lot of verbose not only in GIT but more so on written classes.
- (3) - Store - It is a Java 17 (Corretto) Project that uses Maven.
- (4) - No frontend layer provided for this APP.
- (5) - Implemented 2 controllers
- (5.1) - StoreController - responsible with CRUD operations on Product. I have here methods such as:
- (5.1.1) getAllProducts - returns all products containing a title if provided, otherwise all products
- (5.1.2) getProductById - finds a Product by an ID
- (5.1.3) createProduct - creates a Product from the ProductDTO received body
- (5.1.4) updateProduct - updates a Product from the ProductDTO received body
- (5.1.5) deleteProduct - deletes a Product.
Products are associated to user, so one user cannot delete other user's products.
- (6) - AuthController - responsible with basic authentication API(s). I am using in this POC (proof of concept),
JWT to generate a token. 
In SecurityConfig - I configured which API(s) requires basic authentication and which don't. 
There is room for improvement here, as I am using only 2 roles and this application is viewed mostly as
a Store platform - Product Management Module, I didn't required multiple authenticated roles
So for this POC we have:
- (6.1) "unauthenticated" people that have the right to read (GET operations from (5.1))
- (6.2) "authenticated" people that are actually STORE ADMINS that applies CRUD operation on PRODUCTS

However, in a more complex application we can consider CUSTOMERS as a separate spring boot application. 
They should allowed to ORDER, BUY, SET PAYMENT METHODS, SET DELIVERY OPTIONS ETC. 
At this moment other authenticated roles were not taken into consideration.
- (7) - GlobalExceptionHandler - responsible with error handling and logging
A project specific exception was also defined - StoreException - BAD_REQUEST 400
Unexpected exceptions return with HttpStatus: INTERNAL_SERVER_ERROR 500
Multiple other custom Exceptions could have been used, however as this is a POC I only use StoreException.
Logs were added in GlobalExceptionHandler to avoid duplication.
- (8) - This project has a high coverage as can be noticed (45 tests). Some methods are fully covered.
- (9) - New features isn't necessary better features for example "record" breaks naming standard.
Instead of getUsername or getPassword we have methods named: username(), password() breaking the standard naming conventions.
Lambda and Stream collections also are problematic causing performance issues, multiplying by a factor of N the memory
requirements, as well, as being very hard to debug.
Anonymous classes for example was also a new feature that proved to be a total bomb in the code causing memory leaks.
- (10) this document
- (11) Other info:
- (11.1) I added dependency in the REST endpoints to support both XML and JSON outputs - WebConfig
- (11.2) Controllers were implemented to be light:
They should not contain code such try / catch, or conversions from entity to dto(s).
- (11.3) UserDTO doesn't contain password. This DTO is used in Product. Password was purposely skipped,
because we don't allow user to change his password while performing product CRUD operations.
- (11.4) Several motives why I choose my StoreException to implement RuntimeException and not (a checked exception):
 - Clean code / vs verbose
 - Better suited for business logic errors
 - Should bubble up to the controller layer
 - Are handled globally (e.g. via @ControllerAdvice)
 - Don’t represent programming mistakes or recoverable conditions
 - Easier integration with Spring’s exception handling
 - Avoids swallowing or over-handling errors
 - Checked exceptions often lead to catch blocks where developers may:
 - Swallow exceptions silently (catch (Exception e) {} )
 - Wrap them repeatedly in new exceptions just to compile
 - Unchecked exceptions encourage you to only catch what you intend to handle.
- (11.5) username and pseudonym have unique non-null constraints.
- (11.6) a ManyToOne relation was defined for author (product's owner)
- (11.7) H2 in memory database was chosen for this project alongside JPA.
- (11.8) Bonus implementation - Added some basic functionality for banned user. "Diego" is a banned user.
These users cannot be modified at this point in this POC, but the application can be extended to support
further functionality.
- (11.9) In order to respect D from SOLID for AuthService(authenticate method), I added a service that
provides instances of classes - ClassService - using reflection.
- (11.10) In order to respect S from SOLID methods have a single point of exist (1 return statement). It is
easier to debug (only one breakpoint instead of multiple breakpoints for each return statement).

### POSSIBLE IMPROVEMENTS, NOTES, AND OTHER INFO (WHICH WERE NOT DONE DUE TO TIME LIMITATIONS)
- (1) Extra documentation OPENAPI (Swagger) can be implemented attached to the project
- (2) Adding Jacoco for project coverage
- (3) Magic vales (numbers and text) were not externalized in all the unit tests (due time limitations), but
the source code should not have such magic values.
- (4) Use ObjectMapper instead of manual mapping, but I've chosen to avoid adding more dependencies.
- (5) Instead of having multiple API(s) for getting a product, I could use something like GraphQL which allows
writing one API while allowing user to filter the products by any combination of fields.
Although if I start to consider, we could have added multiple fields and if they are not null they could have been
used for filtering. Still the GraphQL offers this out of the box without special implementation.
- (6) BigDecimal should be used for Finance related calculus in java however I choose double for this POC
- (7) Lombok / Builder pattern could have been used to obtain / create objects with more parameters.
- (8) Product quantity not taken into consideration in the API(s). I could update GET API(s) to only
bring those products that have quantity greater.

### USER GUIDE
- (1) first start the application. The application uses default port 8080.
You can either configure a new port:
```
in application.properties:
server.port=8081
```
Or you can stop the service / kill the process that is using your 8080
```
netstat -ano | findstr :8080
tasklist /FI "PID eq 12345"
taskkill /PID 12345 /F
```

- (2) then you load "REST API for STORE.postman_collection.json" into the Postman application
- (3) then you need to "register" an user. Some things to consider here:
- (3.1) You will get a validation error if you attempt to register 2 users with same username and pseudonym
- (3.2) User "Diego" is banned - (we have an hardcoded In-Memory ban-list (for fun))
- (4) then you will need to "authenticate" with the user and pass from the registered user 
- (5) then have fun calling CRUD operations on products. With the mention here that GET API(s) are public 
also available to unauthenticated users.