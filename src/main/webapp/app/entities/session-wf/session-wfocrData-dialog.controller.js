(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('SessionWfOcrDataDialogController', SessionWfOcrDataDialogController);

    SessionWfOcrDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SessionWf', 'OcrSession'];

    function SessionWfOcrDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SessionWf, OcrSession) {
        var vm = this;

        vm.sessionWf = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ocrsessions = OcrSession.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sessionWf.id !== null) {
                SessionWf.update(vm.sessionWf, onSaveSuccess, onSaveError);
            } else {
                SessionWf.save(vm.sessionWf, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ocrGatewayApp:sessionWfUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
