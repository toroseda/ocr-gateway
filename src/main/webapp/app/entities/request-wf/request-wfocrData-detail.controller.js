(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('RequestWfOcrDataDetailController', RequestWfOcrDataDetailController);

    RequestWfOcrDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RequestWf', 'EdmsRequest'];

    function RequestWfOcrDataDetailController($scope, $rootScope, $stateParams, previousState, entity, RequestWf, EdmsRequest) {
        var vm = this;

        vm.requestWf = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ocrGatewayApp:requestWfUpdate', function(event, result) {
            vm.requestWf = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
