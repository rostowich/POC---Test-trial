'use strict';
var app=angular.module("indexApp");
app.controller("userController", function($rootScope, $scope, $http, $window, usSpinnerService){
	$rootScope.authenticated=window.localStorage['isLogged'];
	$rootScope.permission=window.localStorage['permission'];
	$scope.currentUserTicketPage=0;
	$scope.userinfo=JSON.parse(window.localStorage['userLogged']);
	$rootScope.userEmail=$scope.userinfo.email;
	$scope.totalElement=0;
	$scope.numberElement=0;
	$scope.loadTicket=function(id){
		$http({
			method: 'GET',
			url: "/tickets/user/"+id+"?page="+$scope.currentUserTicketPage
		}).then(function successCallBack(response){
			$scope.usertickets=response.data;
			$scope.pages=new Array(response.data.totalPages);
			$scope.totalElement=response.data.totalElements;
			$scope.numberElement=response.data.numberOfElements;
		}, function errorCallBack(response){
				
		});
	}
	
	$scope.loadUserInfo=function(id){
		$http({
			method: 'GET',
			url: "/users/"+id
		}).then(function successCallBack(response){
			$scope.userinfo=response.data;
		}, function errorCallBack(response){
				
		});
	}
	
	$scope.gotoPage=function(p,id){
		$scope.currentUserTicketPage=p;
		$scope.loadTicket(id);
	}
	
	$scope.loadOffer=function(){
		
		$http({
			method: 'GET',
			url: "/offers"
		}).then(function successCallBack(response){
				$scope.offers=response.data;
				usSpinnerService.stop('spinner-4');
		}, function errorCallBack(response){
			    usSpinnerService.stop('spinner-4');
		});
	}
	$scope.loadUser=function(){
		$scope.loadUserInfo($scope.userinfo.userId);
		$scope.loadTicket($scope.userinfo.userId);
		$scope.loadOffer();
	}
	usSpinnerService.spin('spinner-4');
	$scope.loadUser();
	
	$scope.purchaseTickets=function(number,Offer,user){
		usSpinnerService.spin('spinner-4');
		var postData={
				"number":number,
				"userId":user,
				"offer":{
					"route":{
						"from":Offer.route.from,
						"to":Offer.route.to
					},
					"price":{
						"amount":Offer.price.amount,
						"currency":Offer.price.currency
					}
				},
				
		};
		
		$http({
			method: 'POST',
			data:postData,
			url: "/tickets/user/purchase"
		}).then(function successCallBack(response){
			$scope.successPurchase="Ticket purchased with success. You can download your receipt or get details by mail";
			$scope.errorMessage="";
			$scope.loadUser();
			usSpinnerService.stop('spinner-4');
		}, function errorCallBack(response){
			$scope.successPurchase="";
			$scope.errorMessage=response.data.message;
			usSpinnerService.stop('spinner-4');
		});	
	 }	
	
	var getMailResponseData=function(id){
		usSpinnerService.spin('spinner-4');
		return $http({
			method: 'GET',
			url: "/mail/"+id
		}).then(function (result){
			$scope.emailMessage="Please verify your email address";
			usSpinnerService.stop('spinner-4');
			return result.data;
		},function errorCallBack(response){
			$scope.emailMessage="Please verify your email address";
			usSpinnerService.stop('spinner-4');
		});
	};
	
	$scope.sendMail=function(id){
		
		var myDataPromise=getMailResponseData(id);
		myDataPromise.then(function (result) {
			
		});
		
	}
	$scope.download=function(id){
		usSpinnerService.spin('spinner-4');
		$http({
			method: 'GET',
			url: "/pdf/"+id
		}).then(function successCallBack(response){
			window.location.assign("/pdf/"+id);
			usSpinnerService.stop('spinner-4');
		}, function errorCallBack(response){
				
		});
	}
	});