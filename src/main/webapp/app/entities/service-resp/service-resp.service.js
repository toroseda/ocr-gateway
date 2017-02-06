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
                        data.startDate = DateUtils.convertLocalDateFromServer(data.startDate);
                        data.endDate = DateUtils.convertLocalDateFromServer(data.endDate);
                        data.lastRunDate = DateUtils.convertLocalDateFromServer(data.lastRunDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startDate = DateUtils.convertLocalDateToServer(copy.startDate);
                    copy.endDate = DateUtils.convertLocalDateToServer(copy.endDate);
                    copy.lastRunDate = DateUtils.convertLocalDateToServer(copy.lastRunDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startDate = DateUtils.convertLocalDateToServer(copy.startDate);
                    copy.endDate = DateUtils.convertLocalDateToServer(copy.endDate);
                    copy.lastRunDate = DateUtils.convertLocalDateToServer(copy.lastRunDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
