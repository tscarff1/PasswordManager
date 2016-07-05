# PasswordManager
Password manager program written using Swing and BCrypt. It encrypts the account information using AES encryption and the user-chosen password to create the key. 
Admin password is hashed using BCrypt's hashing functionality. I created this program for myself to keep track of my passwords,
so I have made it secure enough for me to feel comfortable using it.

##Using the program
![Title Screen](https://github.com/tscarff1/PasswordManager/blob/master/PasswordManager/screen2.png)

On loading the program for the first time, it will ask for an administrator. The password has a maximum length of 20, but has no other restrictions.

![Title Screen](https://github.com/tscarff1/PasswordManager/blob/master/PasswordManager/screen1.png)

On subsequent uses of the program, the program will instead ask you to confirm the admin password.

![Title Screen](https://github.com/tscarff1/PasswordManager/blob/master/PasswordManager/screen7.png)
![Title Screen](https://github.com/tscarff1/PasswordManager/blob/master/PasswordManager/screen3.png)

Using File > Add account will allow the user to add a new account as shown.

![Title Screen](https://github.com/tscarff1/PasswordManager/blob/master/PasswordManager/screen4.png)
![Title Screen](https://github.com/tscarff1/PasswordManager/blob/master/PasswordManager/screen6.png)

The user can edit any of the account information, including the password, by double clicking in the appropriate cell.

![Title Screen](https://github.com/tscarff1/PasswordManager/blob/master/PasswordManager/screen5.png)

The user can copy an account's password without having to by using edit > copy password after selecting a row by single clicking.
##To-Do
- turn viewing/editing on and off
- Add the ability to change passwords
- Add the ability to store passwords online
-- Will probably build an android app too
