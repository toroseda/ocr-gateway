(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsResponseOcrDataController', EdmsResponseOcrDataController);

    EdmsResponseOcrDataController.$inject = ['$scope', '$state', 'EdmsResponse', 'EdmsResponseSearch'];

    function EdmsResponseOcrDataController ($scope, $state, EdmsResponse, EdmsResponseSearch) {
        var vm = this;

        vm.edmsResponses = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EdmsResponse.query(function(result) {
                vm.edmsResponses = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EdmsResponseSearch.query({query: vm.searchQuery}, function(result) {
                vm.edmsResponses = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
