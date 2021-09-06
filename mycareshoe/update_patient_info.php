<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
// include database and object files
include_once 'db_connect.php';
include_once 'patient.php';
  
// get database connection
$database = new DbConnect();
$db = $database->getConnection();
  
// prepare patient object
$patient = new Patient($db);
  
// get posted data
$data = array();
parse_str($_SERVER['QUERY_STRING'], $data);

// set patient's property values
$fields= array("gender", "height", "weight", "feet_size", "diabetes", "type_feet", "patient_number");

foreach($fields as $field)
{
	if(isset($data[$field])){
		$patient->$field = $data[$field];
	}
}

// update the info
if($patient->update($data)){
  
    // set response code - 200 ok
    http_response_code(200);
  
    // tell the user
    echo json_encode(array("message" => "Patient info was updated!"));
}
  
// if unable to update the patient info, tell the user
else{
  
    // set response code - 503 service unavailable
    http_response_code(503);
  
    // tell the user
    echo json_encode(array("message" => "Unable to update patient's info!"));
}
?>