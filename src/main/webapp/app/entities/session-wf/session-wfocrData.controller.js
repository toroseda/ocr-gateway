(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('SessionWfOcrDataController', SessionWfOcrDataController);

    SessionWfOcrDataController.$inject = ['$scope', '$state', 'SessionWf', 'SessionWfSearch'];

    function SessionWfOcrDataController ($scope, $state, SessionWf, SessionWfSearch) {
        var vm = this;

        vm.sessionWfs = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            SessionWf.query(function(result) {
                vm.sessionWfs = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SessionWfSearch.query({query: vm.searchQuery}, function(result) {
                vm.sessionWfs = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
