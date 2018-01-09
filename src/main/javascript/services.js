var TaskService = angular.module('TaskService', [])
TaskService.factory('TaskService', ['$http', function ($http) {

    var TaskService = {};

    TaskService.getTasks = function () {
        return $http.get("api/task");
    };

    TaskService.deleteTask = function (taskId) {
        return $http['delete']('api/task/'+taskId);
    };
    TaskService.saveTaskCall = function(task){
        var date = task.DueDate;
        if(date.getDate)
            task.DueDate = date.getFullYear() +"-"+ (date.getMonth()+1) +"-"+ date.getDate();
        return $http.put("api/task", task);
    };
    return TaskService;
}]);