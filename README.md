# Game Messaging Server

## Configure
Make sure to update the application.conf and the routes file with the specific location that the external assets will be saved (the / at the end is required).  Current prod configuration is:
```
/home/mwingfield/gameFiles/
```

## Distribute
```
sbt dist

or

activator dist
```

## Install
```
scp target/universal/game-messaging-server-0.1.0-SNAPSHOT.zip personal02:~

ssh personal03

unzip game-messaging-server-0.1.0-SNAPSHOT.zip
```

## Run
```
sudo ./bin/game-messaging-server -Dhttp.port=80 > /dev/null 2>&1 &
```