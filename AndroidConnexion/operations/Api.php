<?php
ini_set('display_errors','on');
error_reporting(E_ALL);

require_once '../includes/DBOperation.php';
require_once '../includes/DB.php';
require_once '../includes/firebase.php';
require_once '../includes/push.php';

// function validParam($params){
// 	$available = true;
// 	$missingparams = "";
// 	foreach ($params as $param) {
// 		if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
// 			$available = false;
// 			$missingparams = $missingparams. ": ", $param;
// 		}
// 	}
// 	if(!$available){
// 		$response = array();
// 		$response['error'] = true;
// 		$response['message'] = 'Parameters '.substr($missingparams, 1, strlen($missingparams)). ' absent';
// 		//afficher les erreurs
// 		echo json_encode($response);
// 		die();
// 	}
// }

$response = array();
//$db = new DBOperation();
//$db->createStore('adrsM', 'adidas', '0000000', 'adrs', '34000', 'loca', 0.09, 0.08, 'desc', 'cat', NULL,NULL);

//tout appel api contien un attribut apical

if(isset($_GET['apical'])){

	if ( $_GET['apical']=="createUser"){
		//valideParam(array('userName', 'mailAdress', 'password', 'age', 'Gender', 'fonction'));
		$db = new DBOperation();
		$verif = $db->verifyUserExistance($_POST['mailAdress']);
		if($verif){
			$response['error'] = true;
			$response['message'] = 'cette adress mail existe deja';
		}else{
			$result = $db->createUser($_POST['userName'], $_POST['mailAdress'], $_POST['password'], $_POST['age'], $_POST['Gender'], $_POST['fonction']);
			if($result){
				$response['error'] = false;
				$response['message'] = 'utilisateur ajouté';
			}else{
				$response['error'] = true;
				$response['message'] = 'une erreure s\'est produite';	 
		}
	}

	} else if ( $_GET['apical']=="getMyStoreList"){
		$db = new DBOperation();
		if ($_POST['mailAdress']!=""){
			$verif = $db->verifyUserExistance($_POST['mailAdress']);
			if($verif){
				$res=$db->getStoreList($_POST['mailAdress']);
				if ($res!=null){
					$response['error'] = false;
					$response['message'] = 'stores trouvés';
					$response['stores']=$res;
				}
				else {
					$response['error'] = true;
					$response['message'] = 'aucun store trouvé';
				}
			}
		}else {
			if($_POST['storeCategory']!="SELECTIONNER UNE CATEGORIE"){
				$res=$db->getStoreList2($_POST['storeCategory'], $_POST['motCle']);
				if ($res!=null){
					$response['error'] = false;
					$response['message'] = 'stores trouvés';
					$response['stores']=$res;
				}
				else {
					$response['error'] = true;
					$response['message'] = 'aucun store trouvé';
				}
			}else{
				$res=$db->getStoreList();
				if ($res!=null){
					$response['error'] = false;
					$response['message'] = 'stores trouvés';
					$response['stores']=$res;
				}
				else {
					$response['error'] = true;
					$response['message'] = 'aucun store trouvé';
			}
			}
		}
	} else if ( $_GET['apical']=="getMyItemList"){
		$db =  new DBOperation();
		if ($_POST){
			if ($_POST['all']=="yes"){
				$res=$db->getItemList();
				if ($res!=null){
					$response['error'] = false;
					$response['message'] = 'items trouvés';
					$response['items']=$res;
				}
				else {
					$response['error'] = true;
					$response['message'] = 'aucun item trouvé';
				}
			}else{
				$verif = $db->verifyUserExistance($_POST['mailAdress']);
				if($verif){
					$res=$db->getItemList($_POST['storeId']);
					if ($res!=null){
						$response['error'] = false;
						$response['message'] = 'items trouvés';
						$response['items']=$res;
					}
					else {
						$response['error'] = true;
						$response['message'] = 'aucun item trouvé';
					}
				}
			}
		}
		else {
			$res=$db->getItemList();
				if ($res!=null){
					$response['error'] = false;
					$response['message'] = 'Items trouvés';
					$response['items']=$res;
				}
				else {
					$response['error'] = true;
					$response['message'] = 'aucun store trouvé';
			}
		}
	} else if ( $_GET['apical']=="getUsersList"){
		$db = new DBOperation();
		$response['error'] = false;
		$response['message'] = 'requette passée avec succer';
		$response['users'] = $db->getUsersList();
	} else if ( $_GET['apical']=="getUserByMail"){
		//valideParam(array('mailAdress'));
		$db = new DBOperation();
		$result = $db->getUserByMail($_POST['mailAdress']);
		if($result != null){
			if($result['password'] == $_POST['password']){
				$response['error'] = false;
				$response['message'] = 'utilisateur trouvé';
				$response['user'] = $db->getUserByMail($_POST['mailAdress']);	
			}else{
				$response['error'] = true;
				$response['message'] = 'mot de passe errone';
			}
		}else{
			$response['error'] = true;
			$response['message'] = 'aucun utilisateur trouvé';
		} 
	}else if ($_GET['apical']=="updateUser"){
		$db = new DBOperation();
		$result = $db->updateUser($_POST['userName'], $_POST['mailAdress'], $_POST['password'], $_POST['age'], $_POST['Gender'], $_POST['fonction']);
		if($result){
			$response['error'] = false;
			$response['message'] = 'modifications faits avec succée';
		}else{
			$response['error'] = true;
			$response['message'] = 'une erreure s\'est produite';
		}
	}else if ($_GET['apical']=="updateRegId"){
		$db = new DBOperation();
		$result = $db->updateRegId($_POST['mailAdress'], $_POST['regId']);
		if($result){
			$response['error'] = false;
			$response['message'] = 'modifications regId done';
		}else{
			$response['error'] = true;
			$response['message'] = 'une erreure s\'est produite en modifiant le reg id';
		}
	}else if ($_GET['apical']=="uploadImage") {
		$db = new DBOperation();
		$result = $db->createStore($_POST['mailAdress'], $_POST['storeName'], $_POST['storePhone'], $_POST['storeAdress'], $_POST['storeCodePostal'], $_POST['storeLocality'], $_POST['storeLongitude'], $_POST['storeLatitude'], $_POST['storeDescription'], $_POST['storeCategory'], $_POST['storeOpening'], $_POST['storeClosing'],$_POST['image_data'], $_POST['image_tag']);
		if($result){
				$response['error'] = false;
				$response['message'] = 'le magasins est ajouté ';
		}else{
			$response['error'] = true;
			$response['message'] = 'une erreure s\'est produite ';
		}
	}else if ($_GET['apical']=="uploadItem") {
		$db = new DBOperation();
		$result = $db->createItem($_POST['idStore'], $_POST['articleName'], $_POST['articleDescription'], $_POST['price'], $_POST['image_data'], $_POST['image_tag']);
		if($result){
				$response['error'] = false;
				$response['message'] = 'le magasins est ajouté ';
		}else{
			$response['error'] = true;
			$response['message'] = 'une erreure s\'est produite ';
		}
	}
	else if ($_GET['apical']=="sendMessage") {
		$db = new DBOperation();
		$result = $db->saveMessage($_POST['name'], $_POST['phone'], $_POST['sender'], $_POST['message'], $_POST['replyEmail'],$_POST['storeId'], $_POST['receiver']);
		if($result){
			$result2 = $db->getUserByMail($_POST['receiver']);
			$firebase = new Firebase();
			$push = new Push();
			$payload = array();
			$payload['team'] = 'idrisAliouacheSakhi';
			$payload['score'] = '5.6';
			$push->setTitle($_POST['sender']);
			$push->setMessage($_POST['message']);
			$push->setImage('');
			$push->setIsBackground(FALSE);
        	$push->setPayload($payload);
			$json = '';
        	$response = '';
			$json = $push->getPush();
            $responseFinal = $firebase->send($result2["regId"], $json);
			$response['error'] = false;
			$response['message'] = 'le message est envoyé à '.$responseFinal;
		}else{
			$response['error'] = true;
			$response['message'] = 'une erreure s\'est produite ';
		}
	}else if ($_GET['apical']=="getMessages") {
		$db = new DBOperation();
		$result = $db->getMessages($_POST['receiver'],$_POST['type']);
		if($result!=null){
				$response['error'] = false;
				$response['message'] = 'Messages trouvés';
				$response['messages'] = $result;
		}else{
			$response['error'] = true;
			$response['message'] = 'une erreure s\'est produite ';
		}
	}else{
		$response['error'] = true;
		$response['message'] = 'appel a une fonction non definie';
	}

}else{
	$response['error'] = true;
	$response['message'] = 'un appel Api non valide';

}


echo json_encode($response);


?>