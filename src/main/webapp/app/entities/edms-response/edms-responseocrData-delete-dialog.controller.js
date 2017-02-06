(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsResponseOcrDataDeleteController',EdmsResponseOcrDataDeleteController);

    EdmsResponseOcrDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'EdmsResponse'];

    function EdmsResponseOcrDataDeleteController($uibModalInstance, entity, EdmsResponse) {
        var vm = this;

        vm.edmsResponse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EdmsResponse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
