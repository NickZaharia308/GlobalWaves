# Proiect GlobalWaves - Etapa 1

## Copyright 2023 Zaharia Nicusor-Alexandru 325CA

### Project Idea
The project simulates an audio app, such as Spotify.\
Users can enter different commands and they will get a following output.\
The main entities are: 
* Users
* Songs
* Playlists
* Podcasts

The code uses JSON to simulate the input and the output and the informations stored about: users, songs and podcasts (which contain episodes).

### Structure

In the **src/** folder, there are more .java files:
* **AddRemoveInPlaylist.java** - Adds/removes a song in a playlist
* **Backward.java** - Goes back 90 seconds in a podcast episode
* **Command.java** - The general structure for a command, each command has these parameters
* **CreatePlaylist.java** - Allows users to create public playlists
* **Episodes.java** - Represents an episode of a podcast
* **FollowingPlaylist.java** - Allows users to follow others' users' public playlists
* **Forward.java** - Skips 90 seconds in a podcast episode
* **GetTop5Songs.java** - Lists the top 5 most appreciated songs by users
* **GetTop5Playlists.java** - Lists the top 5 most followed playlists by users
* **Library.java** - The main component that stores the users, songs, playlists, and podcasts
* **Like.java** - Allows users to like a song
* **Load.java** - Loads a song, playlist, or podcast on the music player
* **Main.java** - The entry point in the program, transfers all the input to the library
* **MusicPlayer.java** - Stores information about the current track, left time, and other information
* **Next.java** - Skips to the next song or episode
* **Playlists.java** - Groups a list of songs under a single name
* **PlayPause.java** - Switches the music player from play to pause and vice versa
* **Podcasts.java** - Groups a list of episodes with the same theme
* **Prev.java** - Goes to the start of the song or episode
* **PrintOutput.java** - Reads every command and prints JSON nodes to the output
* **Repeat.java** - Allows the user to repeat songs, playlists, and podcasts once or unlimited times
* **Search.java** - Gives the user a list with results based on the search's command parameters
* **Select.java** - Lets the user choose one of the search's results
* **ShowPlaylists.java** - Shows the owner's playlists
* **ShowPreferredSongs.java** - Gives a list with the appreciated songs
* **Shuffle.java** - Shuffles the songs in a playlist
* **Songs.java** - Has information about a library song
* **Status.java** - Shows some details about the current track
* **SwitchVisibility.java** - Switches a playlist's visibility from "private" to "public" and vice versa
* **Test.java** - Used to test specific input
* **Users.java** - Contains details about every user

### Description
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
The center unit that stores the lists of the main entities is the library, found in "Library.java," which contains the list of all users, songs, podcasts, and playlists.\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Every user has their own Music Player, which can be found in "MusicPlayer.java." The music player runs one track at a time (library song, playlist song, or podcast episode). It also stores informations about the track such as:
* paused/not paused
* shuffled/ not shuffled
* repeat track type (never, once or infinite)
* time left

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
A user can give a certain command at a known timestamp. Timestamp is used to have a chronological order of the commands. The output is basically instant (same timestamp as input) and the reason is that we don't want a user to wait much after searching a song or after following a playlist.

\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Grouping the classes we have:
* <font color="yellow">Main entities:</font>
  * Users, Songs, Playlists, Podcasts, Episodes, MusicPlayer\
  \
Those are used to store general informations about entities and have some helper methods to simplify the search in an array list of users for example.

* <font color="Pink">Passive Commands:</font>
  * Command, Search, Select, Load, CreatePlaylist, AddRemoveInPlaylist, FollowingPlaylist, ShowPlaylists,
  ShowPreferredSongs, SwitchVisibility and Like.\
  \
Those are all commands, which don't necessarily change the state of the music player (static commands). With the exception of Search, Select and Load which actively change the music player without modifying its parameters.

* <font color="violet">Active Commands:</font>
  * Backward, Forward, PlayPause, Prev, Next, Repeat, Shuffle\
  \
The commands above are active because they modify, how or what track will be played next. A command that I will explain is **Repeat**.\
  Repeat has 3 states:
    * for playlists: 
	  * **no repeat**, the player is on pause after a track ends
	  * **repeat all**, starts the playlists again after the last song finishes
	  * **repeat current song**, plays the current song infinite times
	* for a library song or podcast:
	  * **no repeat**, the player is on pause after a track ends
	  * **repeat once**, it runs the current track one more time
	  * **repeat infinite**, rust the current track infinite times
* <font color="green">Statistics:</font>
  * GetTop5Songs, GetTop5Playlists, Status\
  \
The listed commands, show informations about most liked songs, or most followed playlists or the current status of a track (paused or not, shuffled or not, repeat mode, remainde time and name of the track).

* <font color="lighblue">IOs:</font>
  * Main, PrintOutput, Library, Test\
  \
The classes responsible for managing the input and the output of the program. Library for example follows the **Singleton Pattern** because we only want one instance of the library that stores all of the arrays. The communication between input->commands and results->output is done using JSON, because is a lightweight data-interchange format commonly used for data exchange between a server and a web application, as well as for configuration files.
