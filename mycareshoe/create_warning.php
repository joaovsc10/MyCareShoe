<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
require_once 'warning.php';
require_once 'db_connect.php';

$emptyFound=FALSE;
  
$database = new DbConnect();
$db = $database->getConnection();
  
$warning = new Warning($db);
  
  
  // get posted data
$queries = array();
parse_str($_SERVER['QUERY_STRING'], $queries);
  

// make sure data is not empty
foreach($queries as $element) {

	if(empty($element))
		$emptyFound=TRUE;
	
}
if(!$emptyFound)
{
    // set warning's property values
    $warning->reading_id = $queries["reading_id"];
	$warning->patient_number = $queries["patient_number"];
	$warning->warning_date = $queries["warning_date"];
  
    // create the warning
    if($warning->create()){
  
        // set response code - 201 created
        http_response_code(201);
  
        // tell the user
        echo json_encode(array("message" => "Warning reading was created."));
    }
  
    // if unable to create the warning, tell the user
    else{
  
        // set response code - 503 service unavailable
        http_response_code(503);
  
        // tell the user
        echo json_encode(array("message" => "Unable to create warning reading."));
    }
}
  
// tell the user data is incomplete
else{
  
    // set response code - 400 bad request
    http_response_code(400);
  
    // tell the user
    echo json_encode(array("message" => "Unable to create warning reading. Data is incomplete."));
}
?>