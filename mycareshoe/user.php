
<?php
    
    include_once 'db_connect.php';
    
    class User{
        
        private $db;
        
        private $db_table = "user";
		
		public $user_id;
		
		public $profile_id;
		
		public $email;
		
		public $password;
		
		public $username;
		
		public $patient_number;
        
        public function __construct($db){
            $this->conn = $db;
        }
        
        public function isLoginExist($usernameEmail, $password, $isEmailSet){
			
			if ($isEmailSet==0)
				$query = "select * from ".$this->db_table." where username = '$usernameEmail' AND password = '$password' Limit 1";
			
			 elseif($isEmailSet==1)
				$query = "select * from ".$this->db_table." where email = '$usernameEmail' AND password = '$password' Limit 1";
				
            // prepare query statement
			$stmt = $this->conn->prepare($query);
  
			// execute query
			$stmt->execute();
			
			// get retrieved row
			$row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            if($stmt->rowCount() ==1){
                
				$this->user_id = $row['user_id'];
				$this->profile_id = $row['profile_id'];
				$this->email = $row['email'];
				$this->password = $row['password'];
				$this->username = $row['username'];
				$this->patient_number = $row['patient_number'];
                $this->conn = null;
               
                
                return $row;
                
            }
            
            $this->conn = null;
            
            return false;
            
        }
        
        public function isEmailUsernameExist($username, $email){
            
            $query = "select * from ".$this->db_table." where username = '$username' AND email = '$email'";
            
            // prepare query statement
			$stmt = $this->conn->prepare($query);
  
			// execute query
			$stmt->execute();
            
            if($stmt->rowCount() > 0){
                
                $this->conn = null;
                
                return true;
                
            }
               
            return false;
            
        }
        
        public function isValidEmail($email){
            return filter_var($email, FILTER_VALIDATE_EMAIL) !== false;
        }
        
        public function createNewRegisterUser($username, $password, $email){
              
            $isExisting = $this->isEmailUsernameExist($username, $email);
            
            if($isExisting){
                
                $json['success'] = 0;
                $json['message'] = "Error in registering. Probably the username/email already exists";
            }
            
            else{
                
            $isValid = $this->isValidEmail($email);
                
                if($isValid)
                {
                $query = "insert into ".$this->db_table." (username, password, email, created_at, updated_at) values ('$username', '$password', '$email', NOW(), NOW())";
                
				// prepare query statement
				$stmt = $this->conn->prepare($query);
	  
				// execute query
				$stmt->execute();
				
				//TODO
                
                if($inserted == 1){
                    
                    $json['success'] = 1;
                    $json['message'] = "Successfully registered the user";
                    
                }else{
                    
                    $json['success'] = 0;
                    $json['message'] = "Error in registering. Probably the username/email already exists";
                    
                }
                
                $this->conn = null;
                }
                else{
                    $json['success'] = 0;
                    $json['message'] = "Error in registering. Email Address is not valid";
                }
                
            }
            
            return $json;
            
        }
        
        public function loginUsers($usernameEmail, $password, $isEmailSet){
            
            $json = array();
            
            $canUserLogin = $this->isLoginExist($usernameEmail, $password, $isEmailSet);
            
            if($canUserLogin!=false){
                
                $json['success'] = 1;
                $json['message'] = "Successfully logged in";
				$json['user']=$canUserLogin;
				
				session_start();
				$_SESSION['username'] = $this->username;
				$_SESSION['id'] = $this->profile_id;
				header("location: http://localhost/tele/site/index.php");

                
            }else{
                $json['success'] = 0;
                $json['message'] = "Incorrect details";
				
            }
			return $json;
            
        }
    }
    ?>
