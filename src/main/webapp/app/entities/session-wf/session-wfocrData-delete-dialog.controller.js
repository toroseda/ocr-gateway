(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('SessionWfOcrDataDeleteController',SessionWfOcrDataDeleteController);

    SessionWfOcrDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'SessionWf'];

    function SessionWfOcrDataDeleteController($uibModalInstance, entity, SessionWf) {
        var vm = this;

        vm.sessionWf = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SessionWf.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
