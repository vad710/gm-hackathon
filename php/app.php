<?php

class GM {
	private $_bearer = null;
	private $_vehicles = null;
	private $_lat = null;
	private $_long = null;
	
	public function __construct($lat, $long) {
		$auth = new GMAuth();
		$this->_bearer = $auth->getBearer();
		$this->_lat = $lat;
		$this->_long = $long;
		
		if(empty($this->_bearer)) {
			die('bearer fail');
		} else {
			$this->_retrieveVehicles();
			$this->sendDestination();
		}
	}
	
	private function _retrieveVehicles() {
		$credentials = sprintf('Authorization: '.$this->_bearer['type'].' '.$this->_bearer['token']);
		$options = array(
				'http' => array(
						'method' => 'GET',
						'header'=> $credentials."\r\nAccept: application/json")
		);
		$context = stream_context_create($options);
		$response = file_get_contents("https://developer.gm.com/api/v1/account/vehicles?offset=0&size=2", false, $context);
		$vehicles = json_decode($response, true);
		if(is_array($vehicles) && is_array($vehicles['vehicles']) && is_array($vehicles['vehicles']['vehicle']) && count($vehicles['vehicles']['vehicle']) > 0) {
			$this->_vehicles = $vehicles['vehicles']['vehicle'];
		}
	}
	
	public function sendDestination($vehicle_id=null) {
		if(empty($vehicle_id) || !isset($this->_vehicles[$vehicle_id])) {
			$vehicle_id = 0;
		}
		if(isset($this->_vehicles[$vehicle_id])) {
			$vin = $this->_vehicles[$vehicle_id]['vin'];
			
			$credentials = sprintf('Authorization: '.$this->_bearer['type'].' '.$this->_bearer['token']);
			$ch = curl_init();
			curl_setopt($ch,CURLOPT_HTTPHEADER, array(
				"Accept: application/json\r\n".
				$credentials
			));
			curl_setopt($ch,CURLOPT_URL, "https://developer.gm.com/api/v1/account/vehicles/".$vin."/navUnit/commands/sendNavDestination");
			curl_setopt($ch,CURLOPT_POST, 1);
			curl_setopt($ch,CURLOPT_POSTFIELDS, json_encode(array('navDestination' => array(
				'destinationLocation' => array(
					'lat' => $this->_lat,
					'long' => $this->_long
				)
			))));
			$result = curl_exec($ch);
			curl_close($ch);
			/*
			
			// TESTING CASE FOR CONNECTING TO GM DEV
			
			$credentials = sprintf('Authorization: '.$this->_bearer['type'].' '.$this->_bearer['token']);
			$options = array(
				'http' => array(
					'method' => 'GET',
					'header'=> $credentials."\r\nAccept: application/json")
			);
			$context = stream_context_create($options);
			@file_put_contents("https://developer.gm.com/api/v1/account/vehicles/".$vin."/navUnit/commands/sendNavDestination", json_encode(array('navDestination' => array(
				'destinationLocation' => array(
					'lat' => $this->_lat,
					'long' => $this->_long
				)
			))), false, $context);
			*/
		}
	}
}

class GMAuth {
	private $_user = "28c220975cf9a0c1da0b58ab2";
	private $_pass = "8a09afc5d2d6f59f";
	private $_url = "https://developer.gm.com/api/v1/oauth/access_token";
	private $_bearer = null;
	
	public function __construct($user=null, $pass=null) {
		if(!empty($user)) {
			$this->_user = $user;
		}
		if(!empty($pass)) {
			$this->_pass = $pass;
		}
		$credentials = sprintf('Authorization: Basic %s', base64_encode($this->_user.':'.$this->_pass));
		$options = array(
			'http' => array(
				'method' => 'GET',
				'header'=> $credentials."\r\nAccept: application/json")
		);
		$context = stream_context_create($options);
		$response = file_get_contents($this->_url, false, $context);
		$this->_bearer = json_decode($response, true);
	}
	
	public function getBearer() {
		return array(
			'token' => $this->_bearer['access_token'],
			'type' => $this->_bearer['token_type']
		);
	}
}

$lat = $_GET['lat'];
$long = $_GET['long'];

if(!empty($lat) && !empty($long)) {
	$gm = new GM($lat, $long);	
}
