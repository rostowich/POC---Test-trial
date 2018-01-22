	'use strict';
	// create the module and name it IndexApp
    var indexApp = angular.module('indexApp', ['ngRoute','angularSpinner','720kb.datepicker']);
    
    // configure our routes
    indexApp.config(function($routeProvider, $locationProvider) {
    	$locationProvider.hashPrefix('');
        $routeProvider.when('/index', {
                templateUrl : '/index.html',
                controller  : 'indexController',
            }).when('/', {
                templateUrl : 'home.html',
                controller  : 'indexController',
            }).when('/register', {
                templateUrl : 'register.html',
                controller  : 'regController',
            }).when('/login', {
                templateUrl : 'login.html',
                controller : 'logController',
            }).when('/user', {
                templateUrl : 'user.html',
                controller : 'userController',
            }).when('/admin', {
                templateUrl : 'admin.html',
                controller  : 'adminController',
            }).when('/order', {
                templateUrl : 'order.html',
                controller  : 'orderController',
            }).otherwise({redirectTo: '/'});
                	
    });

    // create the controller and inject Angular's $scope
    indexApp.controller('indexController',function($rootScope, $scope, $http,$window) {
    
        // create a message to display in our view
        $scope.message = 'Welcome to the Airline travel management portal!';
        $rootScope.permission=window.localStorage['permission'];
        $scope.logout=function(){
    		$http({
    			method: 'POST',
    			url: "/logout",
    		}).then(function successCallBack(response){
    			window.localStorage['isLogged'] = false;
    			$rootScope.authenticated=window.localStorage['isLogged'];
    			$window.location.href = '#/login';
    			location.reload();
    		}, function errorCallBack(response){
    			
    		});
    	}
    });
    
   