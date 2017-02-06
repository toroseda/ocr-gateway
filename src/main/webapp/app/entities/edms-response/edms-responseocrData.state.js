(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('edms-responseocrData', {
            parent: 'entity',
            url: '/edms-responseocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsResponse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-response/edms-responsesocrData.html',
                    controller: 'EdmsResponseOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsResponse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('edms-responseocrData-detail', {
            parent: 'entity',
            url: '/edms-responseocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsResponse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-response/edms-responseocrData-detail.html',
                    controller: 'EdmsResponseOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsResponse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EdmsResponse', function($stateParams, EdmsResponse) {
                    return EdmsResponse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'edms-responseocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('edms-responseocrData-detail.edit', {
            parent: 'edms-responseocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-response/edms-responseocrData-dialog.html',
                    controller: 'EdmsResponseOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsResponse', function(EdmsResponse) {
                            return EdmsResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-responseocrData.new', {
            parent: 'edms-responseocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-response/edms-responseocrData-dialog.html',
                    controller: 'EdmsResponseOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                accountNumber: null,
                                subRequestId: null,
                                requestDocType: null,
                                errorCode: null,
                                errorDescription: null,
                                promisedDirectory: null,
                                responseDocType: null,
                                responseSubRequestId: null,
                                responseAeaCode: null,
                                responseDocCount: null,
                                directoryAvilableFlg: null,
                                fileCount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('edms-responseocrData', null, { reload: 'edms-responseocrData' });
                }, function() {
                    $state.go('edms-responseocrData');
                });
            }]
        })
        .state('edms-responseocrData.edit', {
            parent: 'edms-responseocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-response/edms-responseocrData-dialog.html',
                    controller: 'EdmsResponseOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsResponse', function(EdmsResponse) {
                            return EdmsResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-responseocrData', null, { reload: 'edms-responseocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-responseocrData.delete', {
            parent: 'edms-responseocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-response/edms-responseocrData-delete-dialog.html',
                    controller: 'EdmsResponseOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EdmsResponse', function(EdmsResponse) {
                            return EdmsResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-responseocrData', null, { reload: 'edms-responseocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
