<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
  
require_once 'patient.php';
require_once 'db_connect.php';

  
$database = new DbConnect();
$db = $database->getConnection();
  
$patient = new Patient($db);
  
// get posted data
$queries = array();
parse_str($_SERVER['QUERY_STRING'], $queries);
  
if(isset($queries['p']))
{
	$stmt= $patient->read($queries['p']); 
}
  
// check if more than 0 record found
if(isset($stmt)){
  
	
    // create array
    $patient_info_arr = array(
        "gender" =>  $patient->gender,
        "birth" => $patient->birth,
        "height" => $patient->height,
		"weight" => $patient->weight,
		"feet_size" => $patient->feet_size,
		"diabetes" => $patient->diabetes,
		"type_feet" => $patient->type_feet,
		"name" => $patient->name,
		"patient_number" => $patient->patient_number
    );
  
    // set response code - 200 OK
    http_response_code(200);
  
    // show patient reading data
    echo json_encode($patient_info_arr);
}
  
else{
    // set response code - 404 Not found
    http_response_code(404);
  
    // tell the user no sensor readings data found
    echo json_encode(
        array("message" => "No patient data found.")
    );
}
?>