<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
  
// database connection will be here
require_once 'warnings.php';
require_once 'db_connect.php';

  
  
  // instantiate database and product object
$database = new DbConnect();
$db = $database->getConnection();

// initialize object
$warnings = new Warning($db);


// query warnings
$result = $warnings->read();
$num = $result->rowCount();
  
// check if more than 0 record found
if($num>0){
  
    // warnings array
    $warnings_arr=array();
    $warnings_arr["readings"]=array();
  

    while ($row = $result->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
  
        $warnings_item=array(      
			"patient_number" => $patient_number,
			"reading_id" => $reading_id
        );
  
        array_push($warnings_arr["readings"], $warnings_item);
    }
  
    // set response code - 200 OK
    http_response_code(200);
  
    // show warnings data in json format
    echo json_encode($warnings_arr);
}
  
else{
  
    // set response code - 404 Not found
    http_response_code(404);
  
    // tell the user no sensor data was found
    echo json_encode(
        array("message" => "No Sensor data found.")
    );
}

