# Game Messaging Server

## Distribute
```
sbt dist

or

activator dist
```

## Install
```
scp target/universal/game-messaging-server-0.1.0-SNAPSHOT.zip personal01:~

ssh personal01

unzip game-messaging-server-0.1.0-SNAPSHOT.zip
```

## Run
```
sudo ./bin/game-messaging-server -Dhttp.port=9090 > /dev/null 2>&1 &
```