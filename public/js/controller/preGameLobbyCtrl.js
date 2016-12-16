angular.module("game").controller("PreGameLobbyCtrl", function($scope, $rootScope, $http, $location, server, state){
    $scope.firstPlayer = state.firstToEnterGame;
});