<?php
    
    include_once 'config.php';
    
    class DbConnect{
        
        private $connect;
        
        public function getConnection(){
			
			$this->conn = null;
  
        try{
            $this->conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD);
            $this->conn->exec("set names utf8");
        }catch(PDOException $exception){
            echo "Connection error: " . $exception->getMessage();
        }
		
		return $this->conn;
        }
        

    }
    ?>