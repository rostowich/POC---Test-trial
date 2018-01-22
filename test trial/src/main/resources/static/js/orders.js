'use strict';
var app=angular.module("indexApp");

app.controller("orderController", function($rootScope,$scope,$http, usSpinnerService){
	$scope.currentPage=0;
	$rootScope.authenticated=window.localStorage['isLogged'];
	$rootScope.userEmail=JSON.parse(window.localStorage['userLogged']).email;
	$rootScope.permission=window.localStorage['permission'];
	
	$scope.findTickets=function(){
		usSpinnerService.spin('spinner-2');
		var url="/tickets/all?";
		if(!angular.isUndefined($scope.ticketId) && $scope.ticketId!="")
			url=url+"id="+$scope.ticketId+"&";
		if(!angular.isUndefined($scope.departure) && $scope.departure!="")
			url=url+"departure="+$scope.departure+"&";
		if(!angular.isUndefined($scope.destination) && $scope.destination!="")
			url=url+"destination="+$scope.destination+"&";
		if(!angular.isUndefined($scope.amount) && $scope.amount!="")
			url=url+"amount="+$scope.amount+"&";
		if(!angular.isUndefined($scope.number) && $scope.number!="")
			url=url+"number="+$scope.number+"&";
		if(!angular.isUndefined($scope.beginDate) && $scope.beginDate!="")
			url=url+"beginDate="+$scope.beginDate+"&";
		if(!angular.isUndefined($scope.endDate) && $scope.endDate!="")
			url=url+"endDate="+$scope.endDate+"&";
		if(!angular.isUndefined($scope.email) && $scope.email!="")
			url=url+"email="+$scope.email+"&";
		$http({
			method: 'GET',
			url: url+"page="+$scope.currentPage
		}).then(function successCallBack(response){
			usSpinnerService.stop('spinner-2');
			$scope.tickets=response.data;
			$scope.pages=new Array(response.data.totalPages);
			$scope.totalElement=response.data.totalElements;
			$scope.numberElement=response.data.numberOfElements;
		}, function errorCallBack(response){
			usSpinnerService.stop('spinner-2');
		});	
	 }
	$scope.findTickets();
	
	$scope.gotoPage=function(p){
		$scope.currentPage=p;
		$scope.findTickets();
	}

    });

	