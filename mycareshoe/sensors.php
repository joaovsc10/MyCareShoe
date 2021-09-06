<?php


    include_once 'db_connect.php';
	
	
class Sensors{
	
	
  
//https://codeofaninja.com/2017/02/create-simple-rest-api-in-php.html


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
	public $S14;
    public $S15;
    public $S16;
    public $S17;
    public $S18;
    public $S19;
	public $S20;
	public $S21;
	public $S22;
	public $S23;
	public $S24;
	public $S25;
	public $S26;
	public $T1;
	public $T2;
	public $H1;
	public $H2;
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
		
		
		// query to insert reading
		$query = "INSERT INTO " . $this->table_name . " (S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,S11,S12,S13,S14,S15,S16,S17,S18,S19,S20,S21,S22,S23,S24,S25,S26,T1,T2,H1,H2,date,patient_number) VALUES (:S1, :S2, :S3, :S4, :S5, :S6,:S7,:S8,:S9,:S10,:S11,:S12,:S13,:S14,:S15,:S16,:S17,:S18,:S19,:S20,:S21,:S22,:S23,:S24,:S25,:S26,:T1,:T2,:H1,:H2,:date,:patient_number)";
	  
	 
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
		$stmt->bindParam(":S14", $this->S14);
		$stmt->bindParam(":S15", $this->S15);
		$stmt->bindParam(":S16", $this->S16);
		$stmt->bindParam(":S17", $this->S17);
		$stmt->bindParam(":S18", $this->S18);
		$stmt->bindParam(":S19", $this->S19);
		$stmt->bindParam(":S20", $this->S20);
		$stmt->bindParam(":S21", $this->S21);
		$stmt->bindParam(":S22", $this->S22);
		$stmt->bindParam(":S23", $this->S23);
		$stmt->bindParam(":S24", $this->S24);
		$stmt->bindParam(":S25", $this->S25);
		$stmt->bindParam(":S26", $this->S26);
		$stmt->bindParam(":T1", $this->T1);
		$stmt->bindParam(":T2", $this->T2);
		$stmt->bindParam(":H1", $this->H1);
		$stmt->bindParam(":H2", $this->H2);
		$stmt->bindParam(":date", $this->date);
		$stmt->bindParam(":patient_number", $this->patient_number);
	

		// execute query
    if($stmt->execute()){

        return true;
    }
  
    return false;
		  
}

// used when filling up the update sensors reading form
function readOne($data){
  
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
    $stmt->bindParam(":reading_id", $data['reading_id']);
  
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
	$this->S14 = $row['S14'];
    $this->S15 = $row['S15'];
    $this->S16 = $row['S16'];
    $this->S17 = $row['S17'];
    $this->S18 = $row['S18'];
	$this->S19 = $row['S19'];
	$this->S20 = $row['S20'];
	$this->S21 = $row['S21'];
	$this->S22 = $row['S22'];
	$this->S23 = $row['S23'];
	$this->S24 = $row['S24'];
	$this->S25 = $row['S25'];
	$this->S26 = $row['S26'];
	$this->T1 = $row['T1'];
	$this->T2 = $row['T2'];
	$this->H1 = $row['H1'];
	$this->H2 = $row['H2'];
	$this->date = $row['date'];
	$this->patient_number = $row['patient_number'];
	}
}

function searchByStartEndDate($data){
  

	if($data['start_date']!=$data['end_date'])
	{
    $query = "SELECT
                *
            FROM
                " . $this->table_name . "
            WHERE date 
			
			BETWEEN
			
                :start_date
			AND
				:end_date
	";
	
	$startEndDifferent=true;
	
	
	}
	else{
		
		$query = "SELECT
                *
            FROM
                " . $this->table_name . " s
            WHERE 
			
			DATE(s.date)= :start_date
	";
	$startEndDifferent=false;
		
	}
	 $stmt = $this->conn->prepare( $query );
	 
	if($startEndDifferent)
		$stmt->bindParam(":end_date", $data['end_date']);
	
	$stmt->bindParam(":start_date", $data['start_date']);
  
    // prepare query statement
   
	
    // execute query
    if($stmt->execute()){
	
		return $stmt;
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
    $stmt->bindParam(":patient_number", $keywords['patient_number']);
  
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
    $stmt->bindParam(":patient_number", $keywords['patient_number']);
	$stmt->bindParam(":date", $keywords['day']);
	
	
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
    $stmt->bindParam(":patient_number", $keywords['patient_number']);
	$stmt->bindParam(":date", $keywords['day']);
	$stmt->bindParam(":hour", $keywords['hour']);
	
	
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
    $stmt->bindParam(":patient_number", $keywords['patient_number']);
	$stmt->bindParam(":month", $keywords['month']);
	
	
    // execute query
    $stmt->execute();
  
    return $stmt;
}
}
?>