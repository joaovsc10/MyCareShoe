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
$queries = array();
parse_str($_SERVER['QUERY_STRING'], $queries);
  

// make sure data is not empty
foreach($queries as $element) {

	if(empty($element) && $element!=0){
		$emptyFound=TRUE;
	}
	
}
if(!$emptyFound)
{
    // set sensors property values
    $sensors->S1 = $queries["S1"];
    $sensors->S2 = $queries["S2"];
	$sensors->S3 = $queries["S3"];
	$sensors->S4 = $queries["S4"];
	$sensors->S5 = $queries["S5"];
	$sensors->S6 = $queries["S6"];
	$sensors->S7 = $queries["S7"];
	$sensors->S8 = $queries["S8"];
	$sensors->S9 = $queries["S9"];
	$sensors->S10 =$queries["S10"];
	$sensors->S11 = $queries["S11"];
	$sensors->S12 = $queries["S12"];
	$sensors->S13 = $queries["S13"];
	$sensors->S14 = $queries["S14"];
	$sensors->S15 = $queries["S15"];
	$sensors->S16 = $queries["S16"];
	$sensors->S17 = $queries["S17"];
	$sensors->S18 = $queries["S18"];
	$sensors->S19 = $queries["S19"];
	$sensors->S20 = $queries["S20"];
	$sensors->S21 = $queries["S21"];
	$sensors->S22 = $queries["S22"];
	$sensors->S23 = $queries["S23"];
	$sensors->S24 = $queries["S24"];
	$sensors->S25 = $queries["S25"];
	$sensors->S26 = $queries["S26"];
	$sensors->T1 = $queries["T1"];
	$sensors->T2 = $queries["T2"];
	$sensors->H1 = $queries["H1"];
	$sensors->H2 = $queries["H2"];
	$sensors->date = $queries["date"];
	$sensors->patient_number = $queries["patient_number"];
  
    // create the sensors
    if($sensors->create()){
  

        // set response code - 201 created
        http_response_code(201);
  
        // tell the user
        echo json_encode(array("success" => "1","message" => "Sensors reading was created.", "reading_id" => $db->lastInsertId() , "date" => $sensors->date));

    }
  
    // if unable to create the sensors, tell the user
    else{
  
        // set response code - 503 service unavailable
        http_response_code(503);
  
        // tell the user
        echo json_encode(array("success" => "0","message" => "Unable to create sensors reading."));
    }
}
  
// tell the user data is incomplete
else{
  
    // set response code - 400 bad request
    http_response_code(400);
  
    // tell the user
    echo json_encode(array("success" => "0", "message" => "Unable to create sensors reading. Data is incomplete."));
}
?>