# Coworking Space

## API
### Differences with planing
* Update booking endpoint
  * Planned: Takes an id as PathParam which is the id of the booking which should be edited
  * Implemented: The endpoint hasn't the id as PathParam. It was simpler to transmit it directly in the body of the request with all the other booking data
