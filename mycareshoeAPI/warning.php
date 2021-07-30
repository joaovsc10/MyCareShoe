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
  
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }
		// read warning's data
	function read($params){
  
    // select all query
	  $query = "SELECT
                *
            FROM
                " . $this->table_name . " w
            WHERE
                w.patient_number = :patient_number
			AND
				w.reading_id= :reading_id
            ";
	   
	// prepare query statement
	$stmt = $this->conn->prepare($query);
	
	$stmt->bindParam(":patient_number", $params['patient_number']);
	$stmt->bindParam(":reading_id", $params['reading_id']);
  
        // execute query
    $stmt->execute();
	
    return $stmt;
	}
	
		// create reading
	function create(){
		
		// query to insert reading
		$query = "INSERT INTO " . $this->table_name . " (reading_id,patient_number) VALUES (:reading_id, :patient_number)";
	  
	 
	  // prepare query
      $stmt = $this->conn->prepare($query);


		// bind values
		$stmt->bindParam(":reading_id", $this->reading_id);
		$stmt->bindParam(":patient_number", $this->patient_number);
	

		// execute query
    if($stmt->execute()){
        return true;
    }
  
    return false;
		  
}
}
?>