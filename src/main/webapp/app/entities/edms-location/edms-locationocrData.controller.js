(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .controller('EdmsLocationOcrDataController', EdmsLocationOcrDataController);

    EdmsLocationOcrDataController.$inject = ['$scope', '$state', 'EdmsLocation', 'EdmsLocationSearch'];

    function EdmsLocationOcrDataController ($scope, $state, EdmsLocation, EdmsLocationSearch) {
        var vm = this;

        vm.edmsLocations = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EdmsLocation.query(function(result) {
                vm.edmsLocations = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EdmsLocationSearch.query({query: vm.searchQuery}, function(result) {
                vm.edmsLocations = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
