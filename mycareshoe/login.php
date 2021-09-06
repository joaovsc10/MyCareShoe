
<?php
    // required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

    require_once 'user.php';
	require_once 'db_connect.php';

  
	// instantiate database and product object
	$database = new DbConnect();
	$db = $database->getConnection();
	
	 $queries = array();
	parse_str($_SERVER['QUERY_STRING'], $queries);
    
    $username = "";
    
    $password = "";
    
    $email = "";
	

	if(count($queries)>0)
	{
		//
		
		
		if(isset($queries['username'])){
			$username=$queries['username'];
		}
		
		if(isset($queries['password'])){
			$password=$queries['password'];
		}
		
		if(isset($queries['email'])){

			$email=$queries['email'];
		}
	}else{
      if(isset($_POST['usernameEmail'])){
		    
		
		  if(filter_var($_POST['usernameEmail'], FILTER_VALIDATE_EMAIL)){
	
				$email = $_POST['usernameEmail'];
				
		  }else{
				$username = $_POST['usernameEmail'];
		  }
    }
    
    if(isset($_POST['password'])){
        
        $password = $_POST['password'];
        
    }
    
      }
    
    $userObject = new User($db);
	
    
    // Registration
    
  /*  if(!empty($username) && !empty($password) && !empty($email)){
        
        $hashed_password = sha1($password);
        
        $json_registration = $userObject->createNewRegisterUser($username, $hashed_password, $email);
        
        echo json_encode($json_registration);
        
    } */


    // Login
	
	if(!empty($password)){
			
		$hashed_password = sha1($password);
		 
		if(!empty($username))
		
			$json_array = $userObject->loginUsers($username, $hashed_password, 0);
		
		if(!empty($email))
			
			$json_array = $userObject->loginUsers($email, $hashed_password,1);
		
		if($json_array['success']==0){
			
			$_SESSION['errors'] = array("Your username or password was incorrect.");
			header("Location:http://localhost/tele/site/log_in.php");
		}
 
        echo json_encode($json_array);
    }
    ?>
