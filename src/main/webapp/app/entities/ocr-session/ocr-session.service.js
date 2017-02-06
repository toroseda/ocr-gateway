(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('OcrSession', OcrSession);

    OcrSession.$inject = ['$resource'];

    function OcrSession ($resource) {
        var resourceUrl =  'api/ocr-sessions/:id';

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
