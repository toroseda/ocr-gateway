(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('session-wfocrData', {
            parent: 'entity',
            url: '/session-wfocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.sessionWf.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/session-wf/session-wfsocrData.html',
                    controller: 'SessionWfOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sessionWf');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('wfStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('session-wfocrData-detail', {
            parent: 'entity',
            url: '/session-wfocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.sessionWf.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/session-wf/session-wfocrData-detail.html',
                    controller: 'SessionWfOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sessionWf');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('wfStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SessionWf', function($stateParams, SessionWf) {
                    return SessionWf.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'session-wfocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('session-wfocrData-detail.edit', {
            parent: 'session-wfocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-wf/session-wfocrData-dialog.html',
                    controller: 'SessionWfOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SessionWf', function(SessionWf) {
                            return SessionWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('session-wfocrData.new', {
            parent: 'session-wfocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-wf/session-wfocrData-dialog.html',
                    controller: 'SessionWfOcrDataDialogController',
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
                    $state.go('session-wfocrData', null, { reload: 'session-wfocrData' });
                }, function() {
                    $state.go('session-wfocrData');
                });
            }]
        })
        .state('session-wfocrData.edit', {
            parent: 'session-wfocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-wf/session-wfocrData-dialog.html',
                    controller: 'SessionWfOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SessionWf', function(SessionWf) {
                            return SessionWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('session-wfocrData', null, { reload: 'session-wfocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('session-wfocrData.delete', {
            parent: 'session-wfocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/session-wf/session-wfocrData-delete-dialog.html',
                    controller: 'SessionWfOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SessionWf', function(SessionWf) {
                            return SessionWf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('session-wfocrData', null, { reload: 'session-wfocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
