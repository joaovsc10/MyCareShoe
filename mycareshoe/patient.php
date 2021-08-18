<?php


    include_once 'db_connect.php';
	
	
class Patient{
	
  
    // database connection and table name
    private $db;
    private $table_name = "personal_info";
  
    // object properties
    public $gender;
    public $birth;
    public $height;
    public $weight;
    public $feet_size;
    public $diabetes;
    public $type_feet;
	public $name;
	public $patient_number;
  
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }
		// read patient's data
	function read($patient_number){
  
    // select all query
	  $query = "SELECT
                *
            FROM
                " . $this->table_name . " p
            WHERE
                p.patient_number = :patient_number
            ";
	   
	// prepare query statement
	$stmt = $this->conn->prepare($query);
	
	$stmt->bindParam(":patient_number", $patient_number);
  
        // execute query
    if($stmt->execute()){
	
		$num = $stmt->rowCount();
	}
  
    // get retrieved row
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
  
    $num = $stmt->rowCount();
  
  
	if($num==1){
    // set values to object properties
	$this->gender = $row['gender'];
    $this->birth = $row['birth'];
    $this->height = $row['height'];
    $this->weight = $row['weight'];
    $this->feet_size = $row['feet_size'];
    $this->diabetes = $row['diabetes'];
	$this->type_feet = $row['type_feet'];
	$this->name = $row['name'];
	$this->patient_number = $row['patient_number'];
	
    return $stmt;
	}
}

// update the product
function update($updateData){

  $query = "UPDATE " . $this->table_name . " SET ";
  $params = array();
  
  foreach($updateData as $key=>$value){
	  
	  if(isset($key)){
		  $query .= "$key = :$key, ";
		// You're using prepared statements, right?
		$params[$key] = $value;
	  }
  }
  // Cut off last comma and append WHERE clause
$query = substr($query,0,-2)." WHERE patient_number = :patient_number";
// Store id for prepared statement


    // prepare query statement
    $stmt = $this->conn->prepare($query);
 
    // execute the query
    if($stmt->execute($params)){
        return true;
    }
  
    return false;
}
}
?>
