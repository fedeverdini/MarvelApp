# MarvelApp
Simple App to list characters and see details using Marvel APIs from https://developer.marvel.com/docs

This app is still on development process.
### Furute features:
* UI/UX Enhacement
* Implement viewbinding
* Find character by name
* Infinite scroll on character list
* Unit tests
* Automated tests

## API Keys
To make it work, you have to set your **private and public keys** to generate the hash requested by Marvel API.
Check the file **Keys.kt**

## List configuration
* To change the **number of columns** in the list, check the value of **CHARACTER_LIST_COLUMNS** in the **Constants.kt** file
* To change the **number of elements** in the list, check the value of **CHARACTER_LIST_PARAM_LIMIT** in the **Constants.kt** file

## Libraries
* OkHttp3
* Retrofit
* Koin
* Room
* RecyclerView
* Timber
* Lottie
* Picasso
* SwipeRefresh
* ViewPager2
* TabLayout
