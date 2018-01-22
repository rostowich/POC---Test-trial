'use strict';
var app=angular.module("indexApp");
app.controller("logController", function($rootScope,$scope,$http, $window, usSpinnerService){
	$scope.login=function(){
		usSpinnerService.spin('spinner-3');
		var dataUser={
				"email":$scope.email,
				"password":$scope.password,
		};
		$http({
			method: 'POST',
			url: "/authenticate",
			data: dataUser
		}).then(function successCallBack(response){
			window.localStorage['userLogged'] = angular.toJson(response.data);
			window.localStorage['isLogged'] = true;
			window.localStorage['permission'] = response.data.role;
			$rootScope.userEmail=JSON.parse(window.localStorage['userLogged']).email;
			$rootScope.authenticated=window.localStorage['isLogged'];
			$rootScope.permission=window.localStorage['permission'];
			usSpinnerService.stop('spinner-3');
			$window.location.href = '#/home';
		}, function errorCallBack(response){
			window.localStorage['isLogged'] = false;
			$rootScope.authenticated=window.localStorage['isLogged'];
			usSpinnerService.stop('spinner-3');
			$scope.errorMessage="Incorrect email or password";
			
		});
	}
});