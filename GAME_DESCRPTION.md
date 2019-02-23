# AnswerMe Quiz Game Description

The main game logic of the application is in a basic quiz format where the user is presented with a set of categories to select from. Once, the user has selected a category, a URL is constructed according to the chosen category and a network request is made. This request is made using Android’s recently released component called Volley. Volley helps create a request queue and runs the request on another thread. Once the response has been obtained, a callback method is called. While the JSON request is in process, the user is shown a progress dialog to ensure the user gets feedback.

The category list is created by using a custom adapter. The adapter enables to modify the ListView with the image and text that is required for the game.

Following this the user, is presented with one question at a time with four choices out of which only one is correct. Correct answer takes the user to the next question until the game is won. Wrong answer results in the end of the game. All the positions of the answer choices in the quiz are shuffled using the Collections class. The questions have to be answered within 15 seconds.

The application follows a MVC architecture by default, as Android’s directory structure makes it easy to follow the MVC Architecture. There are custom classes for Category, Player and QuestionModel. They all implement the Parcelable interface and therefore can easily be passed within intents. The Java classes, layout XMLs and the resources are in different directories thereby enabling an MVC approach.

The design uses SQLite for data persistence. This is done by extending the SQLiteOpenHelper Class. This gives us easy to use methods that can be modified and overridden to add data to database and get data from the database.

ListViews are used for category selection with custom adapters. The High Score activity also uses the ListViews. It sources the list of players with their high scores from the SQLite Database. For each unique player, their name and the top score will be displayed. The list is also sorted according to the top score by using Collections class and the custom sort method.

Network requests are made in a separate class which fetches the data from the OpenTriviaDB API and then is parsed to get the questions, correct answers and incorrect answers. Validations are made to check errors and display prompts to the user. Errors can be like network unavailability, bad request, no data etc. A set of questions in an ArrayList of QuestionModel is then sent to the Quiz Activity.

Audio effects and theme music is included into the game to improve the user feedback. Effects are played on right answers, wrong answers, game won etc. Also, a theme music is played during the main gameplay. Android’s MediaPlayer class was used to implement this feature. 

Error validations are done within the application in multiple places to maintain the flow of the game play and improve the user experience. Failed network requests or erroneous network requests are handled gracefully. 

Additionally, application also validates user input data and shows appropriate prompt for the same.
