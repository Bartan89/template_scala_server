Story 1 Create a Github repo within the Codestar organisation with an empty, Hello world Play (MAAK HIER MAAR AKKA HTTP VAN) application
 
 
 //kijk ook even naar Feedback vincent op vorige
 
Story 2 We’re going to be create a web-application for the road network of a small city. It should support:
• A GET request for /api/traffic-lights/{id} to fetch a single traffic light by its id. A traffic light is a JSON object of the form
  { "id": 9, "color": "Green" }
When we try to request a traffic light that doesn’t exist, we should get a 404 Not Found.
• A GET request for /api/traffic-lights to fetch all traffic lights
• A PUT request to /api/traffic-lights where you can insert or update a traffic light with a JSON object of the form described above. If a traffic light does not exist with that id, create it. If it does exist, update it. The PUT request should respond with the new state of the traffic light.
  It’s OK to use an in-memory data structure to keep track of the traffic lights.
 
Story 3 When a traffic light is "Red", we should only be able to set it to "Orange" any other update is forbidden (PUT should result in a 400 Bad Request). Likewise for the transition from "Orange" to "Green" and from "Green" to "Red"
 
Story 4 We’re only going to accept state transitions from "Red" to "Green" and vice versa now. Going from "Red" to "Green" is immediate, but going from "Green" to "Red" will take 15 seconds and the status will be "Orange" in those 15 seconds (just like a real traffic light!) A user can’t set a traffic light to orange themselves anymore, that’s just part of the process of going from "Green" to "Red", and happens automatically.
  Make sure that the request to make a light "Red" is only completed after the light is actually red (use an async action with a Future for this).
If you would request the state of the traffic light (in a different tab) within those 15 seconds, you should get the color "Orange".
  During this time, other people can also still request to make the state "Red", but those requests shouldn’t restart the timer. Suppose person A requests to make a light "Red" and after 5 seconds, person B does the same. Person has to wait 15 seconds before the PUT returns with color "Red". Person B only needs to wait 10 seconds.
 
Story 5 Add a query parameter to GET for /api/traffic-lights to fetch only traffic lights of a certain color (for example, /api/traffic-lights?color=Green)
 
Story 5 - Move the state of the application (all traffic lights) to a service and @Inject it with Play (like @Daniel Lau suggested)
Story 6 - Instead of having 15 seconds hardcoded, use a configuration file for that (put it in conf/application.conf)
 
Story 7 verplaast de in-memory structuur naar de database
Story 8 zorgt ervoor dat een service gestopt kan worden als een stoplicht op oranje staat (als ie weer opstart zal ie vanzelf weer naar rood springen)