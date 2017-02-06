(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('SessionWf', SessionWf);

    SessionWf.$inject = ['$resource'];

    function SessionWf ($resource) {
        var resourceUrl =  'api/session-wfs/:id';

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
