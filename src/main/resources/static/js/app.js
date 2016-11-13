var jobSchedulerApp = angular.module("jobSchedulerApp", ['ngRoute', 'ngCookies', 'angular-jwt', 'ngMaterial']);

jobSchedulerApp.config(function($routeProvider) {

	   $routeProvider	   
	   .when('/login', {		 
		   controller: 'loginController',
		   templateUrl: 'views/login.html',
		   cache: 'false'
	   })
	   .when('/workorder', {
		   controller: 'workOrderController',
		   templateUrl: 'views/addworkorder.html',
		   cache: 'false'
	   })
	   .when('/viewworkorder', {
		   controller: 'viewWorkOrderController',
		   templateUrl: 'views/viewworkorder.html',
		   cache: 'false'
	   })	   
	   .when('/myschedule', {
		   controller: 'myScheduleController',
		   templateUrl: 'views/myschedule.html',
		   cache: 'false'
	   })	   
	   .otherwise({
	      redirectTo: '/'
	   });
		
});

jobSchedulerApp.controller("routeController", 
	function ($scope,$location,$http,$cookies,jwtHelper,$rootScope, $document) {

	
		var token = $cookies.get("access_token");
		
		if( !angular.isUndefined(token) ) {
			
			$rootScope.authenticated = true;
			
			var payload = jwtHelper.decodeToken(token);
			if (payload.roles == "CUSTOMERCARE")
				$location.path("/viewworkorder"); 
			else
				$location.path('/myschedule');
		}		
		else {
			$rootScope.authenticated = false;

			$location.path('/login');
		}

		$scope.logout = function() {
			
			$rootScope.authenticated = false;

			$cookies.remove("access_token");
			$cookies.remove("username");
			 $rootScope.username = "";
				
			 window.location.href = '/login'; 
			window.location.reload(true) ;
	        }
	 
	}
);

jobSchedulerApp.controller("loginController",
	function ($scope, $location, $http, $cookies, jwtHelper,$rootScope)	{
	
	    $rootScope.authenticated = false;
	
		$scope.dologin = function()	{
			
			//angular.element(document.getElementById('logoutbtn'))[0].disabled = false;
			$rootScope.username = $scope.form.username;
			$cookies.put("username", $scope.form.username);

			$http({
			    method : "POST",
			    url : "/jobscheduler/authenticate",
			    data : angular.toJson($scope.form),
			    headers : {
			    	'Content-Type': 'application/json',
			        'Accept' : 'application/json'
			    }
			})
			.then( handleSuccess, handleError );			
 
	        function handleSuccess(response) {
	        		        		        	
	        	
	        	$rootScope.authenticated = true;	        		        		        	

				$cookies.put("access_token", response.data.token);
				var payload = jwtHelper.decodeToken(response.data.token);
				
				if (payload.roles == "CUSTOMERCARE"){	

					$location.path("/viewworkorder"); 
	        }
				else
					$location.path('/myschedule');
	        	}
	 
	        function handleError(response) {
	        	  $rootScope.authenticated = false;

				angular.element(document.querySelector("#divstatus")).css("visibility", "visible");
				angular.element(document.querySelector("#divstatus")).addClass("alert-danger");
				$scope.statusmsg="Error: " + response.data.message;
	        }			
		};
	}
);

jobSchedulerApp.controller("workOrderController",
	function ($scope, $location, $http,$rootScope, $cookies)	{

		
	$rootScope.authenticated = true;
		
		$http({
		    method : "GET",
		    url : "/jobscheduler/skills"
		 
		})
		.then( handleSuccess, handleError );			

        function handleSuccess(response) {
        	//alert(response.data);
        	
        	$scope.availableOptions =response.data;

        }
 
        function handleError(response) {
			angular.element(document.querySelector("#divstatus")).css("visibility", "visible");
			angular.element(document.querySelector("#divstatus")).addClass("alert-danger");
			$scope.statusmsg="Error: " + response.data.message;        	
        }					
		
		
		
		
		$rootScope.username = $cookies.get("username");
		$scope.addWorkOrder = function()	{
			$http({
			    method : "POST",
			    url : "/jobscheduler/workorder",
			    data : angular.toJson($scope.workorder),
			    headers : {
			    	'Content-Type': 'application/json',
			        'Accept' : 'application/json'
			    }
			})
			.then( handleSuccess, handleError );			
 
	        function handleSuccess(response) {
        				$location.path("/viewworkorder"); 	    				
	        }
	 
	        function handleError(response) {
				angular.element(document.querySelector("#divstatus")).css("visibility", "visible");
				angular.element(document.querySelector("#divstatus")).addClass("alert-danger");
				$scope.statusmsg="Error: " + response.data.message;        	
	        }			
		};
	}
);

jobSchedulerApp.controller("myScheduleController", 
	function ($scope,$location,$http,$cookies,jwtHelper, $rootScope)	{
	
	$rootScope.authenticated = true;
	
		$rootScope.username = $cookies.get("username");

		$http({
		    	method : "GET",
		    	url : "jobscheduler/myschedule/"+$rootScope.username
		})
		.then( handleSuccess, handleError );			
	
        function handleSuccess(response) {        	
        	$scope.records = response.data;
        }
 
        function handleError(response) {
			angular.element(document.querySelector("#divstatus")).css("visibility", "visible");
			angular.element(document.querySelector("#divstatus")).addClass("alert-danger");
			$scope.statusmsg="Error: " + response.data.message;        	
        }				
	}
);

jobSchedulerApp.controller("viewWorkOrderController", 
	function ($scope,$location,$http,$cookies,jwtHelper,$rootScope)	{
		
	$rootScope.authenticated = true;
		$rootScope.username = $cookies.get("username");
		
  
		
		$scope.testAlgorithm = function() {
			
			
			$http({
			    method : "GET",
			    url : "/jobscheduler/testAlgo"
			})
			.then( handleSuccess, handleError );			
 
	        function handleSuccess(response) {
	        	
	        	alert('Job Scheduling completed!!!');	
//	        	angular.element(document.querySelector("#divstatus")).css("visibility", "visible");
//				angular.element(document.querySelector("#divstatus")).addClass("alert-danger");
//				$scope.statusmsg="Job Scheduling completed!!!"; 	        	
				 window.location.href = '/viewworkorder'; 
				 window.location.reload(true) ;	        	
	        	
	        }
	 
	        function handleError(response) {
	        	
	        	alert(JSON.stringify(response.data));
	        	
				angular.element(document.querySelector("#divstatus")).css("visibility", "visible");
				angular.element(document.querySelector("#divstatus")).addClass("alert-danger");
				
				$scope.statusmsg="Error: " + JSON.stringify(response.data); //response.data.message;        	
	        }				
		}		    
	    		
		
		
		$http({
	    	method : "GET",
	    	url : "jobscheduler/vieworders"
		})
		.then( handleSuccess, handleError );			
	
	    function handleSuccess(response) {        	
	    	$scope.records = response.data;
	    }
	
	    function handleError(response) {
			angular.element(document.querySelector("#divstatus")).css("visibility", "visible");
			angular.element(document.querySelector("#divstatus")).addClass("alert-danger");
			$scope.statusmsg="Error: " + response.data.message;        	
	    }				
	    
	  
	    
	}
);
