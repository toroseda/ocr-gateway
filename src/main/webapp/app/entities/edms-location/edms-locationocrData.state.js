(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('edms-locationocrData', {
            parent: 'entity',
            url: '/edms-locationocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsLocation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-location/edms-locationsocrData.html',
                    controller: 'EdmsLocationOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsLocation');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('edms-locationocrData-detail', {
            parent: 'entity',
            url: '/edms-locationocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsLocation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-location/edms-locationocrData-detail.html',
                    controller: 'EdmsLocationOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsLocation');
                    $translatePartialLoader.addPart('status');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EdmsLocation', function($stateParams, EdmsLocation) {
                    return EdmsLocation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'edms-locationocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('edms-locationocrData-detail.edit', {
            parent: 'edms-locationocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-location/edms-locationocrData-dialog.html',
                    controller: 'EdmsLocationOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsLocation', function(EdmsLocation) {
                            return EdmsLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-locationocrData.new', {
            parent: 'edms-locationocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-location/edms-locationocrData-dialog.html',
                    controller: 'EdmsLocationOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                actualDirectory: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('edms-locationocrData', null, { reload: 'edms-locationocrData' });
                }, function() {
                    $state.go('edms-locationocrData');
                });
            }]
        })
        .state('edms-locationocrData.edit', {
            parent: 'edms-locationocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-location/edms-locationocrData-dialog.html',
                    controller: 'EdmsLocationOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsLocation', function(EdmsLocation) {
                            return EdmsLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-locationocrData', null, { reload: 'edms-locationocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-locationocrData.delete', {
            parent: 'edms-locationocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-location/edms-locationocrData-delete-dialog.html',
                    controller: 'EdmsLocationOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EdmsLocation', function(EdmsLocation) {
                            return EdmsLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-locationocrData', null, { reload: 'edms-locationocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
