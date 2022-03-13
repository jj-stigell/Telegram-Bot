# Telegram Event organizing and registering bot

Simple bot for creating events where user can sign up. Limits can be se how many people are allowed to participate.
Small project part of the programming studio course @Aalto University Finland.
The exercise base uses the **Bot4s** -library by Alfonso Peterssen.


## TODO:

- Add error handling for invalid input
- Save event information for JSON in case of bot crashes, now information stored to buffer. Info lost on reboot/crash
- Better unique identification of users, instead of name (obvious problem with duplicates).
- Tests


## Run

- get bot token by creating a new telegram bot using the BotFather in Telegram
- add bot token to the root directory in bot_token.txt file
- start from sbt shell
- command: run
- Find bot from the telegram and start chatting, you can add it to existing groups

## Bot token

You need to create a file called **bot_token.txt** which contains your bot_token and place it in the root directory for the code to work. The token is given to you by the BotFather -bot in Telegram.

The **bot_token.txt** -file should never be pushed into the repository for safety reasons. Our .gitignore -file should prevent this from happening.
