(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('EdmsLocation', EdmsLocation);

    EdmsLocation.$inject = ['$resource'];

    function EdmsLocation ($resource) {
        var resourceUrl =  'api/edms-locations/:id';

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
