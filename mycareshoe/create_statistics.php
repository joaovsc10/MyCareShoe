<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
require_once 'statistics.php';
require_once 'db_connect.php';

$emptyFound=FALSE;
  
$database = new DbConnect();
$db = $database->getConnection();
  
$statistics = new Statistics($db);
  
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
    // set statistics property values
    $statistics->cadence = $queries["cadence"];
    $statistics->balance = $queries["balance"];
	$statistics->steps = $queries["steps"];
	$statistics->date = $queries["date"];
	$statistics->patient_number = $queries["patient_number"];
  
    // create the statistics
    if($statistics->create()){
  

        // set response code - 201 created
        http_response_code(201);
  
        // tell the user
        echo json_encode(array("success" => "1","message" => "Statistics record was created.", "statistics_id" => $db->lastInsertId() , "date" => $statistics->date));

    }
  
    // if unable to create the statistics, tell the user
    else{
  
        // set response code - 503 service unavailable
        http_response_code(503);
  
        // tell the user
        echo json_encode(array("success" => "0","message" => "Unable to create statistics reading."));
    }
}
  
// tell the user data is incomplete
else{
  
    // set response code - 400 bad request
    http_response_code(400);
  
    // tell the user
    echo json_encode(array("success" => "0", "message" => "Unable to create statistics record. Data is incomplete."));
}
?>