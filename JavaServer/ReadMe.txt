This program is a basic chat communication system built from scratch with Java sockets and no frameworks.

To get the program running on your computer you will need to set up a database.
If you have MySQL installed, create a new database and open the MySQLConnection class
to change the User, Password, ServerName, and DatabaseName variables to match your own.
Also, for the Java sockets to work, you may need to disable your computer's firewalls on 
the current network. After you have the database and firewalls configured, run the ServerControl
file, and it will automatically create the required tables in the database and start the server program. 
Once the server program is running, run the ClientControl file and a UI will appear on your screen.
It is possible to run the ClientControl multiple times to create as many clients as desired. 