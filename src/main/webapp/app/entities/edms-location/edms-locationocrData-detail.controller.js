(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsLocationOcrDataDetailController', EdmsLocationOcrDataDetailController);

    EdmsLocationOcrDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EdmsLocation', 'EdmsResponse', 'EdmsDownload'];

    function EdmsLocationOcrDataDetailController($scope, $rootScope, $stateParams, previousState, entity, EdmsLocation, EdmsResponse, EdmsDownload) {
        var vm = this;

        vm.edmsLocation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ocrGatewayApp:edmsLocationUpdate', function(event, result) {
            vm.edmsLocation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
