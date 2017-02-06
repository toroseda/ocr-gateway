(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsLocationOcrDataDeleteController',EdmsLocationOcrDataDeleteController);

    EdmsLocationOcrDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'EdmsLocation'];

    function EdmsLocationOcrDataDeleteController($uibModalInstance, entity, EdmsLocation) {
        var vm = this;

        vm.edmsLocation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EdmsLocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
