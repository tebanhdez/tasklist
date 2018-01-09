var TaskService = angular.module('TaskService', [])
TaskService.factory('TaskDataOp', ['$http', function ($http) {

    var TaskDataOp = {};

    TaskDataOp.getTasks = function () {
        return $http.get("api/task");
    };

    TaskDataOp.deleteTask = function (taskId) {
        return $http['delete']('api/task/'+taskId);
    };
    TaskDataOp.saveTaskCall = function(task){
        var date = task.DueDate;
        if(date.getDate)
            task.DueDate = date.getFullYear() +"-"+ (date.getMonth()+1) +"-"+ date.getDate();
        return $http.put("api/task", task);
    };
    return TaskDataOp;
}]);