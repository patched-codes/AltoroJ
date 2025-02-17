# AltoroJ Banking Application

**WARNING**: This application contains security vulnerabilities. Run it only in a backed-up and sheltered environment (such as a VM with a recent snapshot and host-only networking) and at your own risk, especially if you enable some of the advanced options described below!

## Overview
AltoroJ is a sample banking J2EE web application designed to demonstrate application security vulnerabilities. Built with standard Java & JSP functionality, it provides a simple and uncluttered platform for learning about real-life application security issues without the complexity of additional frameworks.

The application uses Apache Derby as its SQL database, which is automatically initialized on first login. Transactions and operations are stored in this database within a repository folder called "altoro" in your OS home directory (e.g. C:\Users\[your_username] or /Users/[your_username]).

## Features
- Standard J2EE/JSP implementation without additional frameworks
- Automatic database initialization
- REST API with Swagger documentation
- Configurable security vulnerabilities for learning/testing
- Sample banking functions including:
  - Account management 
  - Fund transfers
  - Transaction history
  - Credit card applications
  - User feedback system
  - Administrative functions

## Credentials
Default login credentials:
- Regular user: jsmith/demo1234
- Admin user: admin/admin

## Prerequisites
- Eclipse 4.6 or newer (requires Java 8)
- Tomcat 7.x
- Gradle 3.0 (for command line builds)
- Gradle Buildship Eclipse plugin (for Eclipse builds)
  - Install via Eclipse Marketplace (Help -> Eclipse Marketplace)

## Getting Started
For detailed setup instructions, see [Importing AltoroJ into Eclipse from GitHub](https://github.com/AppSecDev/AltoroJ/blob/master/Importing%20AltoroJ%20into%20Eclipse%20from%20GitHub.md)

## Advanced Configuration
AltoroJ includes advanced configuration options that can enable additional functionality and security vulnerabilities. These are disabled by default for stability and safety. See WEB-INF/app.properties for details on available options.

## REST API
The application provides a REST API documented using Swagger. Access the API documentation through the REST API link in the application footer.

## Public Demo
A public demo version is available at http://altoromutual.com:8080/. Note that advanced options are disabled on this instance.

## Troubleshooting

### Database Creation Error
If you receive "Failed to create database 'altoro'" on login:
1. Verify you can log in with jsmith/demo1234
2. Check the Eclipse Console or catalina.out for "user.home=" path
3. Either:
   - Grant write permissions to the Tomcat user for that directory
   - Change DB location via -Duser.home="<new_path>" Java argument

### Compilation Errors
If you have compilation errors in Eclipse:
1. Run the Gradle build to download required libraries
2. Refresh project dependencies

## License
This project is licensed under the [Apache License 2.0](LICENSE).

## History
AltoroJ was created in 2008 and has been used worldwide for security education, vulnerability demonstration, and academic curricula. While stable, bug reports and vulnerability exploits are welcome!
