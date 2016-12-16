var app = angular.module("game");

app.factory("server", function($http){
    return {
        getMessagesByTime: function(gameCode, time, success, error){
            $http.get("/game/" + gameCode + "/messages/time/" + time).then(success, error);
        },
        getMessagesByIndex: function(gameCode, index, success, error){
            $http.get("/game/" + gameCode + "/messages/index/" + index).then(success, error);
        }
    };
});