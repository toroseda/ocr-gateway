(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('OcrSessionOcrDataDialogController', OcrSessionOcrDataDialogController);

    OcrSessionOcrDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'OcrSession', 'SessionWf', 'EdmsRequest'];

    function OcrSessionOcrDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, OcrSession, SessionWf, EdmsRequest) {
        var vm = this;

        vm.ocrSession = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.sessionwfs = SessionWf.query();
        vm.edmsrequests = EdmsRequest.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ocrSession.id !== null) {
                OcrSession.update(vm.ocrSession, onSaveSuccess, onSaveError);
            } else {
                OcrSession.save(vm.ocrSession, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ocrGatewayApp:ocrSessionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setRequestData = function ($file, ocrSession) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        ocrSession.requestData = base64Data;
                        ocrSession.requestDataContentType = $file.type;
                    });
                });
            }
        };

    }
})();
