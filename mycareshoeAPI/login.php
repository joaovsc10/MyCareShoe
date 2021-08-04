
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
		if(isset($queries['username'])){
			$username=$queries['username'];
		}
		
		if(isset($queries['password'])){
			$password=$queries['password'];
		}
	}else{
      if(isset($_POST['username'])){
        $username = $_POST['username'];
        
		
    }
    
    if(isset($_POST['password'])){
        
        $password = $_POST['password'];
        
    }
    
    if(isset($_POST['email'])){
        
        $email = $_POST['email'];
        
    }  }
    
    $userObject = new User($db);
	
    
    // Registration
    
    if(!empty($username) && !empty($password) && !empty($email)){
        
        $hashed_password = sha1($password);
        
        $json_registration = $userObject->createNewRegisterUser($username, $hashed_password, $email);
        
        echo json_encode($json_registration);
        
    }
    
    // Login
    
    if(!empty($username) && !empty($password) && empty($email)){
        
        $hashed_password = sha1($password);
        
        $json_array = $userObject->loginUsers($username, $hashed_password);
        
        echo json_encode($json_array);
    }
    ?>
