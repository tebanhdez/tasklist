var app = angular.module('app', [
   'ngRoute',
   'controllers'
]);

app.config(['$routeProvider',
            function ($routeProvider) {
               $routeProvider.when('/', {
                  templateUrl: 'partials/home.html',
                  controller: 'TaskController'
               }).otherwise({
                   redirectTo: '/'
               });
            }]);