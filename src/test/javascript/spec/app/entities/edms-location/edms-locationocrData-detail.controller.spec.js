'use strict';

describe('Controller Tests', function() {

    describe('EdmsLocation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEdmsLocation, MockEdmsResponse, MockEdmsDownload;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEdmsLocation = jasmine.createSpy('MockEdmsLocation');
            MockEdmsResponse = jasmine.createSpy('MockEdmsResponse');
            MockEdmsDownload = jasmine.createSpy('MockEdmsDownload');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EdmsLocation': MockEdmsLocation,
                'EdmsResponse': MockEdmsResponse,
                'EdmsDownload': MockEdmsDownload
            };
            createController = function() {
                $injector.get('$controller')("EdmsLocationOcrDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ocrGatewayApp:edmsLocationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
