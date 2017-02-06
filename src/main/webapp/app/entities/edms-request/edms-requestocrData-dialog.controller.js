(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsRequestOcrDataDialogController', EdmsRequestOcrDataDialogController);

    EdmsRequestOcrDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EdmsRequest', 'OcrSession', 'RequestWf'];

    function EdmsRequestOcrDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EdmsRequest, OcrSession, RequestWf) {
        var vm = this;

        vm.edmsRequest = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.ocrsessions = OcrSession.query();
        vm.requestwfs = RequestWf.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.edmsRequest.id !== null) {
                EdmsRequest.update(vm.edmsRequest, onSaveSuccess, onSaveError);
            } else {
                EdmsRequest.save(vm.edmsRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ocrGatewayApp:edmsRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;
        vm.datePickerOpenStatus.lastRunDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
