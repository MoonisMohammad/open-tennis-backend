## About this repo

 The Back-end deals with getting data from devices and storing this data in the database. API is the acronym for Application Programming Interface, it is the intermediary interface that allows the front-end to get data stored in the database columns. The frontend uses the API interface to Create, Read, Update, and Delete data inside the database. For any action, the front end wants to perform on data in the database it needs an API.

The backend has an API key to make sure only registered devices are allowed to upload data to the server through the API. The users need to login to get data from the backend. The Backend API provides a role-based structure to enable facility manager and admin roles apart from your general user 

## Tech stack / framework used

```Spring framework``` together with embedded PostgreSQL to implement this project

## Installation steps

1. Install Java - Download and install ```java JDK 11``` from https://openjdk.java.net/projects/jdk/11/. 
2. Install rails - Download and install ```PostgreSQL``` set user postgres password to ```moonis221199```
3. cd into target folder ```target```
4. run command ``` java -jar demo-0.0.1-SNAPSHOT.jar```
5. Open the url shown on the command prompt after running above command in Chrome browser to access the application (http://localhost:8080/)

## Guiding principles

This project was designed with model-view-controller (MVC)  design pattern. The MVC  design pattern separates an application into the following components:

- Models for handling data and business logic 
- Controllers for user interface and application logic
- Views for handling API's for graphical user interface objects and presentation

## Contact

Moonis Mohammad

moonizmohammad@gmail.com

https://www.linkedin.com/in/moonis-mohammad/

