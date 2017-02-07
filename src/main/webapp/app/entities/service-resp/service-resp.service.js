(function() {
    'use strict';
    angular
        .module('ocrGatewayApp')
        .factory('ServiceResp', ServiceResp);

    ServiceResp.$inject = ['$resource', 'DateUtils'];

    function ServiceResp ($resource, DateUtils) {
        var resourceUrl =  'api/service-resps/:id';

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
