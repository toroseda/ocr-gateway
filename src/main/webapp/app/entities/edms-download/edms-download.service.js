(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('EdmsDownload', EdmsDownload);

    EdmsDownload.$inject = ['$resource'];

    function EdmsDownload ($resource) {
        var resourceUrl =  'api/edms-downloads/:id';

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
