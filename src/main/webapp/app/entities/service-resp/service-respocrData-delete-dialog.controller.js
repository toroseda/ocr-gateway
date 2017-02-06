(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('ServiceRespOcrDataDeleteController',ServiceRespOcrDataDeleteController);

    ServiceRespOcrDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceResp'];

    function ServiceRespOcrDataDeleteController($uibModalInstance, entity, ServiceResp) {
        var vm = this;

        vm.serviceResp = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceResp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
