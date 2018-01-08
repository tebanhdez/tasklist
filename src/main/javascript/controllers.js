var controllers = angular.module("controllers", []);

controllers.controller("TaskController", ['$scope', '$http', function ($scope, $http) {
    $scope.debug = false;
    $scope.title = 'Pending tasks:';

    $http.get("api/task").then(function (response) {
        $scope.data = response.data;
        console.log(response.data);
    }, function (response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        console.error(response);
    });

    $scope.markAsCompleted = function(task, e){
        var response = confirm("Do you want to mark the task as complete?");
        console.log("responded");
        if (response==true) {
            console.log("Completing")
            completeTaskCall(task);
        }else{
            e.preventDefault();
        }

    };
    $scope.deleteTask = function(taskId){
        var response = confirm("Do you want to delete the task?");
        console.log("responded");
        if (response==true) {
            console.log("deleting")
            deleteTaskCall(taskId);
        }

    };
    function completeTaskCall(task){
        $http.put("api/task", task).then(function (response) {
            console.log(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    var deleteTaskCall = function(taskId){
        $http['delete']('api/task/'+taskId).then(function (response) {
            console.log(response);
        }, function (response) {
            console.error(response);
        });
    };
    $scope.toggleDebug = function () {
        $scope.debug = !$scope.debug;
    };

}]);