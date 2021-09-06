<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
  
// include database and object files
require_once 'sensors.php';
require_once 'statistics.php';
require_once 'db_connect.php';

// get database connection
$database = new DbConnect();
$db = $database->getConnection();
  
  




  
 $queries = array();
parse_str($_SERVER['QUERY_STRING'], $queries);

if($queries['topic']=='sensorsReading')
	

	$object = new Sensors($db);
else
	if($queries['topic']=='statistics')
		$object = new Statistics($db);

// get keywords
$keywords=['patient_number','day','hour','month', 'start_date', 'end_date'];

foreach ($keywords as $keyword) {
	
  $queries[$keyword]=isset($queries[$keyword])? $queries[$keyword] : '';
  
}

if($queries['start_date']!='' && $queries['end_date']!=''){
	
	$stmt = $object->searchByStartEndDate($queries);
	
}else{
	// query sensor reading
	if($queries['day']!='' && $queries['hour']=='')
	{
		$stmt = $object->searchByPatientDate($queries);
	}
	else{
		if($queries['hour']!='')
		{
			
			$stmt = $object->searchByPatientHour($queries);
		}
		else
		{
			if($queries['month']!='')
				$stmt = $object->searchByPatientMonth($queries);
			else
				$stmt = $object->searchByPatient($queries);	
		}
		
	}
}
$num = $stmt->rowCount();

  
// check if more than 0 record found
if($num>0){
  
    // sensor reading array
    $data_array=array();
    $data_array["records"]=array();
  
    // retrieve our table contents
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
  
  if($queries['topic']=='sensorsReading')
	
        $data_item = array(
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
		"S14" => $S14,
        "S15" => $S15,
		"S16" => $S16,
		"S17" => $S17,
		"S18" => $S18,
		"S19" => $S19,
		"S20" => $S20,
		"S21" => $S21,
		"S22" => $S22,
		"S23" => $S23,
		"S24" => $S24,
		"S25" => $S25,
		"S26" => $S26,
		"T1" => $T1,
		"T2" => $T2,
		"H1" => $H1,
		"H2" => $H2,
		"date" => $date,
		"patient_number" => $patient_number
    );
	else
		if($queries['topic']=='statistics')
			
			$data_item = array(
			"statistics_id" =>  $statistics_id,
			"cadence" => $cadence,
			"steps" => $steps,
			"balance" => $balance,
			"date" => $date,
			"patient_number" => $patient_number
    );

  
    array_push($data_array["records"], $data_item);
	
	
 }
  
    // set response code - 200 OK
    http_response_code(200);
  
    // show sensors reading data
    echo json_encode($data_array);
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