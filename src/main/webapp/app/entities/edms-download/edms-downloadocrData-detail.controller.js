(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsDownloadOcrDataDetailController', EdmsDownloadOcrDataDetailController);

    EdmsDownloadOcrDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EdmsDownload', 'EdmsLocation'];

    function EdmsDownloadOcrDataDetailController($scope, $rootScope, $stateParams, previousState, entity, EdmsDownload, EdmsLocation) {
        var vm = this;

        vm.edmsDownload = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ocrGatewayApp:edmsDownloadUpdate', function(event, result) {
            vm.edmsDownload = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
