'use strict';

/* jasmine specs for controllers go here */

describe('Controller tests', function () {

   describe('TaskController', function () {

      var scope, ctrl, httpMock;

      beforeEach(module('app'));
      beforeEach(inject(function ($rootScope, $controller) {
         scope = $rootScope.$new();
         ctrl = $controller('TaskController', {$scope: scope});
      }));

      it('should initialize global vars', function () {
         expect(ctrl).toBeDefined();
         spyOn(scope, 'getAllPendingTaskCall');
         expect(scope).toBeDefined();
         expect(scope.editTask).toBeDefined();
         expect(scope.getAllPendingTaskCall).toHaveBeenCalled();

      });

   });
});
