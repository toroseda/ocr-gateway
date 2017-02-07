(function() {
    'use strict';

    angular
        .module('ocrGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ocr-sessionocrData', {
            parent: 'entity',
            url: '/ocr-sessionocrData',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.ocrSession.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ocr-session/ocr-sessionsocrData.html',
                    controller: 'OcrSessionOcrDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ocrSession');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ocr-sessionocrData-detail', {
            parent: 'entity',
            url: '/ocr-sessionocrData/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ocrGatewayApp.ocrSession.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ocr-session/ocr-sessionocrData-detail.html',
                    controller: 'OcrSessionOcrDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ocrSession');
                    $translatePartialLoader.addPart('status');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OcrSession', function($stateParams, OcrSession) {
                    return OcrSession.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ocr-sessionocrData',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ocr-sessionocrData-detail.edit', {
            parent: 'ocr-sessionocrData-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ocr-session/ocr-sessionocrData-dialog.html',
                    controller: 'OcrSessionOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OcrSession', function(OcrSession) {
                            return OcrSession.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ocr-sessionocrData.new', {
            parent: 'ocr-sessionocrData',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ocr-session/ocr-sessionocrData-dialog.html',
                    controller: 'OcrSessionOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                status: null,
                                serverFilePath: null,
                                filename: null,
                                requestData: null,
                                requestDataContentType: null,
                                updatedBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ocr-sessionocrData', null, { reload: 'ocr-sessionocrData' });
                }, function() {
                    $state.go('ocr-sessionocrData');
                });
            }]
        })
        .state('ocr-sessionocrData.edit', {
            parent: 'ocr-sessionocrData',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ocr-session/ocr-sessionocrData-dialog.html',
                    controller: 'OcrSessionOcrDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OcrSession', function(OcrSession) {
                            return OcrSession.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ocr-sessionocrData', null, { reload: 'ocr-sessionocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ocr-sessionocrData.delete', {
            parent: 'ocr-sessionocrData',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ocr-session/ocr-sessionocrData-delete-dialog.html',
                    controller: 'OcrSessionOcrDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OcrSession', function(OcrSession) {
                            return OcrSession.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ocr-sessionocrData', null, { reload: 'ocr-sessionocrData' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
