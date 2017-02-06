(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsRequestOcrDataController', EdmsRequestOcrDataController);

    EdmsRequestOcrDataController.$inject = ['$scope', '$state', 'EdmsRequest', 'EdmsRequestSearch'];

    function EdmsRequestOcrDataController ($scope, $state, EdmsRequest, EdmsRequestSearch) {
        var vm = this;

        vm.edmsRequests = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EdmsRequest.query(function(result) {
                vm.edmsRequests = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EdmsRequestSearch.query({query: vm.searchQuery}, function(result) {
                vm.edmsRequests = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
