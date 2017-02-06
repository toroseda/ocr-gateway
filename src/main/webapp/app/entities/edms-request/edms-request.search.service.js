(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .factory('EdmsRequestSearch', EdmsRequestSearch);

    EdmsRequestSearch.$inject = ['$resource'];

    function EdmsRequestSearch($resource) {
        var resourceUrl =  'api/_search/edms-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
