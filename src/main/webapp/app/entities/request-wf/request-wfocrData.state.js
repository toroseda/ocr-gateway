(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('request-wfocrData', {
            parent: 'entity',
            url: '/request-wfocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.requestWf.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/request-wf/request-wfsocrData.html',
                    controller: 'RequestWfOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('requestWf');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('wfStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('request-wfocrData-detail', {
            parent: 'entity',
            url: '/request-wfocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.requestWf.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/request-wf/request-wfocrData-detail.html',
                    controller: 'RequestWfOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('requestWf');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('wfStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RequestWf', function($stateParams, RequestWf) {
                    return RequestWf.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'request-wfocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('request-wfocrData-detail.edit', {
            parent: 'request-wfocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-wf/request-wfocrData-dialog.html',
                    controller: 'RequestWfOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RequestWf', function(RequestWf) {
                            return RequestWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('request-wfocrData.new', {
            parent: 'request-wfocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-wf/request-wfocrData-dialog.html',
                    controller: 'RequestWfOcrDataDialogController',
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
                    $state.go('request-wfocrData', null, { reload: 'request-wfocrData' });
                }, function() {
                    $state.go('request-wfocrData');
                });
            }]
        })
        .state('request-wfocrData.edit', {
            parent: 'request-wfocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-wf/request-wfocrData-dialog.html',
                    controller: 'RequestWfOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RequestWf', function(RequestWf) {
                            return RequestWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('request-wfocrData', null, { reload: 'request-wfocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('request-wfocrData.delete', {
            parent: 'request-wfocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-wf/request-wfocrData-delete-dialog.html',
                    controller: 'RequestWfOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RequestWf', function(RequestWf) {
                            return RequestWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('request-wfocrData', null, { reload: 'request-wfocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
