<?php

/* creer par sakhi idris */

class DBConnect{

	private $con;

	function __construct(){

	}

	function connect(){
		include_once dirname(__FILE__).'/DB.php';
		$this->con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
		if (mysqli_connect_error()){
			echo "une erreure s'est produite lors de la connexion: ".mysqli_connect_error();
		}
		return $this->con;
	}
}

?>