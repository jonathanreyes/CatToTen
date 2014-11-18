CatToTen
========

Don't be mean when you text bro

=======
Group Members
Jonathan Reyes, Pauline Masigla, Mehran Mizani

Application Concept
Cat To 10 is designed to curb cyber-bullying by warning users if their messages may be hurtful to the recipient before the messages are sent. The app has two main components: an SMS interceptor and a database interface.

The meat of Cat To 10 is the SMS interceptor, which will run in an asynchronous task even when the Cat To 10 app is closed. When the user sends an SMS text message, the interceptor will grab the message before it is actually sent and search the message for Offensive Phrases. These Offensive Phrases will be stored in a database on the user’s Android device, and each Offensive Phrase will have an associated Anger Score. The interceptor will calculate an overall Anger Score for the intercepted SMS message, and if that overall Score is beyond a certain threshold, a dialog box will be presented to the user. The dialog box will warn the user that his or her message is potentially hurtful and give the user the option to edit the message before sending. Alternatively, the user can elect to send his or her original message, but in order to do so, the user must first click through 10 cute cat pictures. These pictures are meant to calm the user and put him or her in a clearer state of mind before sending the SMS message. Eventually, the interceptor will release either the original SMS message or an edited version of it, and the released message will be sent to the proper recipient.

The main activity of Cat To 10 will be an interface through which a user can interact with the Offensive Phrases database. This will include options to import a set of Offensive Phrases and to export the current database to a file.

Required Resources
-A way to intercept SMS messages before they are sent
-An algorithm to calculate the overall Anger Score of an SMS message
-A web service to provide random cat pictures
-A database to store Offensive Phrases
-Functionality to import and export the database
-Functionality to create, read, update, and delete Offensive Phrases in the database

In the event that there is no obvious or implementable way to intercept SMS messages before they are sent, Cat To 10 will also include its own SMS messaging activity. In this case, the messaging activity will become the main activity, the interceptor will be an asynchronous task running behind the messaging activity, and the database interface will be a secondary activity within the app.

