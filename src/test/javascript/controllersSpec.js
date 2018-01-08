'use strict';

/* jasmine specs for controllers go here */

describe('Controller tests', function () {

   describe('TaskController', function () {

      var scope, ctrl;

      beforeEach(module('app'));
      beforeEach(inject(function ($rootScope, $controller) {
         scope = $rootScope.$new();
         ctrl = $controller('TaskController', {$scope: scope});
      }));

      it('should contain hello', function () {
         expect(scope.data).not.toBeDefined();
      });
   });
});
