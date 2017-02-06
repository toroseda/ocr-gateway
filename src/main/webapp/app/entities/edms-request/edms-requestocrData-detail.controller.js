(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsRequestOcrDataDetailController', EdmsRequestOcrDataDetailController);

    EdmsRequestOcrDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EdmsRequest', 'OcrSession', 'RequestWf'];

    function EdmsRequestOcrDataDetailController($scope, $rootScope, $stateParams, previousState, entity, EdmsRequest, OcrSession, RequestWf) {
        var vm = this;

        vm.edmsRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ocrGatewayApp:edmsRequestUpdate', function(event, result) {
            vm.edmsRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
