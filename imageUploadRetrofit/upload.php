<?php

require "init.php";

if($con){
    $phone = $_POST['phone'];
    $image = $_POST['image'];

    $upload_path = "uploads/$phone.jpg";

    $sql = "INSERT INTO imageinfo(phone,path) VALUES('$phone','$upload_path')";

    if(mysqli_query($con,$sql)){
        file_put_contents($upload_path,base64_decode($image));

        $result = "ok";
    }
    else{
        $result = "failed";
    }

}
echo json_encode(array("response"=>$result));

mysqli_close($con);

?>
