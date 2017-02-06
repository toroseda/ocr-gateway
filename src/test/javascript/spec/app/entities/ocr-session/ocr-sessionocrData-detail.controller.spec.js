'use strict';

describe('Controller Tests', function() {

    describe('OcrSession Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOcrSession, MockSessionWf, MockEdmsRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOcrSession = jasmine.createSpy('MockOcrSession');
            MockSessionWf = jasmine.createSpy('MockSessionWf');
            MockEdmsRequest = jasmine.createSpy('MockEdmsRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OcrSession': MockOcrSession,
                'SessionWf': MockSessionWf,
                'EdmsRequest': MockEdmsRequest
            };
            createController = function() {
                $injector.get('$controller')("OcrSessionOcrDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ocrGatewayApp:ocrSessionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
