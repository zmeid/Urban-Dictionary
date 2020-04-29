# Urban-Dictionary

![Snapshot](https://i.imgur.com/GC7ufwEl.png) ![Snapshot](https://i.imgur.com/YgkWsT7l.png)

# Used components/libraries/design patterns:

- 100% Kotlin
- Kotlin Coroutines(instead of RxJava and Java threads)
- Kotlin delegates
- Kotlin extensions
- Git workflow
- Dagger2
- MVVM
- Room
- View binding
- Livedata
- Lifecycle-Aware Custom DialogUtils
- Retrofit
- OkHttp logging interceptor
- SharedPref
- Leakcanary by Square(To catch memory leaks)
- CardView
- Swipte to refresh
- Exo Media Player
- Timber
- Recyclerview
- Mockito
- Espresso

# Some notes:

- Configuration changes are handled gracefully. If we rotate the device when:
  - Recyclerview has data, there will not be any room database query or API call. Data will be simply returned from viewmodel.
  - Sorting alert dialog is on the screen, it's state will be recovered in the new screen.
  - Playing urban sound, it's state will be recovered in the new screen. Sound will be playing where it left in previous screen.
- Http logging are enabled only in debug mode.
- I use "Region - endRegion" to make code cleaner.
- Almost all libraries/classes are injected by Dagger to classes.
- Timber is configured with custom TimberLineNumberDebugTree which provides clickable logs to navigate developer to the point where log was generated.
- I tried to document all classes and necessary methods with KDoc.
- LeakCanary helped me a lot to catch memory leaks and correct memory usage. It shows notifications when LeakCanary's object watcher detects leaks.


# Some scenarios:
- All request results are cached in local database using Room.
- When searching a word, acitivity asks viewmodel to bring the definitions and viewmodel asks repository for the definitions. Repository decides where to get them from. If the word definition results are already in database, it returns them. If not, API call is made and results are inserted to database.
- User can swipe the activity down to do force refresh. In case of force refresh, API call is made and data in database is overridden.
- If GET request fails, there will be an error message with a try again button.
  
  
# Tests:
- MainActivity Test(Instrumentation test)
  - mainActivityPlaySoundTest(): Searches the letter 'b', plays the sound of the letter. Asserts that sound player is initialized and it is visible on the screen.
- UrbanRepository Unit Test
  - testGetDefinitions():  Tests [UrbanRepository.getDefinitions]. If forceRefresh is true, urban list from [urbanApiResponseModel] should be returned. Otherwise, [urbanListFromDatabase] should be returned.

# Some example branches:
I followed git workflow. All feature branches are created from develop branch and merged back to develop branch.
- feature/main-activity-ui-design
- feature/consume-urban-api
- feature/sort-by-thumbs
- feature/share-urban-and-play-urban-sound

# Improve ideas:
- Some words come with brackets. They can be linkified.
- Implement crashlytic.
- Use DialogFragments instead of AlertDialog.
- Create a common ProgressDialog and inject with dagger whereever needed.

# Tasks before publishing the app:
- Run full lint inspection(should be done before every commit).
- Run static code analysis.
- Enable ProGuard.
- Use firebase test lab.

