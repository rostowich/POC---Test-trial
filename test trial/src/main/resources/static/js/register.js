'use strict';
var app=angular.module("indexApp");
app.controller("regController", function($scope,$http, usSpinnerService){
	$scope.userinfo={};
	$scope.register=function(email, password){
		usSpinnerService.spin('spinner-5');
		var dataUser={
				"email":$scope.email,
				"password":$scope.password,
				"confirmPassword":$scope.passwordConfirm
		};
		$http({
			method: 'POST',
			url: "/users/register",
			data: dataUser
		}).then(function successCallBack(response){
			$scope.userinfo=response.data;
			$scope.errorMessage="";
			$scope.sucessRegistration="You have been successfully registered. You can now log in";
			usSpinnerService.stop('spinner-5');
		}, function errorCallBack(response){
			$scope.errorMessage=response.data.message;
			usSpinnerService.stop('spinner-5');
		});
	}

	});