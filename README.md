# Ozi
Ozi is an Android app built entirely with Kotlin and Jetpack Compose. It follows Android design and development best practices and is intended to show my skill as an Android Developer.

The app will continue to be developed. Features and best-practices will be implemented in a reasonable and useful fashion in order to actually improve the app as well as showcase my skill and knowledge as I improve. Updates will be regularly pushed here. Stable variant is available on the Play Store. [link]

# Features
Ozi is a simple chat app. Users choose a unique username, password and an avi-icon from a provided set. Users can search for other users of Ozi; a list of suggested users is also provided. Users can also play a speed-typing game; both with chat-mates and in groups of up to 5 players. Light and dark themes are supported.

# Development Environment
Ozi uses the Gradle build system and can be imported directly into Android Studio.
There will be errors where references are made to secret objects, such as the URL to the backend server, service account credentials, etc.

# Architecture
Ozi follows the [official architecture guidance](https://developer.android.com/topic/architecture). Notably, it is a single-module app. I do not think the app is yet large or complex enough to benefit from the advantages of multi-modular architecture.
The app architecture has three layers: a data layer, a domain layer and a UI layer.
The architecture follows a reactive programming model with unidirectional data flow. With the data layer at the bottom, the key concepts are:
-	Higher layers react to changes in lower layers.
-	Events flow down.
-	Data flows up.
-	The data flow is achieved using streams, implemented using Kotlin Flows.

## Example: Displaying Chats on the Home Screen
Chats are shown to the user when the home screen is opened. The following are events take place to achieve this.
-	`HomeViewModel` calls 1GetChatsUseCase` to obtain a stream of ui-chats
-	`GetChatsUseCase` gets this stream of ui-chats by combining streams of data from the users repository, message repository and chat repository
-	The repositories all rely on a local Room database through APIs exposed in the `UserDao`, `MessageDao` and `ChatDao` respectively which they depend on respectively.
-	Data changes to chats, messages or users in the database is emitted into the streams that the DAOs provide to the repositories, and that in turn is provided to the `GetChatsUseCase`.
-	Changes are made to the database when there is a new message. And this is initiated by a push notification made possible through Firebase Cloud Messaging (FCM) APIs.
-	The onMessageReceived() in the Firebase Service starts `ProcessSignalsWorker`, which invokes `ProcessSignalsUseCase`
-	`ProcessSignalsUsecase` calls the message repository’s syncMessage function which in turn calls `OziRemoteService` to execute the actual API request using Retrofit.
-	Message repository saves the new messeges to the database which triggers emission of new data into the streams already mentioned above. Similar operations also happen for syncing users and chats.
-	When `HomeViewModel` receives the ui-chats it provides the data in the form of `HomeUiState` which is exposed through a flow to `HomeScreen` which then renders the information on screen.

## Data layer
The data layer is implemented as an offline-first source of app data and business logic. It is the source of truth for all data in the app.
Repositories are the public API for other layers, they provide the only way to access the app data. The repositories typically offer one or more methods for reading and writing data.

### Reading data
Data is exposed as data streams. This means each client of the repository should be prepared to react to data changes. Data snapshots are sometimes provided too. For example, the ThisUserRepo provides snapshot of user from which some use-cases get the user id to perform some logic.

_Example: Read a list of chats_

A list of chats can be obtained by subscribing to `ChatRepo::getChatsFlow` flow which emits `List<Chat>`
Whenever the list of chats changes (for example, when a new chat is added), the updated `List<Chat>` is emitted into the stream.

### Writing data
To write data, the repository provides suspend functions. It is up to the caller to ensure that their execution is suitably scoped.

### Data sources
A repository may depend on one or more data sources. For example, `MessageRepoImpl` (the default implementation of MessageRepo) depends on the following data sources:

Name | Backed by | Purpose
---|---|---
MessageDao | Room/SQLite | Persistent relational data associated with Messages
OziRemoteService | Remote API accessed using Retrofit | Chat data provided through REST API endpoints as JSON
OziDataStore | Preference DataStore | Persistent key-value data associated with the user, to get the user id and token

## Domain layer
The domain layer contains use cases. These are classes with methods that contain business logic. Some contain single invocable method (operator fun invoke), while others contain a group of very closely related methods. For example, the `NofiticationUseCases` has methods for posting a new message notification, posting a new game request notification, clearing notifications, and replying to a chat via notification.
These use-cases are used to simplify and remove duplicate logic from ViewModels. They typically combine and transform data from repositories.
For example, `GetChatsUseCase` combines a stream (implemented using Flow) of `Message`s from a `MessageRepo`, a stream of `User`s from `UsersRepo` and a stream of `Chat`s from `ChatRepo` to create a stream of `UIChat`. This stream is used by both `HomeViewmodel` and `GroupGameSetupViewModel` to display chats on screen.
All ViewModels depend on the UseCases except for `ExploreViewModel` which depends on `SearchHistoryRepo` directly.

## UI Layer
The UI layer comprises:
- UI elements built using Jetpack Compose
- Android ViewModels

The ViewModels receive streams of data from use cases and repositories, and transforms them into UI state. The UI elements reflect this state, and provide ways for the user to interact with the app.

### Modeling UI state
UI state is modeled as immutable data classes. State objects are only ever emitted through the transform of data streams. This approach ensures that:
- the UI state always represents the underlying app data
- the app data is the source-of-truth.
-	the UI elements handle all possible states.

### Processing user interactions
User actions are communicated from UI elements to ViewModels using regular method invocations. These methods are passed to the UI elements as lambda expressions.

_Example: Start a chat from the ExploreScreen_

The `ExploreScreen_simple` composable takes a lambda expression named `onExploredUserClicked` which is supplied from `ExploreViewModel.createChat(user)`. Each time the user taps a user on the explore screen, the method is called. The ViewModel then processes actions that create the chat and eventually navigates to the chat screen. 

# Testing
To facilitate testing of components, Ozi uses dependency injection with Hilt.

Most data and domain layer components are defined as interfaces. Then, concrete implementations (with various dependencies) are bound to provide those interfaces to other components in the app.
Fakes and mocks are the two types of test-doubles used. [Mockito](https://site.mockito.org/) is the mocking library used for mocks. Fakes are used to provide dependencies in the data and domain layer tests. Mocks are used in ui layer for view model and individual compose screen tests.
Fakes that are expected to provide data can be provided with test data usually through a constructor parameter.
E2E tests use actual production implementations as much as possible for fidelity. The exception is `OziRemoteService` which is faked to avoid test flakiness.
To run the tests in Android Studio:
-	Right click and run all tests in the ‘test’ source set to run all unit tests.
-	Right click and run all test in the ‘androidTest’ source set to run all instrumented tests including E2E tests.

# UI
The app was designed using Material 3 guidelines. 
The Screens and UI elements are built entirely using Jetpack Compose.

The app has light and dark modes.
The app currently has 5 normal screens and 3 onboarding screens, viz;
- Normal screens
  -	Home screen: The landing screen for a logged-in user. Displays the user avi and username on the bar with buttons and menus for theme switch and navigating to other screens. And then a list of chats.
  -	Chat screen: Where user chats with other users. Shows the avi and username of the chat-mate and messages, and allows the user to start a speed-typing game with the chat-mate.
  -	Explore screen: Shows a list of suggested users to start a chat with. Also allows the user to search for any particular user.
  -	Group game setup screen: Allows the user to start a group game of up to 5 players. Shows a list of users derived from current chats. Also allows the user to search for any other user of Ozi.
  -	Profile screen: Shows info about the user. Allows to update profile info.
  -	Developer screen: Simple screen that shows a brief information about the developer and some contact info.

- Onboarding screens:
  -	Landing screen: landing screen for first-time / logged-out users. Provides buttons to navigate to Register or login screens.
  -	Register screen: Where the user can signup by selecting avi, providing username and password.
  -	Login screen: Where existing users can login by providing username and password.

