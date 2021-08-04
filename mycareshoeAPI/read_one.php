<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
  
// include database and object files
require_once 'sensors.php';
require_once 'db_connect.php';
  
// get database connection
$database = new DbConnect();
$db = $database->getConnection();
  
// prepare sensor reading object
$sensors = new Sensors($db);

// read the details of sensors reading to be edited
$sensors->readOne();
  
if($sensors->patient_number!=null){
	
    // create array
    $sensors_reading_arr = array(
        "reading_id" =>  $sensors->reading_id,
        "S1" => $sensors->S1,
        "S2" => $sensors->S2,
		"S3" => $sensors->S3,
		"S4" => $sensors->S4,
		"S5" => $sensors->S5,
		"S6" => $sensors->S6,
		"S7" => $sensors->S7,
		"S8" => $sensors->S8,
		"S9" => $sensors->S9,
		"S10" => $sensors->S10,
		"S11" => $sensors->S11,
		"S12" => $sensors->S12,
		"S13" => $sensors->S13,
		"date" => $sensors->date,
		"patient_number" => $sensors->patient_number
    );
  
    // set response code - 200 OK
    http_response_code(200);
  
    // make it json format
    echo json_encode($sensors_reading_arr);
}
  
else{
    // set response code - 404 Not found
    http_response_code(404);
  
    // tell the user sensors does not exist
    echo json_encode(array("message" => "Sensors reading does not exist."));
}
?>