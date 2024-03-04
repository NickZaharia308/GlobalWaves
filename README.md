# GlobalWaves Project

## Copyright 2024 Zaharia Nicusor-Alexandru 325CA

### Project Idea
The project simulates an audio app, such as Spotify.\
Users can enter different commands and they will get a following output.\
The main grouped entities are: 
* Users - Normal Users, Artists and Hosts
* Songs
* Playlists
* Podcasts
* Albums

The code uses JSON to simulate the input and the output and the informations stored about: users, tracks and statistics.

### Structure

The file in the **src/** hierarchy looks like this:
* **commands/**

   * **admin/**
     * **AddUser** - Authorizes the admin to add a new user (normal, artist or host)
     * **DeleteUser** - Allows the admin to remove a user, under some circumstances mentioned in the code
     * **EndProgram** - Implicit command, that runs at the end of the program, which computes various things for artists
     * **ShowAlbums** - Displays all albums of a given artist
     * **ShowPodcasts** - Shows all podcasts of a given host
     
   * **page/**
     * **command/**
       * **PageCommand** - Helps for page navigation (previous page or following page)
       * **NextPage** - Allows users to go to a page they have been previously on
       * **PreviousPage** - Navigates to the previous page of a user, if that exists
     * **observer/**
       * **PageSubject** - Subject Class in Observer design pattern that has as subjects artists and hosts
       * **PageObserver** - Interface implemented by normal users that are on an artist or host page
     * **ChangePage** - Changes the current selected page of a user 
     * **PrintCurrentPage** - Displays the page that the user previously selected
     

   * **searchBar/**
     * **Search** - Gives the user a list with results based on the search's command parameters
     * **Select** - Lets the user choose one of the search's results
     * **Load** - Loads a song, playlist, or podcast on the music player

   * **statistics/**
     * **GetAllUsers** - Returns a list with all users
     * **GetOnlineUsers** - Gets a list with all normal users that are online
     * **GetTop5Albums** - Shows top 5 albums, sorted by the sum of the number of likes of each song
     * **GetTop5Artists** - Presents top 5 artists, sorted by the number of total likes of all albums
     * **GetTop5Songs** - Lists the top 5 most appreciated songs by users
     * **GetTop5Playlists** - Lists the top 5 most followed playlists by users
     * **Wrapped** - Shows various statistics for all kind of users

   * **users/**

     * **artist/**
       * **AddAlbum** - Allows the artists to add a new album with songs
       * **AddEvent** - Lets the artists to add a new event
       * **AddMerch** - Permits the artists to add new merchandise
       * **RemoveAlbum** - Enables the artists to remove an album under some conditions, better explained in the code
       * **RemoveEvent** - Allows the artists to delete an event

     * **host/**
       * **AddPodcast** - Authorizes the hosts to introduce a new podcast
       * **AddAnnouncement** - Permits the hosts to add a new announcement
       * **RemovePodcast** - Grants the hosts the ability to remove podcasts if some conditions are fulfilled
       * **RemoveAnnouncement** - Allows the hosts to delete an announcement

     * **notifications/**
       * **GetNotifications** - Gets a list with all the notifications of a user
       * **NotificationObserver** - Normal Users that subscribe to a host or an artist
       * **NotificationSubject** - Artists or Hosts that notify users when they add something
       * **Subscribe** - Lets users subscribe to / unsubscribe from an artist or a host
     
     * **playlists/**
       * **CreatePlaylist** - Allows users to create public playlists
       * **FollowingPlaylist** - Allows users to follow others' users' public playlists
       * **ShowPlaylists** - Shows the owner's playlists
       * **SwitchVisibility** - Switches a playlist's visibility from "private" to "public" and vice versa

     * **AdBreak** - Inserts an add in the front of the queue of a non-premium user
     * **AddRemoveInPlaylist** - Adds/removes a song in a playlist
     * **Backward** - Goes back 90 seconds in a podcast episode
     * **BuyMerch** - Permits the user to buy merch from an artist
     * **BuyPremium** - Allows the user to buy premium (no more ads)
     * **CancelPremium** - Cancels the user's premium subscription and computes the song revenue for each listened artist
     * **Forward** - Skips 90 seconds in a podcast episode
     * **Like** - Allows users to like a song
     * **LoadRecommendations** - Loads the last recommendation for a user (song or playlist)
     * **Next** - Skips to the next song or episode
     * **PlayPause** - Switches the music player from play to pause and vice versa
     * **Prev** - Goes to the start of the song or episode
     * **Repeat** - Allows the user to repeat songs, playlists, albums and podcasts once or unlimited times
     * **SeeMerch** - Shows the user bought merchandise (from artists) 
     * **ShowPreferredSongs** - Gives a list with the appreciated songs
     * **Shuffle** - Shuffles the songs in a playlist or an album
     * **Status** - Shows some details about the current track
     * **SwitchConnectionStatus** - Switches the online status of a normal user.
     * **UpdateRecommendations** - Creates a playlist or gives a song based on user's listened artists, songs, genres

   * **Command** - The general structure for a command, each command has these parameters

* **fileio/input/**
   * **EpisodeInput** - Input for the array with all episodes
   * **LibraryInput** - Input for the Library of the program
   * **PodcastInput** - Input for the array with all podcasts
   * **SongInput** - Input for the array with all of the songs
   * **UserInput** - Input for the list of initial users

* **main/**
   * **Library** - The main component that stores the users, songs, playlists, albums, podcasts and an observers map
   * **PrintOutput** - Reads every command and prints JSON nodes to the output
   * **Test** - Used to test specific input
   * **Main** - The entry point in the program, transfers all the input to the library

* **user/entities/**

   * **audio/**

     * **files/**
       * **Album** - Artist made collection of personal songs
       * **Episodes** - Represents an episode of a podcast
       * **Playlists** - Groups a list of songs under a single name
       * **Podcasts** - Groups a list of episodes with the same theme
       * **Songs** - Has information about a library song

     * **MusicPlayer** - Stores information about the current track, left time, and other information

   * **specialEntitites/**
     * **Announcement** - Host created entity that represents an announcement and appears on its page
     * **Event** - Artist made entity that appears on its page
     * **Merch** - Artist made entity that represents merchandise and appears on its page
   * **Artist** - Special type of user that can create albums 
   * **Host** - Special type of user that can create podcasts
   * **Users** - Contains details about every user

### Description
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
The center unit that stores the lists of the main entities is the library, found in "Library.java," which contains the list of all users, songs, podcasts, albums, playlists and an observer map.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Every user has their own Music Player, which can be found in "MusicPlayer.java." The music player runs one track at a time (library song, playlist song, or podcast episode). It also stores informations about the track such as:
* paused/not paused
* shuffled/ not shuffled
* repeat track type (never, once or infinite)
* time left

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
A user can give a certain command at a known timestamp. Timestamp is used to have a chronological order of the commands. The output is basically instant (same timestamp as input) and the reason is that we don't want a user to wait much after searching a song or after following a playlist.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
The communication between input->commands and results->output is done using JSON, because is a lightweight data-interchange format commonly used for data exchange between a server and a web application, as well as for configuration files.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
##### Grouping the classes we have:
* <font color="yellow">commands/</font>

  * All of the commands are placed here. The commands are categorized as the hierarchy shows so that we have: **admin** commands, **pages** related commands, **searchBar** commands that let the user search, select and load something, general **statistics** and finally **user's** commands.

* <font color="violet">fileio/input/:</font>
  * Files that are used to read the data in JSON format and make it Java usable.

* <font color="green">main/:</font>
  * Classes in this package don't really fit in a specific category, because each one of them is important and unique:
    * Main: Access point in the program, that fills the library and runs the commands.
    * Test: Class strictly used for input file testing.
    * PrintOutput: Class designed to create the JSON nodes and write them in output files.
    * Library: Central Storing Entity *(A.K.A. CSE)* that follows the Singleton pattern and keeps the essential program lists.

* <font color="lighblue">user/entities/</font>
  * Package that contains both passive entities **(specialEntities/ and audio/)** and active entities such as: users, hosts and artists which use commands.

### OOP Concepts

 * #### Design Patters
   * Library follows the **Singleton Design Pattern** because we only want one instance of the library that stores all of the arrays.
   * The page system uses the **Observer Design Pattern** so that if a normal user has an artist/host page selected, it will be notifed if something on that page changes. In this case the relation in one-to-many, where **Subjects** are hosts and artists and **Observers** are normal users.
   * The page system also uses the **Command Design Pattern**

 * #### Inheritance
   * All commands extend the command class, artists and hosts extend the users class and the user implements the Observer interface.

 * #### Encapsulation
   * Classes have their fields as private (or protected in some cases), because it helps in maintaining a consistent state, facilitating future changes to the internal representation of the class without affecting external code. This translates to:
   * **Private Fields**: Declaring class fields as private to restrict direct access from outside the class.
   * **Public Getters and Setters**: Providing public methods (getters and setters) to access and modify the private fields. This allows controlled and well-defined access to the internal state of the object.

 * #### Miscellaneous
   * **Comparator**: PageMenu, GetTop5Artists, GetTop5Albums, GetTop5Songs, GetTop5Playlists, EndProgram, Wrapped
   * **Iterator**: DeleteUser, RemoveAlbum
   * **Stream**: DeleteUser, Songs, BuyMerch
   * **Lambda Expressions**: AddAlbum, DeleteUser, Subject, EndProgram, BuyMerch

