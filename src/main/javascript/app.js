var app = angular.module('app', [
   'ngRoute',
   'controllers','ui.bootstrap.modal','TaskService'
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