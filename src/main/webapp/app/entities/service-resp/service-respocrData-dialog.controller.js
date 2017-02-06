(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('ServiceRespOcrDataDialogController', ServiceRespOcrDataDialogController);

    ServiceRespOcrDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'ServiceResp', 'EdmsDownload', 'ServiceWf'];

    function ServiceRespOcrDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, ServiceResp, EdmsDownload, ServiceWf) {
        var vm = this;

        vm.serviceResp = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.edmsdownloads = EdmsDownload.query({filter: 'serviceresp-is-null'});
        $q.all([vm.serviceResp.$promise, vm.edmsdownloads.$promise]).then(function() {
            if (!vm.serviceResp.edmsDownloadId) {
                return $q.reject();
            }
            return EdmsDownload.get({id : vm.serviceResp.edmsDownloadId}).$promise;
        }).then(function(edmsDownload) {
            vm.edmsdownloads.push(edmsDownload);
        });
        vm.servicewfs = ServiceWf.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceResp.id !== null) {
                ServiceResp.update(vm.serviceResp, onSaveSuccess, onSaveError);
            } else {
                ServiceResp.save(vm.serviceResp, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ocrGatewayApp:serviceRespUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setRawJson = function ($file, serviceResp) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        serviceResp.rawJson = base64Data;
                        serviceResp.rawJsonContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;
        vm.datePickerOpenStatus.lastRunDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
