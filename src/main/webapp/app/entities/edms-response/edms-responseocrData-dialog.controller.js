(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsResponseOcrDataDialogController', EdmsResponseOcrDataDialogController);

    EdmsResponseOcrDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'EdmsResponse', 'EdmsRequest'];

    function EdmsResponseOcrDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, EdmsResponse, EdmsRequest) {
        var vm = this;

        vm.edmsResponse = entity;
        vm.clear = clear;
        vm.save = save;
        vm.edmsrequests = EdmsRequest.query({filter: 'edmsresponse-is-null'});
        $q.all([vm.edmsResponse.$promise, vm.edmsrequests.$promise]).then(function() {
            if (!vm.edmsResponse.edmsRequestId) {
                return $q.reject();
            }
            return EdmsRequest.get({id : vm.edmsResponse.edmsRequestId}).$promise;
        }).then(function(edmsRequest) {
            vm.edmsrequests.push(edmsRequest);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.edmsResponse.id !== null) {
                EdmsResponse.update(vm.edmsResponse, onSaveSuccess, onSaveError);
            } else {
                EdmsResponse.save(vm.edmsResponse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ocrGatewayApp:edmsResponseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
