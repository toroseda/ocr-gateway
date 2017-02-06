(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('edms-downloadocrData', {
            parent: 'entity',
            url: '/edms-downloadocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsDownload.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-download/edms-downloadsocrData.html',
                    controller: 'EdmsDownloadOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsDownload');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('edms-downloadocrData-detail', {
            parent: 'entity',
            url: '/edms-downloadocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.edmsDownload.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/edms-download/edms-downloadocrData-detail.html',
                    controller: 'EdmsDownloadOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('edmsDownload');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EdmsDownload', function($stateParams, EdmsDownload) {
                    return EdmsDownload.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'edms-downloadocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('edms-downloadocrData-detail.edit', {
            parent: 'edms-downloadocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-download/edms-downloadocrData-dialog.html',
                    controller: 'EdmsDownloadOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsDownload', function(EdmsDownload) {
                            return EdmsDownload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-downloadocrData.new', {
            parent: 'edms-downloadocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-download/edms-downloadocrData-dialog.html',
                    controller: 'EdmsDownloadOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                actualDirectory: null,
                                actualFilename: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('edms-downloadocrData', null, { reload: 'edms-downloadocrData' });
                }, function() {
                    $state.go('edms-downloadocrData');
                });
            }]
        })
        .state('edms-downloadocrData.edit', {
            parent: 'edms-downloadocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-download/edms-downloadocrData-dialog.html',
                    controller: 'EdmsDownloadOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EdmsDownload', function(EdmsDownload) {
                            return EdmsDownload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-downloadocrData', null, { reload: 'edms-downloadocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('edms-downloadocrData.delete', {
            parent: 'edms-downloadocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/edms-download/edms-downloadocrData-delete-dialog.html',
                    controller: 'EdmsDownloadOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EdmsDownload', function(EdmsDownload) {
                            return EdmsDownload.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('edms-downloadocrData', null, { reload: 'edms-downloadocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
