'use strict';

describe('Controller Tests', function() {

    describe('ServiceResp Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockServiceResp, MockEdmsDownload, MockServiceWf;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockServiceResp = jasmine.createSpy('MockServiceResp');
            MockEdmsDownload = jasmine.createSpy('MockEdmsDownload');
            MockServiceWf = jasmine.createSpy('MockServiceWf');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ServiceResp': MockServiceResp,
                'EdmsDownload': MockEdmsDownload,
                'ServiceWf': MockServiceWf
            };
            createController = function() {
                $injector.get('$controller')("ServiceRespOcrDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ocrGatewayApp:serviceRespUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
