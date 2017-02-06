(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('edms-requestocrData', {
            parent: 'entity',
            url: '/edms-requestocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-request/edms-requestsocrData.html',
                    controller: 'EdmsRequestOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('edms-requestocrData-detail', {
            parent: 'entity',
            url: '/edms-requestocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-request/edms-requestocrData-detail.html',
                    controller: 'EdmsRequestOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EdmsRequest', function($stateParams, EdmsRequest) {
                    return EdmsRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'edms-requestocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('edms-requestocrData-detail.edit', {
            parent: 'edms-requestocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-request/edms-requestocrData-dialog.html',
                    controller: 'EdmsRequestOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsRequest', function(EdmsRequest) {
                            return EdmsRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-requestocrData.new', {
            parent: 'edms-requestocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-request/edms-requestocrData-dialog.html',
                    controller: 'EdmsRequestOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                accountNumber: null,
                                subRequestId: null,
                                areaCode: null,
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
                    $state.go('edms-requestocrData', null, { reload: 'edms-requestocrData' });
                }, function() {
                    $state.go('edms-requestocrData');
                });
            }]
        })
        .state('edms-requestocrData.edit', {
            parent: 'edms-requestocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-request/edms-requestocrData-dialog.html',
                    controller: 'EdmsRequestOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsRequest', function(EdmsRequest) {
                            return EdmsRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-requestocrData', null, { reload: 'edms-requestocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-requestocrData.delete', {
            parent: 'edms-requestocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-request/edms-requestocrData-delete-dialog.html',
                    controller: 'EdmsRequestOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EdmsRequest', function(EdmsRequest) {
                            return EdmsRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-requestocrData', null, { reload: 'edms-requestocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
