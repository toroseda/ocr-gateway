(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('ServiceWfOcrDataDeleteController',ServiceWfOcrDataDeleteController);

    ServiceWfOcrDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceWf'];

    function ServiceWfOcrDataDeleteController($uibModalInstance, entity, ServiceWf) {
        var vm = this;

        vm.serviceWf = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceWf.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
