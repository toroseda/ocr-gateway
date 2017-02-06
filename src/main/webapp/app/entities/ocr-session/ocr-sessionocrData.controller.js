(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('OcrSessionOcrDataController', OcrSessionOcrDataController);

    OcrSessionOcrDataController.$inject = ['$scope', '$state', 'DataUtils', 'OcrSession', 'OcrSessionSearch'];

    function OcrSessionOcrDataController ($scope, $state, DataUtils, OcrSession, OcrSessionSearch) {
        var vm = this;

        vm.ocrSessions = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            OcrSession.query(function(result) {
                vm.ocrSessions = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            OcrSessionSearch.query({query: vm.searchQuery}, function(result) {
                vm.ocrSessions = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
