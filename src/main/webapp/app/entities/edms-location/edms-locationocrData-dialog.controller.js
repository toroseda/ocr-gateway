(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsLocationOcrDataDialogController', EdmsLocationOcrDataDialogController);

    EdmsLocationOcrDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'EdmsLocation', 'EdmsResponse', 'EdmsDownload'];

    function EdmsLocationOcrDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, EdmsLocation, EdmsResponse, EdmsDownload) {
        var vm = this;

        vm.edmsLocation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.edmsresponses = EdmsResponse.query({filter: 'edmslocation-is-null'});
        $q.all([vm.edmsLocation.$promise, vm.edmsresponses.$promise]).then(function() {
            if (!vm.edmsLocation.edmsResponseId) {
                return $q.reject();
            }
            return EdmsResponse.get({id : vm.edmsLocation.edmsResponseId}).$promise;
        }).then(function(edmsResponse) {
            vm.edmsresponses.push(edmsResponse);
        });
        vm.edmsdownloads = EdmsDownload.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.edmsLocation.id !== null) {
                EdmsLocation.update(vm.edmsLocation, onSaveSuccess, onSaveError);
            } else {
                EdmsLocation.save(vm.edmsLocation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ocrGatewayApp:edmsLocationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
