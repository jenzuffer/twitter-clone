# Twitter-clone
## Task 1
See Assignment 5.

## Task 2
Implement the tiny twitter clone found at this repo, using only Redis as a data store.

You need to implement the two classes PostManagementImpl and UserManagementImpl, and to run the unit tests by right clicking the Java folder under Test, and choosing Run ‘All Tests’.
You are allowed to change the interface and DTOs, just write a small readme listing the changes.

## Task 3
In a readme, write a short explanation of your redis data model. It should be clear enough for a developer to be able to implement the same thing.

redis data model: Our attempt focuses on jsonifying the values in the jedis keyvalue map. We make
use of redis sets and getting information about members of sets to keep track of states. 
We would condeem sets and json as sufficient for representing the wished behvaiour. We also tried
implementing an actual jediPubSub which we called subscribe but did not get to use it. 
Essentially we just use strings to keep track of users followers and users following with strings and 
usernames. These are stored in sets to be kept track of. 
