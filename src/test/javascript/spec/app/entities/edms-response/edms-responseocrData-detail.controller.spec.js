'use strict';

describe('Controller Tests', function() {

    describe('EdmsResponse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEdmsResponse, MockEdmsRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEdmsResponse = jasmine.createSpy('MockEdmsResponse');
            MockEdmsRequest = jasmine.createSpy('MockEdmsRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EdmsResponse': MockEdmsResponse,
                'EdmsRequest': MockEdmsRequest
            };
            createController = function() {
                $injector.get('$controller')("EdmsResponseOcrDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ocrGatewayApp:edmsResponseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
