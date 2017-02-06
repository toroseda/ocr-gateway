(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('ServiceRespOcrDataDetailController', ServiceRespOcrDataDetailController);

    ServiceRespOcrDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ServiceResp', 'EdmsDownload', 'ServiceWf'];

    function ServiceRespOcrDataDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ServiceResp, EdmsDownload, ServiceWf) {
        var vm = this;

        vm.serviceResp = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('ocrGatewayApp:serviceRespUpdate', function(event, result) {
            vm.serviceResp = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
