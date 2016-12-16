angular.module("game").controller("SignInCtrl", function($scope, $rootScope, $http, $location, $interval, server, state){
  $scope.error = {
      show: false,
      msg: ""
  };

  $scope.submit = function(){
      console.log("Signing in: " + $scope.gameCode + " " + $scope.name);
      $scope.messageListener = new MessageListener($scope.gameCode, server, $scope);
      $scope.signInTimer = $interval($scope.messageListener.getMessages, 2000);

      $http.post("/game/" + $scope.gameCode + "/message", {
          "messageType": "PlayerInitMessage",
          "playerName": $scope.name
      }).then(function(success){}, function(err){
          $scope.error.msg = err;
          $scope.error.show = true;
          $interval.cancel($scope.signInTimer);
      });
  }

  $scope.handleMessage = function(msg){
      console.log("Got message: ", msg)
      if (msg.message.messageType === "PlayerRegistrationSuccessMessage" && msg.message.playerName == $scope.name){
          console.log("Player", $scope.name, "is registered");
          $interval.cancel($scope.signInTimer);
          $location.path("/preGameLobby");
          state.playerName = $scope.name;
          state.firstToEnterGame = msg.message.first;
      }
  };

  $scope.handleMessageError = function(err){
      console.log("Got message error: " + err);
  };
});