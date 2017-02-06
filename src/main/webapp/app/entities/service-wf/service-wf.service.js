(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('ServiceWf', ServiceWf);

    ServiceWf.$inject = ['$resource'];

    function ServiceWf ($resource) {
        var resourceUrl =  'api/service-wfs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
