(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsResponseOcrDataDetailController', EdmsResponseOcrDataDetailController);

    EdmsResponseOcrDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EdmsResponse', 'EdmsRequest'];

    function EdmsResponseOcrDataDetailController($scope, $rootScope, $stateParams, previousState, entity, EdmsResponse, EdmsRequest) {
        var vm = this;

        vm.edmsResponse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ocrGatewayApp:edmsResponseUpdate', function(event, result) {
            vm.edmsResponse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
