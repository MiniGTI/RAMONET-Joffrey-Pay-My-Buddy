# RAMONET-Joffrey-Pay-My-Buddy
Application to transfer money between friends easily.
This app uses Java to run and stores the data in Mysql DB.
***
## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.
***
### Prerequisites
What things you need to install the software and how to install them.

    - Java 21.0.1
    - Maven 3.6.3
    - Mysql 8.0.35

### Running Application
Post installation of MySQL, Java and Maven, you must create the database. 
To do this, open your MySQL terminal, the application is configured with:

`port: 9090`
`username: root`
`password: rootroot`

If you want to change configuration and adapt the application to your local configuration, simply update the application.properties in the resources folder.

After that, copy:

`CREATE DATABASE 'prod_paymybuddy'`
If you want to change the database name, update the application.properties.

Next, copy:

`USE 'prod_paymybuddy'`

Open the project in your favorite IDE and run the application file at the root of the paymybuddy folder.
The application creates tables and injects data into them.

### Testing Application
The application includes unit and integration tests. To run them, run the following command:

`mvn test`
