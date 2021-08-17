<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
  
// include database and object files
require_once 'sensors.php';
require_once 'db_connect.php';

// get database connection
$database = new DbConnect();
$db = $database->getConnection();
  
// prepare sensor reading object
$sensors = new Sensors($db);


  
 $queries = array();
parse_str($_SERVER['QUERY_STRING'], $queries);

// get keywords
$keywords=['p','d','h','m'];

foreach ($keywords as $keyword) {
	
  $queries[$keyword]=isset($queries[$keyword])? $queries[$keyword] : '';
  
}


// query sensor reading
if($queries['d']!='' && $queries['h']=='')
{
	$stmt = $sensors->searchByPatientDate($queries);
}
else{
	if($queries['h']!='')
	{
		
		$stmt = $sensors->searchByPatientHour($queries);
	}
	else
	{
		if($queries['m']!='')
			$stmt = $sensors->searchByPatientMonth($queries);
		else
			$stmt = $sensors->searchByPatient($queries);	
	}
	
}
  
$num = $stmt->rowCount();

  
// check if more than 0 record found
if($num>0){
  
    // sensor reading array
    $sensors_reading_arr=array();
    $sensors_reading_arr["records"]=array();
  
    // retrieve our table contents
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
  
        $reading_item = array(
        "reading_id" =>  $reading_id,
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
  
        array_push($sensors_reading_arr["records"], $reading_item);
    }
  
    // set response code - 200 OK
    http_response_code(200);
  
    // show sensors reading data
    echo json_encode($sensors_reading_arr);
}
  
else{
    // set response code - 404 Not found
    http_response_code(404);
  
    // tell the user no sensor readings data found
    echo json_encode(
        array("message" => "No sensor readings data found.")
    );
}
?>