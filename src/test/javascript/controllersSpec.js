'use strict';

describe('Controller tests', function () {

   describe('TaskController', function () {

       var scope, ctrl, mockTaskService, q, deferred;
       var testTask = { 'Id': 'xxxx', 'Name': 'Test Task', 'DueDate': new Date(), 'Completed': false };

       beforeEach(module('app'));

       beforeEach(function () {
           mockTaskService = {
               getTasks: function () {
                   deferred = q.defer();
                   deferred.promise.then(function(data) {
                       return data;
                   });
                   deferred.resolve(testTask);
                   return deferred.promise;
               },
               deleteTask: function () {
                   deferred = q.defer();
                   deferred.resolve();
                   return deferred.promise;
               },
               saveTaskCall: function () {
                   deferred = q.defer();
                   deferred.resolve(testTask);
                   return deferred.promise;
               }
           };
           jasmine.createSpy(mockTaskService.prototype, 'getTasks').and.callThrough();
           jasmine.createSpy(mockTaskService.prototype, 'deleteTask').and.callThrough();
           jasmine.createSpy(mockTaskService.prototype, 'saveTaskCall').and.callThrough();
       });

      beforeEach(inject(function ($rootScope, $controller, $q) {
         scope = $rootScope.$new();
         q = $q;
         ctrl = $controller('TaskController', {$scope: scope, TaskService: mockTaskService});
      }));

      it('should initialize global vars', inject(function ($httpBackend){//function() {
          $httpBackend.expectGET("partials/home.html").respond({});
          scope.getAllPendingTaskCall();
          scope.$apply();
          expect(mockTaskService.prototype.getTasks).toHaveBeenCalled();
          expect(scope.editTask).toBeDefined();
      }));

      it('should mark task as complete', function () {
          spyOn(mockTaskService.prototype, 'saveTaskCall');
          scope.$digest();
          testTask.Completed = true;
          expect(mockTaskService.prototype.saveTaskCall).toHaveBeenCalled();
          expect(scope.editTask).toBeDefined();
      });
       it('should accept delete task', function () {

           spyOn(window, 'confirm').and.returnValue(true)
           scope.deleteTask(testTask.Id);
           scope.$digest();
           expect(mockTaskService.prototype.deleteTask).toHaveBeenCalled();
       });

       it('should cancel delete task', function () {
           spyOn(window, 'confirm').and.returnValue(false)
           scope.$apply();
           scope.deleteTask(testTask.Id);
           expect(mockTaskService.deleteTask).not.toHaveBeenCalled();
       });
       it('should open edit modal', function () {
           scope.open();
           expect(scope.showModal).toBe(true);
       });
       it('should close edit modal', function () {
           scope.cancel();
           expect(scope.showModal).toBe(false);
           expect(scope.editTask.Name).toBe('');
           expect(scope.editTask.DueDate.getDate()).toBe(new Date().getDate());
           expect(scope.editTask.DueDate.getFullYear()).toBe(new Date().getFullYear());
           expect(scope.editTask.DueDate.getMonth()).toBe(new Date().getMonth());
       });
       it('should accept edit changes but not valid data', function () {
           spyOn(mockTaskService.prototype, 'saveTaskCall');
           scope.editTask.Name = '';
           scope.ok();
           expect(mockTaskService.prototype.deleteTask).not.toHaveBeenCalled();
           expect(scope.showModal).toBe(false);
       });
   });
});
