<?php


  
// database connection will be here
require_once 'patient.php';
require_once 'db_connect.php';

  
  
  // instantiate database and product object
$database = new DbConnect();
$db = $database->getConnection();

// initialize object
$patient = new Patient($db);


$result = $patient->readAll();
$num = $result->rowCount();
  
// check if more than 0 record found
if($num>0){
	
	

    while ($row = $result->fetch(PDO::FETCH_ASSOC)){
        
		echo "<tr class='clickable-row' data-href='http://localhost/tele/site/about.php'>";

		echo "<td>" . $row["patient_number"]. "</td>";
		echo "<td>" . $row["gender"]. "</td>";
		echo "<td>" . $row["birth"]. "</td>";
		echo "<td>" . $row["name"]. "</td>";

		echo "</tr>\n";
  }
  
}
  


