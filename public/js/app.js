var app = angular.module("game", ["ngRoute"]);

app.config(function($routeProvider, $qProvider){
    $routeProvider.when("/", {
        templateUrl: "html/signIn.html"
    }).when("/preGameLobby", {
        templateUrl: "html/preGameLobby.html"
    });
    $qProvider.errorOnUnhandledRejections(false);
});

app.run(function($rootScope, $http){
    $rootScope.thing = "Hello World"
});

