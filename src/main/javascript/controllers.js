var controllers = angular.module("controllers", []);

controllers.controller("TaskController", ['$scope', '$http', 'TaskDataOp',function ($scope, $http, TaskDataOp) {

    $scope.title = 'Pending tasks:';
    $scope.editTask = {'Name': '', 'DueDate': new Date()};

    $scope.getAllPendingTaskCall = function(){
        TaskDataOp.getTasks().then(function (response) {
            $scope.data = response.data;
            console.log(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    $scope.markAsCompleted = function(task, e){
        var response = confirm("Do you want to mark the task as complete?");
        console.log("responded");
        if (response==true) {
            console.log("Completing");
            TaskDataOp.saveTaskCall(task).then(function (response) {
                $scope.getAllPendingTaskCall();
                $scope.editTask = {'Name': '', 'DueDate': new Date()};
            }, function (response) {
                console.error(response);
            });
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

    var deleteTaskCall = function(taskId){
        TaskDataOp.deleteTask(taskId).then(function (response) {
            $scope.getAllPendingTaskCall();
            $scope.editTask = {'Name': '', 'DueDate': new Date()};
        }, function (response) {
            console.error(response);
        });
    };

/**/
    $scope.open = function(task) {
        if(task)
            angular.copy(task, $scope.editTask);
        $scope.showModal = true;
    };

    $scope.ok = function() {
        if($scope.editTask.Name && $scope.editTask.DueDate)
            TaskDataOp.saveTaskCall($scope.editTask).then(function (response) {
                $scope.getAllPendingTaskCall();
                $scope.editTask = {'Name': '', 'DueDate': new Date()};
            }, function (response) {
                console.error(response);
            });
        $scope.showModal = false;
    };

    $scope.cancel = function() {
        $scope.editTask = {'Name': '', 'DueDate': new Date()};
        $scope.showModal = false;
    };
/**/
}]);