# SeniorProject
Discoop
Discoop Documentation
Prepared by Crissy Leach and Sam Kanner
COMP-5501-13 - Prof. Wiseman
August 6th, 2018


Overview
Discoop is an Android app designed for people on a budget, college students, or anyone interested in seeking a deal. The app is intended to utilize crowdsourcing to gather information on discounts so that other users may share and see sales that are happening near them. This version of the app is not able to share information with other users, as the information is stored locally on the device. Users can use Discoop to organize discounts they discover for future use by saving information on the discount type, location, name, details, and expiration date. Users are able to submit information on discounts, search through previously submitted discounts, and view them on the map. 


User Guide
●	Home/ About
○	Click the Discoop application icon to open the app. The user is directed to the homepage on launch. The homepage displays several elements. In the top right corner of the screen, in the app bar, there is an option button. Clicking the button displays “About Discoop”. Upon clicking this option, the user is directed to the about page which displays more information about the app itself as well as the team that developed the app. The about page is available from every other page in the app except on the maps page. Clicking the back button in the top left corner of the page will bring the user back to the homepage. Going to the about page from any other page will produce the same results, but the back button will return the user to the page they were previously on. 
○	On the homepage, in the top left corner in the app bar, there is a hamburger button. Clicking this will open a drawer on the left side of the screen displaying several options. This navigation drawer is available on all pages except the maps page. The first option is “Home”, which takes the user to the launch page. 
●	Map
○	The second option in the navigation drawer is “Map”. Clicking this option takes the user to the map. If the app has been used before and discounts have been submitted previously, clicking this option will bring the user to the maps page and will display markers on the location of each submitted discount. If no discounts have been submitted, the user will see a marker representing their current location. 
○	Returning to the homepage, the user can access the map from the two buttons shown in the middle. Once discounts have been submitted to the database, the user can click the button labeled “Scoop Up The Nearest Discount”. This will show the user the marker on the map representing the closest documented discount to their location. On the homepage, the user can click the “Scoop Up All Discounts” button, and the user will be directed to the maps page displaying a marker for each discount stored in the database. If there is nothing in the database, the user will only see their current location. The only way to return to the homepage, search page, or submit page from the maps page is by clicking the back button on their phone. 
●	Search
○	The third option in the navigation drawer is “Search”. Click this will bring the user to the search page through which they can search through the discounts that have already been submitted. If discounts have been submitted, all of them will be listed under the search bar. The user can click the search bar at the top of the page in the app bar and can type any term they wish to search. If there is a match, the list will shorten to the discounts that match the query. If there is no match, or there are no discounts stored in the database, the search page will appear empty. Clicking an item in the list will direct the user to the map and place a marker identifying the discount’s location. 
●	Submit
○	The final option in the navigation drawer is “Submit”. This will bring the user to the submit page. In this page, the user can submit information on a discount to the database. Here, they will see several fields in which they can input information.
○	The first field is the “Type” field which contains three checkboxes. The user can click the most appropriate checkbox corresponding to the discount they would like to submit. The options are “Restaurant/Bar”, “Retail”, or “Other”. The next field is “Location” in which the user must input the address of the discount location. This is separated into two text input fields, one for the street address and the other for the state. The next field is “Establishment Name” for the name of the establishment. The next field is “Details” where the user can type in the exact information on the discount, such as how to receive the discount. The final field is “Expiration Date” which the user can scroll through the month, day, and year to pick the date the discount will be valid through. 
○	Clicking the “Submit” button at the bottom right of the page will take the information the user provided and stores it in the database. This information will be available through the search page and the maps page. When the button is clicked, the page will refresh, clearing all fields allowing the user to submit another discount. 


Developer Guide
Discoop was developed in Android Studio using Java and XML. 
●	MainActivity.java
○	The MainActivity class controls the application’s Homepage and allows users to navigate throughout the app using the button to find the nearest discounts, the button to show all discounts, or the navigation drawer. This class asks for location permissions from the user and then gets the user’s current GPS coordinates.
○	The MainActivity class implements NavigationView.OnNavigationItemSelectedListener to be able to use the Navigation Drawer.
○	The onCreate method creates the toolbar with a button to go to the AboutDiscoop.java class, and navigation drawer with options to go to the MainActivity class, the MapsActivity class, the submit class, and the SearchActivity class. This method also creates a location to store the user’s device latitude and longitude. 
○	The getLatitude method creates a string from the user’s device’s current latitude.
○	The getLongitude method creates a string from the user’s device’s current longitude.
○	The onBackPressed method controls the closing of the Navigation Drawer.
○	The onCreateOptionsMenu creates the menu in the app bar.
○	The onOptionsItemSelected controls the selection of the About button in the app bar.
○	The onNavigationItemSelected controls the selection of the items listed in the Navigation Drawer.
○	The showMap method changes activities from MainActivity to MapsActivity.
○	The showClosest method retrieves the device’s current location and stores it as latitude and longitude. Then it gets a list of all discounts stored in the database. It loops through each discount in the list, gets the address, and converts the address to latitude and longitude. The user’s latitude and longitude are passed to the distance method, along with the discount’s latitude and longitude, which calculates the distance from one set of coordinates to another. The distance is stored in a temporary variable. If the distance for the next discount is less than the temporary distance, then that variable is stored in the temp instead. The temporary distance variable will have the shortest distance at the end of the loop. The information(name, type, details, expiration date, location) about the discount that produced the shortest distance will be put in an intent as extras which will be passed to the activity that requires the nearest discount. 
○	The getLatFromAddress method takes in a string address and uses the Geocoder to get a list of addresses matching the input address. The method returns the latitude from the first address in the list.
○	The getLngFromAddress method takes in a string address and uses the Geocoder to get a list of addresses matching the input address. The method returns the longitude from the first address in the list.
○	The distance method takes in a latitude and longitude for two different addresses. It uses the Location object’s distanceTo function to calculate the distance between the two addresses. 
○	The aboutPage method changes activities from the MainActivity class to the AboutDiscoop class. 
○	The onStart method checks location service permissions, requests location service permissions, and gets the user’s location on application start.
○	The onRequestPermissionsResult method determines if location service permission was granted or denied by user.
○	The showSnackbar method creates a popup for the user to allow or deny location services.
○	The checkPermissions method checks if permission was granted for location services.
○	The requestPermissions method requests permission for location services from the user. 
○	The method getLastLocation gets the user’s device location.
○	The resource files used are:
■	res/drawable/header - background image of the Discoop logo
■	res/drawable/button_bg_round.xml - custom button for “Scoop Up All Discounts” button, not actually round
■	res/drawable/button_bg_round2.xml - custom button for “Scoop Up Nearest Discount” button, not actually round
■	res/layout/activity_main.xml - layout file to include the navigation drawer
■	res/layout/app_bar_main.xml - layout file for the app bar to include a toolbar
■	res/layout/content_main.xml - layout file to include the text views, buttons, and images displayed on the main page
■	res/layout/nav_header_main.xml - layout file for header on the navigation drawer
■	res/menu/activity_main_drawer.xml - layout file for the items in the navigation drawer
■	res/menu/main.xml - layout file for the About button in the app bar
●	AboutDiscoop.java
○	The AboutDiscoop class controls the application’s informational page. The page displays a TextView paragraph and has a back button that leads back to the previous class.
○	The button navigation to the AboutDiscoop class from the MainActivity class is handled by main.xml, from the Submit class is handled by submit.xml, and from the Search class is handed by search.xml.
○	The resource files used are: 
■	res/layout/activity_about_discoop.xml - layout file for the toolbar
■	res/layout/content_about_discoop.xml - layout file for the contents of the About page
●	MapsActivity.java
○	The MapsActivity class displays a map to the user and is used for viewing discounts stored in the database. 
○	The MapsActivity class creates a Google Maps fragment and implements OnMapReadyCallback to utilize Google Maps functions and objects. 
○	The onCreate method creates the map fragment, and gets the intent and any extras that were sent to this activity. The intent extras are stored in global variables.
○	The onMapReady method enables zoom controls and scroll gestures, and disables tilt and rotate gestures. 
■	If there were any intent extras, they were information about the discount the user is trying to locate (discount address, name, type, expiration date, and details). The address is passed to the getLatitude and getLongitude methods to convert the string address into doubles for latitude and longitude. This method then adds a blue marker on the location for the discount using the coordinates, with the name as a label, and the rest of the information as a snippet. If only the address is available from the extras, then the snippet has a default “Address” label. The camera is set to zoom in and center on the marker. The method also gets the user’s device location and adds a pink marker to mark that location. 
■	If there were no extras, this method takes every discount in the database (stored in a global list) and adds a blue marker for each discount on the map. The camera zooms and focuses on the last item in the database. The user’s device location is also marked with a pink marker. 
○	The getLocationFromAddress method uses the Geocoder to convert a string address into a LatLng point. The Geocoder gets a list of locations from the string and returns the first item in the list. 
○	The resource files used are:
■	res/drawable/ic_current_marker_icon - used for the current location marker.png
■	res/drawable/ic_marker_icon - used for the discount location marker.png
■	res/layout/activity_maps.xml - layout file used to display the Google Maps fragment
●	SearchActivity.java
○	The SearchActivity class allows users to search the database for a discount with information matching the search term. The user can click on an item in the search results list to show the discount on the map.
○	SearchActivity implements NavigationView.OnNavigationItemSelectedListener to be able to use the Navigation Drawer.
○	The onCreate method creates the toolbar which houses the search widget and about button, and also creates the Navigation Drawer.
○	The setUpViews method gets the user’s device current latitude and longitude. It also sets up the search widget in the toolbar and adds all the discounts in the database to a listview. The listview displays all the discounts by default and when a query is submitted from the search bar the list shortens to the discounts that match the query. The discounts are displayed by their address.
○	The distance method takes in a latitude and longitude for two different addresses. It uses the Location object’s distanceTo function to calculate the distance between the two addresses. 
○	The getLatFromAddress method takes in a string address and uses the Geocoder to get a list of addresses matching the input address. The method returns the latitude from the first address in the list.
○	The getLngFromAddress method takes in a string address and uses the Geocoder to get a list of addresses matching the input address. The method returns the longitude from the first address in the list.
○	The method onQueryTextSubmit controls the submission of a search term.
○	The onQueryTextChange controls any change in the search bar.
○	The onBackPressed method controls the closing of the Navigation Drawer.
○	The onCreateOptionsMenu creates the menu in the app bar.
○	The onOptionsItemSelected controls the selection of the About button in the app bar.
○	The aboutPage method changes activities from the SearchActivity class to the AboutDiscoop class. 
○	The onNavigationItemSelected controls the selection of the items listed in the Navigation Drawer.
○	The resource files used are:
■	res/layout/activity_search.xml - layout file to include the Navigation View
■	res/layout/app_bar_search.xml - layout file to create the app bar and toolbar
■	res/layout/content_search.xml - layout file for the contents of the search page including the listview to display search results
■	res/layout/nav_header_search.xml - layout file for the Navigation Drawer header
■	res/menu/activity_search_drawer.xml - menu file for the items in the Navigation Drawer
■	res/menu/search.xml - menu file for the About page option button
■	res/xml/searchable.xml - xml file to create a searchable activity
●	Submit.java
○	The Submit class controls the application’s Submission page to gather information about discounts from users. The page gets information about establishment type, location (street name and number and state), establishment name, discount details, and discount expiration date.
○	The submit class implements NavigationView.OnNavigationItemSelectedListener to be able to use the Navigation Drawer.
○	The onCreate method creates the Navigation Drawer and app bar. This method creates the button to submit information to the database, the checkboxes to indicate discount type, and the text input fields for users to enter discount information. This method also converts the information entered by the user to strings to be stored in the appropriate fields in the database. When the user clicks the submit button, the submit page is refreshed with blank fields.
○	The insertRows methods takes in an integer id, and strings type, address, expiration, details, and name. Using methods in the DBHandler, the information is added to the database.
○	The onBackPressed method controls the closing of the Navigation Drawer.
○	The onCreateOptionsMenu creates the menu in the app bar.
○	The onOptionsItemSelected controls the selection of the About button in the app bar.
○	The onNavigationItemSelected controls the selection of the items listed in the Navigation Drawer.
○	The toastSubmitMsg, toastSubmit, toastAdded, and toastRemoved methods are used to generate toast popups when various fields are selected. 
○	The returnBtn method is used for a nonexistent return button to return to the previous page. 
○	The onCheckboxClicked method is used to determine which checkbox is selected for the Type field.
○	The GetCoordinates method is used to get the user’s device coordinates. 
○	The aboutPage method changes activities from the submit class to the AboutDiscoop class. 
○	The resource files used are:
■	res/layout/activity_submit.xml - layout file to include the drawer layout
■	res/layout/app_bar_submit.xml - layout file to include the app bar and toolbar
■	res/layout/content_submit.xml - layout file for the content for submit.java
■	res/layout/nav_header_submit.xml - layout file for the Navigation Drawer header
■	res/menu/activity_submit_drawer.xml - menu file for the items in the Navigation Drawer
■	res/menu/submit.xml - menu file for the About page in the app bar
●	Discount.java
○	The Discount class is used for creating discount objects. It has constructors, setters, and getters for discount id, type, address, expiration, details, and name.
●	DBHandler.java
○	The DBHandler class is used to create discount objects and to manipulate the database used to store discounts. 
○	The class implements SQLiteOpenHelper to incorporate functions for data manipulation. 
○	The onCreate method creates the discounts table with KEY_ID as the primary integer key, and text columns for KEY_TYPE, KEY_ADDR, KEY_EXPR, KEY_DETAILS, and KEY_NAME.
○	The onUpgrade method drops the old database table and creates a new one if the database version is changed (the version is currently 5).
○	The addDiscount method takes in a discount and uses ContentValues to put the discount information into the appropriate column in the database.
○	The getDiscount method takes in the id of a discount and uses a cursor to search the database for the id matching the input id. It returns the discount that was found. 
○	The getAllDiscounts method uses a cursor and starts at the first entry in the database. The cursor goes to each discount and adds the discount in the list. Once the cursor has gone through all the discounts in the table, the method will return a list of all the discounts. 
○	The getDiscountsCount method uses a cursor to go through all the discounts in the database and returns an integer for the amount of discounts are stored.
○	The updateDiscount method takes in a discount and uses ContentValues to change the information for that discount. 
○	The deleteDiscount method takes in a discount and deletes that discount from the database. 
●	HttpDataHandler.java
○	The HttpDataHandler class establishes an HTTP data connection to a server in order to connect to the internet to utilize the Google Maps Geocoding API to convert addresses into coordinates.
