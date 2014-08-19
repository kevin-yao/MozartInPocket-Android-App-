ReadMe

Instruction of project execution

In order to use the compose, music management and post function, you need to register first
Step1:  Registration
	-  You can type in user name and password in Login Page, restrictions are:
		-- user name and password can not be empty
		-- user name and password can not contains sub string as "\\" to avoid pseudo path
		-- user name and password can not exceed 25 characters
		-- after registration, your will be directed to the use profile page
		-- you can login at any time by using the registered user name and password
		
	-- You can complete and update your personal profile in the user profile page
		-- You have to edit that page at the first time you register
		-- You will be redirected to the use profile page when you click on the personal image
		-- The user profile page includes detailed information about personal photo, name, gender and interest
		-- In order to have your own photo, click on the photo and you can choose three modes
			-- choose a photo from existing pictures
			-- take a picture
			-- use screen shots
		-- All the information you edited will be uploaded to the server
		-- All your account information and post can be recovered after reinstallation
 
 Step2: Login in and then you can use the following functions
 
 Functions List:
 	-- Compose Music;
 	-- Post 
 	-- Share
 	-- Music File Management
 	-- Music in Cloud (view online sharing)
 	-- User Account Managment
 	
Step3: Logout
	You can logout at any time while using this app.
		
Functions Instructions
	
	Compose:
	-- MozartInPocket supports the following functions
	-- Virtual Piano Keyboard (VPK)
		-- You can see a piano keyboard that has exactly the same structure of the real piaon
			-- The piano keyboard has range from C3 to C7, 
			-- You will hear the corresponding sound when pressing the key
			-- All falling and lifting are transformed as falling
		-- Music Score according to key press
			-- After pressing the virtual piano key, you will see the corresponding music score above the piano keyboard
			-- press key for pitch
			-- The default beat is eighth
			-- press beat button if you want to change beats, six different beats can be selected below the VPK (whole, half, quarter, eighth, 16th, 32th)
			-- "Section" is to end current score section
			-- "Line" is to end this line and start a new line
			-- "Score" is end this score
			
			--"Delete" is to delete the last note and beats
			--"space" is to separate music note
			
			-- Extension functions added
				--"Chord" enable you to compose chord notes
				--"Glssando" is for decoration notes
				
				
		-- Play Music Score
			-- The music score you can view currently will be compiled into a whole music file and played back
			-- You can play the music score at any time during composition
			-- The music score will not be saved by playing music score
		-- Save
			-- By pressing save, you are asked to input the music file name and descriptions of this music
			-- The music name can not be empty
			-- The music name support repeatition
			-- Your music score and compiled music midi file will be saved in the default path using your input name
			-- You will be able to open, edit and play your music in the music list
			
	Post
	 	-- You will browse your music list first
	 	-- click on the music name on the left column of the music list
	 	-- You can view the detail information of the music you selected
	 	-- You can type in your comment in the edit text area
	 	-- press "Post", you music midi file and comment will be posted online
	 	-- You can continuously click on different music names and do need to press cancel
	 	
	Share
		-- You will browse your music list first
	 	-- click on the music name on the left column of the music list
	 	-- You can view the detail information of the music you selected
	 	-- Press "Share", you can choose all other services that you use to share the music file with another device 
	 
	 Music List
	 	-- All your composed music files will show in the left column of the music list
	 	-- Click on the music name on the left column, you will see the detail information of the music item in the right column
	 	-- Press the "edit" icon likes a pencil, you can open the whole music file in the compose page
	 		-- You will be able to do everything you are allowed in the compose function with the reopene dfile
	 	-- Press the "delete" button with a icon of crossing you will delete this music file
	 		-- The file will be completed deleted from the database and left column will be updated instantly
	 	-- Press "play" button you will hear the music file
	 		-- You can stop , continue and restart from any time
	 		-- The progress bar will show the current position of the whole music file
	 		-- press back or home navigation button will pause the music
	 	-- MusicList support multitouch operation
	 		-- two finger slipping down --> switch to the next music item
	 									--> go back to the first item if reached the last one
	 		-- three finger slipping down --> shortcut to post the current music item
	 		
	 Music In Cloud
	 	--You will be able to see all the posts of users that use this application and posted their musics
	 	-- Pull down the scroll and release to refresh the post
	 	-- Every post will have the posted user photo, user name
	 	-- post contains publishing time, GPS location and music files
	 	-- you can play, pause the music by simply clicking on the post
	 
	 	
Data Recovery
	-- All user information will be uploaded to the server side, so you can retrieve your information after re-installation or change a device
	-- All your music files are stored in the local SQLite Database, you can retrieve your music files after re-installation or logging out
		But you can not get your musics file if you switch a device
	-- All your posted musics are stored in the server side, so you can retrieve it after re-installation or switching to other devices	

Security
	-- You have to 
Included Libraries
	-- Android-midi-lib 
		android platform does not support midi platform, this lib can provide some midi interfaces
	-- EditorAbcJs 
		javascript library enabling generating music notes using special grammar
		
	
	
 	 