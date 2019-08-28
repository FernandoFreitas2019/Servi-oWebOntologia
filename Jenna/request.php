<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: text/json");
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);
use BorderCloud\SPARQL\SparqlClient;
require_once ('vendor/autoload.php');
$endpoint = "http://localhost:3030/ds/query";

if($request)
{	


	$sc = new SparqlClient();
	$sc->setEndpointRead($endpoint);
	$resposta=new stdclass();
	$resposta->rows =(object)$sc->query($request->query, 'rows')['result'];
	$resposta->err = $sc->getErrors();
	$arrayAux=array();
	foreach ($resposta->rows->rows as $key=>$r) 
	{
		$r=(array)$r;
		$a=array();
		foreach ($r as $key => $value) {
			if($value!="uri")
			{

				if(is_array($request->listaPrefixos)){
					foreach ($request->listaPrefixos as $pref) 
					{
						$value=str_replace($pref->url,'', $value);
					}

				}
				array_push($a, $value);
			}
		}
		array_push($arrayAux, $a);

		
	}
	$resposta->rows->rows=$arrayAux;
	
	echo json_encode($resposta);
}

?>