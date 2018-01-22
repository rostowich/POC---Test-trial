'use strict';
var app=angular.module("indexApp");
app.controller("adminController", function($rootScope, $scope, $http, usSpinnerService){
	$rootScope.authenticated=window.localStorage['isLogged'];
	$rootScope.userEmail=JSON.parse(window.localStorage['userLogged']).email;
	$rootScope.permission=window.localStorage['permission'];
	$scope.currentPage=0;
	$scope.email="";
	$scope.tickets=null;
	$scope.currentUserTicket="";
	
	$scope.findUser=function(){
			usSpinnerService.spin('spinner-1');
			$http({
				method: 'GET',
				url: "/users/get?email="+$scope.email+"&page="+$scope.currentPage
			}).then(function successCallBack(response){
				$scope.users=response.data;
				$scope.totalElement1=response.data.totalElements;
				$scope.numberElement1=response.data.numberOfElements;
				$scope.pages=new Array(response.data.totalPages);
				usSpinnerService.stop('spinner-1');
			}, function errorCallBack(response){
				usSpinnerService.stop('spinner-1');
			});	
		 
	 }
	
	$scope.findUser();
	
	$scope.gotoPage=function(p){
		$scope.currentPage=p;
		$scope.findUser();
	}
	
	$scope.gotoPageTicket=function(p,id,email){
		$scope.getTickets(id,email,p)
	}
	
	$scope.getTickets=function(id,email,currentTicketPage){
		$scope.currentTicketPage=currentTicketPage;
		$http({
			method: 'GET',
			url: "/tickets/user/"+id+"?page="+currentTicketPage
		}).then(function successCallBack(response){
			$scope.tickets=response.data;
			$scope.ticketPages=new Array(response.data.totalPages);
			$scope.currentUserTicket=email;
			$scope.currentUserId=id;
			$scope.totalElement2=response.data.totalElements;
			$scope.numberElement2=response.data.numberOfElements;
		}, function errorCallBack(response){
			
		});
	}	
    });