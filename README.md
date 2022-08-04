***Mobile App Dev - App Brainstorming***
**out of Scope
Having a very polished UI
Having a great App Idea that could be a real product

 Peer Errands


***CATEGORIZE AND EVALUATE***
### Peer Errands:
 - **Category:** Delivery/Pick-Up/ Errand Services
 - - **Mobile:** Mobile first experience. If necessary, a website may be developed to complement mobile use
 - **Story:** -
 - - **Market:** For anyone who needs to have something done for them during times they cannot do it for themselves. This app gives them the ability to delegate responsibilities to other people(peers) within the immediate demography/neighborhood
 - **Habit:** Individuals use this day to day to make sure their respnsibilities are met without placing themselves in positions of inconvinience. Peers who carry out responsibilities they're tasked with are given a bump in rating by the delegators, hence people would be more trusting in delegating important (and probably more expensive) responsibilities to them
 - **Scope: ** V1 would allow people to place the task they want another to carry out in their stead plus an amount they'd be willing to pay. Peers will then be able to request to carry out the task listed. The people who listed the task would then choose a person among the requests based on their rating and previous recommendations. After the responsibilty has been carried out and both parties have acknowledged completion, both parties can then settle on a mode of payment(out of scope)
 - **Hardware and extras:
 - Maps: - shows tasks available in immediate demographic setting
         - could implement live tracking of person carrying out errand(if need be)
- Can implement filters to sort out tasks either by time, proximity or money Involved


Story/Scenario Play:
Person (let's call him/her A) has to go get his laundry from the laundromat but something came up at work so he/she realizes it'd be impossible to pickup his/her stuff from the laundry in preparation for the very-important wedding he/she has to attend the next day. He/She has no friends available during the laundry pick-up window hence no one to delegate this task to.
A then opens Peer Errands and posts the details of the task he wants to delegate on the timeline. -
"Hi, Unfortunately I can't make time to go pick up my laundry and It's absolutely imperative i get it today. The laundromat is called Garrett's Laundromat on 1815 Terry Ave street. You can drop them off infront of Platform nine-and-three-quarters. - 20$"
Person B, who has the whole day to himself, decides to hop on peer errandds to make some money. He sees Person A's task listed and he realizes it's just a 5 minute walk from his current location. He then sends in a request to handle the task.
Person A, after reviewing Person B's profile and satisfied with the reviews and ratings Person B has, accepts B's request.
Person B then goes to the laundromat, picks up the laundry, delivers it to the location, takes a picture as proof and marks the task as complete. Person A sees the proof of delivery, sends Person B the money and then marks the task as complete. Both people leave reviews and ratings for each other. Person B walks away 20$ richer and Person A is settled knowing he/she can attend his/her bestfriend's wedding the next day.


WIREFRAMES:

![IMG_5370](https://user-images.githubusercontent.com/90366540/174722719-58c721ee-07b4-4e66-9a78-269862100776.JPG)



**USER CLASS**

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | UserID        | String   | unique id for the user post (default field) |
   | ProfileImage  | File     | profile image of current user |
   | TaskerRating  | Number   | rating of User who posts tasks |
   | FreeLanceRating| Number  | rating of User who completes tasks |
   
   
   **REQUEST CLASS**
   
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | Pointer to UserID| String| pointer to the User |
   | RequestsTitle | String   | Request's title |
   | RequestsDes   | String   | Request's description |
   | CreatedAt     | DateTime | Date when the request was created |
   | CompletedAt   | DateTime | number of comments that has been posted to an image |
   | Completed     | Boolean  | whether or not the request has been completed |
   | ToDo list     | array    | list of things to do to complete the request |
  

Possible Stretch features:
- User profile pictures ( on timeline and in detail activity)
- Implementing a map feature
- Using the camera app to take a picture after task is completed
- Chat app integrated for users to hold a conversation ( very improbable...maybe after MU)



**_NETWORKING_**
- Home/Timeline Screen
-  * (READ/GET) Query all posts where boolean completed is false
-  * ( Create/POST) Create and post a new Task
-  * (Delete) Delete an existing task
-  *
-  

**_EXISTING API ENDPOINTS_**
An API of Google Maps

**_PLANNED SDK USAGES_**
- Camera Implementation



Requirements:
SDK/Parse Integration: App intereacts with parse and maps api
User authentication: App allows users to log in and sign up
Visuals and interactions: 1. Gesture: Implement pinch to zoom in and out of maps
                          2. Uses External Library Glide
                          3. Implements basic fade in/out transitions
                          4. Might use GIF as visual eye catcher on splash screen


MVP:
Users should be able to Sign up, log in and Log out. In addition to these, Users must be able to view listed tasks in the home timeline, add tasks, request to complete tasks and mark tasks as completed.

Walkthrough of app:

1st Walkthrough:
https://user-images.githubusercontent.com/90366540/182958213-04538055-939f-44d8-b2ff-573b79a66dfd.mov

2nd Walkthrough(Higher Res, Slower speed):

https://user-images.githubusercontent.com/90366540/182958557-e99e2bb3-4442-469c-8b77-d2f402810dfe.mov


