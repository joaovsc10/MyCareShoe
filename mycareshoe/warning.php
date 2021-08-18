<?php


    include_once 'db_connect.php';
	
	
class Warning{
	
  
    // database connection and table name
    private $db;
    private $table_name = "warnings";
  
    // object properties
    public $warning_id;
    public $patient_number;
    public $reading_id;
	public $warning_date;
  
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }
		// read warning's data
	function readAll($params){
  
    // select all query
	  $query = "SELECT
                *
            FROM
                " . $this->table_name . " w
            WHERE
                w.patient_number = :patient_number

            ";
	   
	// prepare query statement
	$stmt = $this->conn->prepare($query);
	
	$stmt->bindParam(":patient_number", $params['patient_number']);
  
        // execute query
    $stmt->execute();
	
    return $stmt;
	}
	
		// create reading
	function create(){
		
		// query to insert reading
		$query = "INSERT INTO " . $this->table_name . " (reading_id,patient_number,warning_date) VALUES (:reading_id, :patient_number, :warning_date)";
	  
	 
	  // prepare query
      $stmt = $this->conn->prepare($query);


		// bind values
		$stmt->bindParam(":reading_id", $this->reading_id);
		$stmt->bindParam(":patient_number", $this->patient_number);
		$stmt->bindParam(":warning_date", $this->warning_date);
	

		// execute query
    if($stmt->execute()){
        return true;
    }
  
    return false;
		  
}
}
?>