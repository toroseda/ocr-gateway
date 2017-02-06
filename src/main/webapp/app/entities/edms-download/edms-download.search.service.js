(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .factory('EdmsDownloadSearch', EdmsDownloadSearch);

    EdmsDownloadSearch.$inject = ['$resource'];

    function EdmsDownloadSearch($resource) {
        var resourceUrl =  'api/_search/edms-downloads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
