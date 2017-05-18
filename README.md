# CarousellTest

This app was implemented for Carousell request. It follows basic MVP architecture. App allows the user to upvote or downvote a topic several times and returns a sorted list of top 20 topics (sorted by upvotes, descending) on the homepage.

### Designing the app

The app consists of two UI screens: 
* Topics - Used to manage a list of topics.
* AddTopic - Used to create topics.

Each screen is implemented using the following classes and interfaces:

* A contract class which defines the connection between the view and the presenter.
* An Activity which creates fragments and presenters.
* A Fragment which implements the view interface.
* A presenter which implements the presenter interface in the corresponding contract.

A presenter typically hosts business logic associated with a particular feature, and the corresponding view handles the Android UI work. The view contains almost no logic; it converts the presenter's commands to UI actions, and listens for user actions, which are then passed to the presenter.

