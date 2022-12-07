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
