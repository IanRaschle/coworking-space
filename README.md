# Coworking Space

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

## Testing
### Test Data
`
{
"firstname": "ian",
"lastname": "raschle",
"email": "ian.raschle@gmail.com",
"password": "admin"
}
`
