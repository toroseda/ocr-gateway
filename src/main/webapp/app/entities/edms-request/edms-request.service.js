(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('EdmsRequest', EdmsRequest);

    EdmsRequest.$inject = ['$resource', 'DateUtils'];

    function EdmsRequest ($resource, DateUtils) {
        var resourceUrl =  'api/edms-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                        data.lastRunDate = DateUtils.convertDateTimeFromServer(data.lastRunDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
