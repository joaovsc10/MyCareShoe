<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
  
// database connection will be here
require_once 'sensors.php';
require_once 'db_connect.php';

  
  
  // instantiate database and product object
$database = new DbConnect();
$db = $database->getConnection();

// initialize object
$sensors = new Sensors($db);


// query sensors
$result = $sensors->read();
$num = $result->rowCount();
  
// check if more than 0 record found
if($num>0){
  
    // sensors array
    $sensors_arr=array();
    $sensors_arr["readings"]=array();
  

    while ($row = $result->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
  
        $sensors_item=array(
            "S1" => $S1,
            "S2" => $S2,
			"S3" => $S3,
			"S4" => $S4,
			"S5" => $S5,
			"S6" => $S6,
			"S7" => $S7,
			"S8" => $S8,
			"S9" => $S9,
			"S10" => $S10,
			"S11" => $S11,
			"S12" => $S12,
			"S13" => $S13,
			"date" => $date,
			"patient_number" => $patient_number
        );
  
        array_push($sensors_arr["readings"], $sensors_item);
    }
  
    // set response code - 200 OK
    http_response_code(200);
  
    // show sensors data in json format
    echo json_encode($sensors_arr);
}
  
else{
  
    // set response code - 404 Not found
    http_response_code(404);
  
    // tell the user no sensor data was found
    echo json_encode(
        array("message" => "No Sensor data found.")
    );
}


