(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('ServiceWfOcrDataDialogController', ServiceWfOcrDataDialogController);

    ServiceWfOcrDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceWf', 'ServiceResp'];

    function ServiceWfOcrDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceWf, ServiceResp) {
        var vm = this;

        vm.serviceWf = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviceresps = ServiceResp.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceWf.id !== null) {
                ServiceWf.update(vm.serviceWf, onSaveSuccess, onSaveError);
            } else {
                ServiceWf.save(vm.serviceWf, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ocrGatewayApp:serviceWfUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
