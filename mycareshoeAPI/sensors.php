<?php


    include_once 'db_connect.php';
	
	
class Sensors{
	
	
  
    // database connection and table name
    private $db;
    private $table_name = "sensor_reading";
  
    // object properties
    public $reading_id;
    public $S1;
    public $S2;
    public $S3;
    public $S4;
    public $S5;
    public $S6;
	public $S7;
	public $S8;
	public $S9;
	public $S10;
	public $S11;
	public $S12;
	public $S13;
	public $date;
	public $patient_number;
  
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }
		// read sensors data
	function read(){
  
    // select all query
	  $query = "SELECT * FROM " . $this->table_name . "";
	   
	// prepare query statement
	$stmt = $this->conn->prepare($query);
  
    // execute query
    $stmt->execute();
	
    return $stmt;
}

	// create reading
	function create(){
		
		$array = array("S1","S2","S3","S4", "S5", "S6", "S7", "S8", "S9", "S10", "S11", "S12", "S13", "date", "patient_number" );
	  
		// query to insert reading
		$query = "INSERT INTO " . $this->table_name . " (S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,S11,S12,S13,date,patient_number) VALUES (:S1, :S2, :S3, :S4, :S5, :S6,:S7,:S8,:S9,:S10,:S11,:S12,:S13,:date,:patient_number)";
	  
	 
	  // prepare query
      $stmt = $this->conn->prepare($query);


		// bind values
		$stmt->bindParam(":S1", $this->S1);
		$stmt->bindParam(":S2", $this->S2);
		$stmt->bindParam(":S3", $this->S3);
		$stmt->bindParam(":S4", $this->S4);
		$stmt->bindParam(":S5", $this->S5);
		$stmt->bindParam(":S6", $this->S6);
		$stmt->bindParam(":S7", $this->S7);
		$stmt->bindParam(":S8", $this->S8);
		$stmt->bindParam(":S9", $this->S9);
		$stmt->bindParam(":S10", $this->S10);
		$stmt->bindParam(":S11", $this->S11);
		$stmt->bindParam(":S12", $this->S12);
		$stmt->bindParam(":S13", $this->S13);
		$stmt->bindParam(":date", $this->date);
		$stmt->bindParam(":patient_number", $this->patient_number);
	

		// execute query
    if($stmt->execute()){
        return true;
    }
  
    return false;
		  
}

// used when filling up the update sensors reading form
function readOne(){
  
    // query to read single record
    $query = "SELECT
                *
            FROM
                " . $this->table_name . "
            WHERE
                reading_id = :reading_id
";
  
    // prepare query statement
    $stmt = $this->conn->prepare( $query );
  
    // bind id of product to be updated
    $stmt->bindParam(":reading_id", $this->reading_id);
  
    // execute query
    if($stmt->execute()){
	
		$num = $stmt->rowCount();
	}
  
    // get retrieved row
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
  
    $num = $stmt->rowCount();
  
  
	if($num>0){
    // set values to object properties
	$this->reading_id = $row['reading_id'];
    $this->S1 = $row['S1'];
    $this->S2 = $row['S2'];
    $this->S3 = $row['S3'];
    $this->S4 = $row['S4'];
    $this->S5 = $row['S5'];
	$this->S6 = $row['S6'];
	$this->S7 = $row['S7'];
	$this->S8 = $row['S8'];
	$this->S9 = $row['S9'];
	$this->S10 = $row['S10'];
	$this->S11 = $row['S11'];
	$this->S12 = $row['S12'];
	$this->S13 = $row['S13'];
	$this->date = $row['date'];
	$this->patient_number = $row['patient_number'];
	}
}

// search sensors record by patient
function searchByPatient($keywords){
  
    // select all query
    $query = "SELECT
                *
            FROM
                " . $this->table_name . " p
            WHERE
                p.patient_number = :patient_number
            ORDER BY
                p.date DESC";
  
    // prepare query statement
    $stmt = $this->conn->prepare($query);
  
    // bind
    $stmt->bindParam(":patient_number", $keywords['p']);
  
    // execute query
    $stmt->execute();
  
    return $stmt;
}


function searchByPatientDate($keywords){
  
    // select all query
    $query = "SELECT
                *
            FROM
                " . $this->table_name . " p
            WHERE
                p.patient_number = :patient_number
			AND			
				p.date = :date
            ";
  
  
	
    // prepare query statement
    $stmt = $this->conn->prepare($query);

    // bind
    $stmt->bindParam(":patient_number", $keywords['p']);
	$stmt->bindParam(":date", $keywords['d']);
	
	
    // execute query
    $stmt->execute();
  
    return $stmt;
}


function searchByPatientHour($keywords){
  
    // select all query
    $query = "SELECT
                *
            FROM
                " . $this->table_name . " p
            WHERE
                p.patient_number = :patient_number
			AND			
				DATE(p.date) = DATE(:date)
			AND 
				HOUR(p.date) = HOUR(:hour)
            ";
  
 
	
    // prepare query statement
    $stmt = $this->conn->prepare($query);

    // bind
    $stmt->bindParam(":patient_number", $keywords['p']);
	$stmt->bindParam(":date", $keywords['d']);
	$stmt->bindParam(":hour", $keywords['h']);
	
	
    // execute query
    $stmt->execute();
  
    return $stmt;
}

function searchByPatientMonth($keywords){
  
    // select all query
    $query = "SELECT
                *
            FROM
                " . $this->table_name . " p
            WHERE
                p.patient_number = :patient_number
			AND 
				MONTH(p.date) = MONTH(:month)
            ";
  
 
	
    // prepare query statement
    $stmt = $this->conn->prepare($query);

    // bind
    $stmt->bindParam(":patient_number", $keywords['p']);
	$stmt->bindParam(":month", $keywords['m']);
	
	
    // execute query
    $stmt->execute();
  
    return $stmt;
}
}
?>