# Teller Machine

* A relatively simple implementation of an 'online' bank account. 
* A new user can create an account with basic information, including email address. 
* Email is used to process requests for account details change.
* Allows for exceptions where the user can reset a password using time sensitive code that enables the user to create a new password. 
* Simple actions like deposits and variations of withdrawing money are allowed.
* Account activity can be viewed in the form of a statement.
* Export Statement as PDF file
* Email the  Statement to the registered email of the user

## Getting Started

****Currently Not ready for deployment****

### Prerequisites

What things you need to install the software and how to install them:
- Jfeonix
- SQLite connector via JDBC
- Javamail API
- JavaBeans Activation Framework
- IText 7

```
jfoenix-8.0.1.jar
sqlite-jdbc-3.23.1.jar
activation.jar
mail.jar
forms-7.1.2.jar
io-7.1.2.jar
kernel-7.1.2.jar
layout-7.1.2.jar
slf4j-api-1.7.25.jar
slf4j-simple-1.7.25
```
## Deployment

Uses a local database so a simple download of .jar file will handle everything

## Built With

* MVC Framework

## Author

* **Mani Shah** - *Initial work* - [PurpleBooth](https://github.com/Mani9723)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Thanks to StackOverFlow for help with bugs
