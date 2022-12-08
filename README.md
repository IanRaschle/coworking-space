# Coworking Space
This Application is a prototype for an API of a coworking space. It implements all the planed requirements and the Api is constructed as planed in the interface planing.
If there are any differences between the planning and the implementation I've documented them at API Differences with planing.
## API
### Differences with planing
* Update booking endpoint
  * Planned: Takes an id as PathParam which is the id of the booking which should be edited
  * Implemented: The endpoint hasn't the id as PathParam. It was simpler to transmit it directly in the body of the request with all the other booking data
* Get ApplicationUser endpoint
  * Planned: There's no such endpoint (I've forgotten it)
  * Implementation: I implemented this endpoint so the admin can also get a specific user
* Role VISITOR
  * Planned: There should be a Role for a visitor
  * Implementation: This Role is unnecessary because a visitor is never stored in the DB, so I haven't implemented it
* Change Email or Password endpoint
  * Planned: I've planed to submit the id of the User whose email or password should be changed in the path
  * Implemented: To assure that everyone only can change his own email or password it's easier to extract the email from the Jwt. With this way the user only can change his data if he's logged in
* Response Codes Of update Booking
  * Planned: I've had planed that this endpoint has 3 response codes but forgot to add one if the submitted booking is invalid
  * Implemented: I implemented that mentioned Response
* Update ApplicationUser
  * Planned: I set the method of this endpoint to POST
  * Implemented: Because PUT ist the correct method for this type of endpoint I implemented ith with the PUT method

# Set up
Clone this git repo, install Visual Studio code and open this project in VS code. There are some extensions you have to download. These Extensions are DevContainers and Java Pack Extension.
Reopen the project in the Dev Container and assure that the Java and Quarkus extensions are installed.

## Start / Test
### General information
To be able to start/test this project you have to generate the keys for the jwt. https://quarkus.io/guides/security-jwt. This is only the case if you clone the repo, cause these keas are in the .gitignore.
To Debug the project you can run Quarkus debug current project or to test the project you can just run the tests in the folder test.java.ch.zli.iraschle

### Testing
#### Test Data
##### User
`
{
"firstname": "hans-ueli",
"lastname": "bra",
"email": "hans.bra@gmail.com",
"password": "admin"
}
`

##### Booking
`
{
"date": "2022-12-12",
"duration": "FULLDAY",
"state": "PENDING",
"applicationUser": {
"id": {id},
"firstname": "{firstname}",
"lastname": "{latsname}}",
"email": "{email}",
"password": "{password}",
"role": "{role}"
}
}
`
