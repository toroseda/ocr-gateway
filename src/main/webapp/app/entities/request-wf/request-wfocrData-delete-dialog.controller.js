(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('RequestWfOcrDataDeleteController',RequestWfOcrDataDeleteController);

    RequestWfOcrDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'RequestWf'];

    function RequestWfOcrDataDeleteController($uibModalInstance, entity, RequestWf) {
        var vm = this;

        vm.requestWf = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RequestWf.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
