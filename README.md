# Stanley-Kanembirira-H230755W
## Secure coding mini project


## Overview

The Secure Student Portal is a Java-based desktop application designed to manage student information securely. The application offers functionalities such as user registration, login, and password management, all while ensuring high-security standards to protect user data. I uploaded the java file and the database file.

## Features

- **User Registration**: Students can sign up by providing their details, including name, age, sex, phone number, part, department code, registration number, email, and password.
- **User Login**: Students can log in using their email and password. The application locks the account after multiple failed login attempts.
- **Password Management**: Users can change their passwords securely and recover forgotten passwords.
- **Display Page**: Displays user information after a successful login.

## Security Measures

**Password Hashing**: 
   - Passwords are hashed using the `BCrypt` algorithm before storing them in the database. This ensures that passwords are stored in a non-reversible format.

**Input Validation**:
   - The application validates user input to ensure all fields are filled out correctly. This helps prevent SQL injection and other malicious inputs.

**Captcha Verification**:
   - During the registration process, users must complete a CAPTCHA challenge to prevent automated sign-ups.

**SQL Injection Prevention**:
   - Prepared statements are used for all database queries, which helps prevent SQL injection attacks.

**Password Strength Checker**:
   - The application includes a password strength checker to ensure that users choose strong passwords containing at least one uppercase letter, one lowercase letter, one digit, one special character, and between 8 and 64 characters long.

**Session Management**:
   - The application manages user sessions to ensure that users are logged out after a period of inactivity.

**Error Handling**:
   - Generic error messages are used to prevent revealing internal system details to potential attackers.

**Secure Connections**:
   - SSL (Secure Sockets Layer) is enabled for database connections to ensure data transmitted between the application and the database is encrypted.



## Testing
Unit tests are included to ensure the application functions correctly and securely. Below are the details and explanations of the tests:

Unit Tests
- SignUpPageTest
- **testCaptchaGeneration()**: Verifies that the CAPTCHA generation method creates a CAPTCHA string that is not null and is exactly 6 characters long.

- PasswordStrengthCheckerTest
- **testPasswordStrength()**: Checks that the PasswordStrengthChecker correctly evaluates a strong password as having a strength of 100 and a weak password as not having a strength of 100.

- MySQLConnectorTest
- **testConnection()**: Ensures that a connection to the database can be established and that the connection is not null.

- MainPageTest
- **testMainPageInitialization()**: Verifies that the MainPage frame initializes correctly, is not null, and has the correct title.

- LoginPageTest
- **testPasswordHashing()**: Ensures that the BCrypt hashing and verification work correctly for given passwords.

- DisplayPageTest
- **testDisplayPageInitialization()**: Verifies that the DisplayPage initializes correctly and that the frame is visible.



## Dependencies

- **BCrypt**: Used for hashing passwords securely.
- **JUnit**: Used for unit testing to ensure code quality.
- **MySQL Connector**: Facilitates connection between the application and MySQL database.


## Running the project
Build the project using maven
`mvn clean install`

Run the application
`mvn exec:java -Dexec.mainClass="securecoding.example.MainPage"`

Running Tests
To run the tests, use:
`mvn test`



## Database Management
I uploaded the database file that contains 'secure_page' database and 'signup' table For program testing i included in the database the following user details:

| name | surname | age | sex | phone\_number | part | department\_code | registration\_number | email\_address | password |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| user | example | 25 | f | 123456789 | 4 | isa | h123456a | h123456a@hit.ac.zw | $2a$10$bjIyfdiD2L2oggS5RmxfEeyKYou5ll3TB8jVJorqHWR5FI1W1.y8O |

**Email - h123456a@hit.ac.zw      Password - Ex@mp1ee**




## Known Issues

- **False Error**: Where the signup table is used, the IDE shows the following error, "Unable to resolve table 'signup'", but the program runs anyways without any runtime error. Make sure all dependencies are working properly.
- **Database Connection**: Ensure the MySQL server is running and the connection details are correct in `MySQLConnector`.




