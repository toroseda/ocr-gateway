(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('EdmsResponse', EdmsResponse);

    EdmsResponse.$inject = ['$resource'];

    function EdmsResponse ($resource) {
        var resourceUrl =  'api/edms-responses/:id';

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
