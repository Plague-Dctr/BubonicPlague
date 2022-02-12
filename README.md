# "Black Death" RAT / Logger

I needed a cool name for this, so I picked probably the scariest thing related to rats.

# What does it do?

- Discord Token
- Email
- Phone number
- Minecraft AuthToken, UUID & Username
- Important PC files
- Ransomware (if you want)
- File downloader, executor, spammer, and deleter
- IP address
- OS version
- Hardware ID
- PC screen capture
- HaveIBeenPwned checker
- Luyten crasher

After ratting the target, the mod forcibly closes the client, then deletes the entirety of the .minecraft folder (including itself), as well as the launcher. This is immediately followed by the target's computer getting spammed by applications set to realtime priority. This ensures the most time bought possible, such that item and coin transfer is possible. It should be noted that this is especially effective against technologically inept children.

# Session Stealing

Go to the **.minecraft** directory and find the **version** folder. Open the **1.8.9 OptiFine** directory, and edit the .json file. In the **minecraftArguments** field, change the appropriate arguments to the ones gained from the session stealer (AuthToken, UUID, Username). Starting Minecraft should then log you into their account.

# Transferring items

Basically just pretend to be really good at auction sniping.

# Compiling

Forge MDK is super finnicky, so this section was necessary.
First download and unzip the Forge MDK for 1.8.9.
Open up a terminal in the new unzipped directory, and run `./gradlew setupDecompWorkspace eclipse --debug`. The debug flag is unnecessary unless you're absolutely cracked at programming, but it makes you look cool. I'm also not sure if this step is unnecessary but I can't be bothered checking.
Open up the build.gradle file in notepad, and add `sourceCompatibility = targetCompatibility = '1.8'` somewhere. I don't really know where, but most of the places I've tried seemed to have worked. Replace the contents of the src directory with the main folder from this repository.
Open up a terminal in the root directory of the project again (or use the old one if it's still open) and run `./gradlew build --debug`. As I've previously established, the debug flag is absolutely necessary.
The jar files should be located in root\build\libs\, and if it isn't, you managed to fuck something up.
Use the file without the word "source" in it. 


# Disclaimer

This was made completely for educational purposes.
Please do not use it for anything else.
Like stealing other people's stuff.
Anything but that.
Also, I am not responsible for any damage done to your computer or anyone else's.
