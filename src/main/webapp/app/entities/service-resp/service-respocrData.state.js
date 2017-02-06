(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-respocrData', {
            parent: 'entity',
            url: '/service-respocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.serviceResp.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-resp/service-respsocrData.html',
                    controller: 'ServiceRespOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceResp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-respocrData-detail', {
            parent: 'entity',
            url: '/service-respocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.serviceResp.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-resp/service-respocrData-detail.html',
                    controller: 'ServiceRespOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceResp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceResp', function($stateParams, ServiceResp) {
                    return ServiceResp.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'service-respocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('service-respocrData-detail.edit', {
            parent: 'service-respocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-resp/service-respocrData-dialog.html',
                    controller: 'ServiceRespOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceResp', function(ServiceResp) {
                            return ServiceResp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-respocrData.new', {
            parent: 'service-respocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-resp/service-respocrData-dialog.html',
                    controller: 'ServiceRespOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rawJson: null,
                                rawJsonContentType: null,
                                documentImage: null,
                                startDate: null,
                                endDate: null,
                                lastRunBy: null,
                                lastRunDur: null,
                                lastRunDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-respocrData', null, { reload: 'service-respocrData' });
                }, function() {
                    $state.go('service-respocrData');
                });
            }]
        })
        .state('service-respocrData.edit', {
            parent: 'service-respocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-resp/service-respocrData-dialog.html',
                    controller: 'ServiceRespOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceResp', function(ServiceResp) {
                            return ServiceResp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-respocrData', null, { reload: 'service-respocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-respocrData.delete', {
            parent: 'service-respocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-resp/service-respocrData-delete-dialog.html',
                    controller: 'ServiceRespOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceResp', function(ServiceResp) {
                            return ServiceResp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-respocrData', null, { reload: 'service-respocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
