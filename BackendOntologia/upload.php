<?php

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
$path = 'Temp/';
if (isset($_FILES['file'])) {
	$originalName = $_FILES['file']['name'];
	$ext = '.'.pathinfo($originalName, PATHINFO_EXTENSION);
	$generatedName = md5($_FILES['file']['tmp_name'].date('dmYHis').rand (0,100)).$ext;
	$filePath = $path.$generatedName;

	if (!is_writable($path)) {
		echo json_encode(array(
			'status' => false,
			'msg'    => 'Destination directory not writable.'
		));
		exit;
	}


	if (move_uploaded_file($_FILES['file']['tmp_name'], $filePath)) {
		$porta=file_get_contents("temp.txt")+1;
		$fp = fopen("temp.txt", 'w');
		$escreve = fwrite($fp, $porta);
		fclose($fp); 
		$xmlDoc = new DOMDocument("1.0", "UTF-8");
		$xmlDoc->preserveWhiteSpace = false;
		$xmlDoc->loadXML(file_get_contents("Temp/{$generatedName}"), LIBXML_NOBLANKS | LIBXML_NOEMPTYTAG);
		$baseUri=$xmlDoc->getElementsByTagName('Ontology')->item(0)->getAttribute('rdf:about');

		unlink("Temp/{$generatedName}");
		$fp = fopen("Temp/{$generatedName}", 'w');
		$escreve = fwrite($fp, $xmlDoc->saveXML());
		fclose($fp); 
		
		$fp = fopen("Temp/inferid-{$generatedName}", 'w');
		$escreve = fwrite($fp, $xmlDoc->saveXML());
		fclose($fp); 

		$comando="java -jar ".$_SERVER['DOCUMENT_ROOT']."/BackendOntologia/owl2-swrl-tutorial-master/target/owltutorial-1.2.2-jar-with-dependencies.jar ".$_SERVER['DOCUMENT_ROOT']."/BackendOntologia/Temp"."/{$generatedName} ".$_SERVER['DOCUMENT_ROOT']."/BackendOntologia/Temp"."/inferid-{$generatedName} ".$baseUri;
		exec($comando);
		$comando="fuseki-server --port={$porta} --file=Temp/inferid-{$generatedName} /ds";
		exec($comando);
		echo json_encode(array(
			'status'        => true,
			'originalName'  => $originalName,
			'generatedName' => $generatedName,
			'porta'=> $porta,
			'baseUri'=>$baseUri
		));
	}else
	{
		echo json_encode(array(
			'status'        => false
		));
	}

}



















/*

$ontologia="";
$ontologiaInferida="";
$uri="";



$comando="java -jar owltutorial-1.2.2-jar-with-dependencies.jar {$ontologia} /"{$ontologiaInferida}/" /"{$uri}/"";
echo $comando;
//exec($comando);
$porta=file_get_contents("temp.txt")+1;
$fp = fopen("temp.txt", "a");
$escreve = fwrite($fp, "1");
fclose($fp); 
$comando="fuseki-server --port={$temp} --file=Teste.owl /ds";
//exec($comando);
*/
?>