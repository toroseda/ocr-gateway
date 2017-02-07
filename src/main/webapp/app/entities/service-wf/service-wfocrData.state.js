(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-wfocrData', {
            parent: 'entity',
            url: '/service-wfocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.serviceWf.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-wf/service-wfsocrData.html',
                    controller: 'ServiceWfOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceWf');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('wfStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-wfocrData-detail', {
            parent: 'entity',
            url: '/service-wfocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.serviceWf.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-wf/service-wfocrData-detail.html',
                    controller: 'ServiceWfOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceWf');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('wfStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceWf', function($stateParams, ServiceWf) {
                    return ServiceWf.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'service-wfocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('service-wfocrData-detail.edit', {
            parent: 'service-wfocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-wf/service-wfocrData-dialog.html',
                    controller: 'ServiceWfOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceWf', function(ServiceWf) {
                            return ServiceWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-wfocrData.new', {
            parent: 'service-wfocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-wf/service-wfocrData-dialog.html',
                    controller: 'ServiceWfOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                wfStatus: null,
                                updatedBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-wfocrData', null, { reload: 'service-wfocrData' });
                }, function() {
                    $state.go('service-wfocrData');
                });
            }]
        })
        .state('service-wfocrData.edit', {
            parent: 'service-wfocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-wf/service-wfocrData-dialog.html',
                    controller: 'ServiceWfOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceWf', function(ServiceWf) {
                            return ServiceWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-wfocrData', null, { reload: 'service-wfocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-wfocrData.delete', {
            parent: 'service-wfocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-wf/service-wfocrData-delete-dialog.html',
                    controller: 'ServiceWfOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceWf', function(ServiceWf) {
                            return ServiceWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-wfocrData', null, { reload: 'service-wfocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
