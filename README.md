## Political Preparedness

Political Preparedness is an example application built to demonstrate core Android Development skills as presented in the Udacity Android Developers Kotlin curriculum. 

This app demonstrates the following views and techniques:

* [Retrofit](https://square.github.io/retrofit/) to make api calls to an HTTP web service.
* [Moshi](https://github.com/square/moshi) which handles the deserialization of the returned JSON to Kotlin data objects. 
* [Glide](https://bumptech.github.io/glide/) to load and cache images by URL.
* [Room](https://developer.android.com/training/data-storage/room) for local database storage.
* [Motion Layout](https://developer.android.com/develop/ui/views/animations/motionlayout) for widget animation. 
  
It leverages the following components from the Jetpack library:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) with binding adapters
* [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) with the SafeArgs plugin for parameter passing between fragments

### Testing

#### Unit Testing
[ElectionsViewModelTest](https://github.com/azzumw/CapstoneProject/blob/master/app/src/test/java/viewmodels/ElectionsViewModelTest.kt)

[ElectionDaoTests](https://github.com/azzumw/CapstoneProject/blob/master/app/src/androidTest/java/com/example/android/politicalpreparedness/database/ElectionDaoTests.kt)

#### Integration Testing
[ElectionFragmentTests](https://github.com/azzumw/CapstoneProject/blob/master/app/src/androidTest/java/com/example/android/politicalpreparedness/election/ElectionFragmentTests.kt)

[LocalDataSourceTest](https://github.com/azzumw/CapstoneProject/blob/master/app/src/androidTest/java/com/example/android/politicalpreparedness/database/LocalDataSourceTest.kt)

[LaunchFragmentTest](https://github.com/azzumw/CapstoneProject/blob/master/app/src/androidTest/java/com/example/android/politicalpreparedness/launch/LaunchFragmentTest.kt)

#### Espresso UI Instrumentation Testing
[MainActivityTests](https://github.com/azzumw/CapstoneProject/blob/master/app/src/androidTest/java/com/example/android/politicalpreparedness/MainActivityTests
.kt)
