<?php
ini_set('display_errors','on');
error_reporting(E_ALL);
class DBOperation{
	private $con;

	function __construct(){
		require_once dirname(__FILE__).'/DBConnect.php';

		$db = new DBConnect();

		$this->con = $db->connect();
	}


	/* creer les fonctions qui seront utilisées */

	function createUser($userName, $mailAdress, $password, $age, $Gender, $fonction){
		$req = $this->con->prepare("INSERT INTO users(userName, mailAdress, password, age, Gender, fonction) VALUES (?,?,?,?,?,?)");
		$req->bind_param("ssssss", $userName, $mailAdress, $password, $age, $Gender, $fonction);
		if($req->execute())
			return true;
		return false;
	}
	function createStore($mailAdress, $storeName , $storePhone, $StoreAdress, $storeCodePostal, $storeLocality, $storeLongitude, $storeLatitude, $storeDescription, $storeCategory, $storeOpening, $storeClosing,$image_data,$image_tag){
		$req = $this->con->prepare("INSERT INTO stores(mailAdress, storeName , storePhone, StoreAdress, storeCodePostal, storeLocality, storeLongitude, storeLatitude, storeDescription, storeCategory, storeOpening, storeClosing) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
		$req->bind_param("ssssssssssss", $mailAdress, $storeName, $storePhone, $StoreAdress, $storeCodePostal, $storeLocality, $storeLongitude, $storeLatitude, $storeDescription, $storeCategory, $storeOpening, $storeClosing);
		if($req->execute()){
			$query=$this->con->prepare("SELECT max(id) from stores");
			$query->execute();
			$query->bind_result($id);
			$query->fetch();
			$photo = $this->uploadImage($image_data, $image_tag,"stores",$id);
			if($photo){
				return true;
			}else{
				$query=$this->con->prepare("DELETE FROM stores WHERE id='".$id."';");
				$query->execute();
				return false;
			}
		}
		return false;
	}
	function createItem($idStore, $articleName, $articleDescription, $price, $image_data, $image_tag){
		$req = $this->con->prepare("INSERT INTO items(idStore, articleName, articleDescription, price) VALUES (?,?,?,?)");
		$req->bind_param("ssss", $idStore, $articleName, $articleDescription, $price);
		if($req->execute()){
			$query=$this->con->prepare("SELECT max(id) from items");
			$query->execute();
			$query->bind_result($id);
			$query->fetch();
			$photo = $this->uploadImage($image_data, $image_tag,"items",$id);
			if($photo){
				return true;
			}else{
				$query=$this->con->prepare("DELETE FROM items WHERE id='".$id."';");
				$query->execute();
				return false;
			}
		}
		return false;
	}
	
	function getUsersList(){
		$req = $this->con->prepare("SELECT userName, mailAdress, password, age, Gender, fonction FROM users");
		$req->execute();
		$req->bind_result($userName, $mailAdress, $password, $age, $Gender, $fonction);
		$users = array();

		while($req->fetch()){
			$user = array();
			$user['userName']=$userName;
			$user['mailAdress']=$mailAdress;
			$user['password']=$password;
			$user['age']=$age;
			$user['Gender']=$Gender;
			$user['fonction']=$fonction;

			array_push($users, $user);
		}
		return $users;
	}

	function getStoreList($mailAddress=""){
		if ($mailAddress!=""){
			$req = $this->con->prepare("SELECT * FROM stores WHERE mailAdress='".$mailAddress."'");
		}
		else{
			$req = $this->con->prepare("SELECT * FROM stores");
		}
		$req->execute();
		$req->bind_result($id,$mailAdress, $storeName , $storePhone, $storeAddress, $storeCodePostal, $storeLocality, $storeLongitude, $storeLatitude, $storeDescription, $storeCategory, $storeOpening, $storeClosing);
		$stores = array();

		while($req->fetch()){
			$store = array();
			$store['storeId']=$id;
			$store['storeName']=$storeName;
			$store['mailAdress']=$mailAdress;
			$store['storePhone']=$storePhone;
			$store['storeAddress']=$storeAddress;
			$store['storeCodePostal']=$storeCodePostal;
			$store['storeLocality']=$storeLocality;
			$store['storeLongitude']=$storeLongitude;
			$store['storeLatitude']=$storeLatitude;
			$store['storeDescription']=$storeDescription;
			$store['storeCategory']=$storeCategory;
			$store['storeOpening']=$storeOpening;
			$store['storeClosing']=$storeClosing;
			$store['storeImageData']=$this->getImage($id,"stores");

			array_push($stores, $store);
		}
		return $stores;
	}

	function getStoreList2($storeCate, $storeCle){

		if ($storeCle!=""){
			$req = $this->con->prepare("SELECT * FROM stores where storeCategory='".$storeCate."' and storeName LIKE '%".$storeCle."%'");
		}else{
			$req = $this->con->prepare("SELECT * FROM stores where storeCategory='".$storeCate."'");
		}
		
		$req->execute();
		$req->bind_result($id,$mailAdress, $storeName , $storePhone, $storeAddress, $storeCodePostal, $storeLocality, $storeLongitude, $storeLatitude, $storeDescription, $storeCategory, $storeOpening, $storeClosing);
		$stores = array();

		while($req->fetch()){
			$store = array();
			$store['storeId']=$id;
			$store['storeName']=$storeName;
			$store['mailAdress']=$mailAdress;
			$store['storePhone']=$storePhone;
			$store['storeAddress']=$storeAddress;
			$store['storeCodePostal']=$storeCodePostal;
			$store['storeLocality']=$storeLocality;
			$store['storeLongitude']=$storeLongitude;
			$store['storeLatitude']=$storeLatitude;
			$store['storeDescription']=$storeDescription;
			$store['storeCategory']=$storeCategory;
			$store['storeOpening']=$storeOpening;
			$store['storeClosing']=$storeClosing;
			$store['storeImageData']=$this->getImage($id,"stores");

			array_push($stores, $store);
		}
		return $stores;
	}
	function getItemList($storeId=""){
		if ($storeId!=""){
			$req = $this->con->prepare("SELECT items.id, items.idStore, items.price, items.articleDescription, items.articleName, stores.storeLocality, stores.storeCategory FROM items, stores WHERE items.idStore='".$storeId."' AND items.idStore=stores.id");
		}
		else{
			$req = $this->con->prepare("SELECT items.id, items.idStore, items.price, items.articleDescription, items.articleName, stores.storeLocality, stores.storeCategory FROM items, stores WHERE items.idStore=stores.id");
		}
		$req->execute();
		$req->bind_result($id,$storeId, $price , $articleDescription, $articleName, $storeLocality, $storeCategory);
		$items = array();

		while($req->fetch()){
			$item = array();
			$item['id']=$id;
			$item['storeId']=$storeId;
			$item['price']=$price;
			$item['articleDescription']=$articleDescription;
			$item['articleName']=$articleName;
			$item['itemLocality']=$storeLocality;
			$item['itemCategory']=$storeCategory;
			$item['itemImageData']=$this->getImage($id,"items");

			array_push($items, $item);
		}
		return $items;
	}

	function getUserByMail($mailAdress){
		$req = $this->con->prepare("SELECT userName, mailAdress, password, age, Gender, fonction, regId FROM users WHERE mailAdress = ?");
		$req->bind_param("s", $mailAdress);
		$req->execute();
		$req->store_result();
		$req->bind_result($userName, $mailAdress, $password, $age, $Gender, $fonction, $regId);
		$user = array();
		if($req->num_rows ==0)
			return null;
		else{
			while($req->fetch()){
			$user['userName']=$userName;
			$user['mailAdress']=$mailAdress;
			$user['password']=$password;
			$user['age']=$age;
			$user['Gender']=$Gender;
			$user['fonction']=$fonction;
			$user['regId']=$regId;
			}
			return $user;
		}
		
	}

	function updateRegId($mailAdress, $regId){
		$req = $this->con->prepare("UPDATE users SET regId = ? WHERE mailAdress = ?");
		$req->bind_param("ss", $regId, $mailAdress);
		if($req->execute())
			return true;
		return false;
	}
	
	function verifyUserExistance($mailAdress){
		//$query = "SELECT * from users WHERE mailAdress = ".$mailAdress;
		$req = $this->con->prepare("SELECT * from users WHERE mailAdress = ?");
		$req->bind_param("s", $mailAdress);
		$req->execute();
		$req->store_result();
		if($req->num_rows ==0)
			return false;
		return true;
	}

	function updateUser($userName, $mailAdress, $password, $age, $Gender, $fonction){
		$req = $this->con->prepare("UPDATE users SET userName = ?, password = ?, age = ?, Gender = ?, fonction = ? WHERE mailAdress = ?");
		$req->bind_param("ssssss", $userName, $password, $age, $Gender, $fonction, $mailAdress);
		if($req->execute())
			return true;
		return false;
	}

	function uploadImage($ImageData, $ImageName,$storeOrItem,$id){
		// $ImageId =0;
		// $oldImageId = $this->con->prepare("SELECT id FROM stores ORDER BY id ASC");
		// $oldImageId->execute();
		// $oldImageId->store_result();
		// if ($oldImageId->num_rows == 0){
		//  	$ImageId = 1;
		// }else{
		// 	$ImageId = $oldImageId->num_rows;
		// }
		$ImagePath = '../images/'.$storeOrItem.'/'.$id;
		$Image = array();
		$image['filePath'] = $ImagePath;
		$image['id']= $id;

		if (file_put_contents($ImagePath, base64_decode($ImageData))!== false)
			return true;
		return false;
	}

	function getImage($id,$storeOrItem){
		$ImagePath = '../images/'.$storeOrItem.'/'.$id;
		return base64_encode(file_get_contents($ImagePath));
	}
	function saveMessage($name, $phone, $sender, $message, $replyEmail,$storeId, $receiver){
		$req = $this->con->prepare("INSERT INTO messages(name, phone, sender, message, replyEmail,storeId, receiver) VALUES (?,?,?,?,?,?,?)");
		$req->bind_param("sssssss",$name, $phone, $sender, $message, $replyEmail,$storeId, $receiver);
		if($req->execute())
			return true;
		return false;
	}

	function getMessages($owner,$type){
		if ($type=="received"){
			$req = $this->con->prepare("SELECT * FROM messages WHERE receiver='".$owner."'");
		}
		if ($type=="sent"){
			$req = $this->con->prepare("SELECT * FROM messages WHERE sender='".$owner."'");
		}
		$req->execute();
		$req->bind_result($id,$name, $phone , $sender, $message, $replyEmail, $storeId, $receiver);
		$messages = array();

		while($req->fetch()){
			$store = array();
			$store['id']=$id;
			$store['name']=$name;
			$store['phone']=$phone;
			$store['sender']=$sender;
			$store['message']=$message;
			$store['replyEmail']=$replyEmail;
			$store['storeId']=$storeId;
			$store['receiver']=$receiver;

			array_push($messages, $store);
		}
		return $messages;
	}

}

?>
 
