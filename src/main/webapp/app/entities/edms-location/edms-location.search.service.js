(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .factory('EdmsLocationSearch', EdmsLocationSearch);

    EdmsLocationSearch.$inject = ['$resource'];

    function EdmsLocationSearch($resource) {
        var resourceUrl =  'api/_search/edms-locations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
