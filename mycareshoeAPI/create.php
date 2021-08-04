<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
require_once 'sensors.php';
require_once 'db_connect.php';

$emptyFound=FALSE;
  
$database = new DbConnect();
$db = $database->getConnection();
  
$sensors = new Sensors($db);
  
// get posted data
$data = json_decode(file_get_contents("php://input"));
  
// make sure data is not empty
foreach($data as $element) {
	
	if(empty($element))
		$emptyFound=TRUE;
	
}
if(!$emptyFound)
{
    // set sensors property values
    $sensors->S1 = $data->S1;
    $sensors->S2 = $data->S2;
	$sensors->S3 = $data->S3;
	$sensors->S4 = $data->S4;
	$sensors->S5 = $data->S5;
	$sensors->S6 = $data->S6;
	$sensors->S7 = $data->S7;
	$sensors->S8 = $data->S8;
	$sensors->S9 = $data->S9;
	$sensors->S10 = $data->S10;
	$sensors->S11 = $data->S11;
	$sensors->S12 = $data->S12;
	$sensors->S13 = $data->S13;
	$sensors->date = $data->date;
	$sensors->patient_number = $data->patient_number;
  
    // create the sensors
    if($sensors->create()){
  
        // set response code - 201 created
        http_response_code(201);
  
        // tell the user
        echo json_encode(array("message" => "Sensors reading was created."));
    }
  
    // if unable to create the sensors, tell the user
    else{
  
        // set response code - 503 service unavailable
        http_response_code(503);
  
        // tell the user
        echo json_encode(array("message" => "Unable to create sensors reading."));
    }
}
  
// tell the user data is incomplete
else{
  
    // set response code - 400 bad request
    http_response_code(400);
  
    // tell the user
    echo json_encode(array("message" => "Unable to create sensors reading. Data is incomplete."));
}
?>