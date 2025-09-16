# Student Management System

## Overview
The **Student Management System** is a Java-based application with a MySQL database that allows students, professors, and administrators to interact through a unified platform. It supports course management, grade tracking, study groups, and a built-in messaging feature.  

## Features
- **Students**
  - Enroll in and drop courses  
  - Join/leave study groups  
  - View grades and activities  
  - Send messages to group members  

- **Professors**
  - Manage courses and activities (labs, seminars, lectures)  
  - Assign and edit grades  
  - Export student grades  

- **Admins & Super Admins**
  - Manage users (students, professors, admins)  
  - Manage courses and enrollments  
  - Super admins can manage admin accounts  

## Technologies Used
- **Programming Language:** Java  
- **GUI Library:** Swing  
- **Database:** MySQL  
- **Connectivity:** JDBC  
- **Build Tool:** Maven/Gradle  
- **Version Control:** Git  

## Usage Guide
1. Import the provided SQL scripts to set up the database (`Proiect_PlatformaDeStudiu2`).  
2. Update the `DatabaseConnection` class with your DB username, password, and URL.  
3. Run the project from your IDE (IntelliJ, Eclipse, or NetBeans).  
4. Log in as **Student**, **Professor**, or **Admin** to explore role-specific features.  

## Development Notes
- Includes UML diagrams (Use Case, Class, Package)  
- Known issues: UI resizing and concurrency in some operations  
- Planned improvements:  
  - Real-time chat and notifications  
  - Multi-language support  
  - Improved validation and error handling  

## Contributors
- **Blaga Alexandra**
- **Boca Alina**
