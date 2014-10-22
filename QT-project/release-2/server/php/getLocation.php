<?php

  require_once("conf.php");
  $server = MYSQL_SERVER;
  $db = MYSQL_DATABASE;
  $user = MYSQL_LOGINNAME;
  $pass = MYSQL_PASSWORD;


    $con = mysqli_connect($server, $user, $pass, $db);

    $query = "SELECT * FROM QTicTacToeServer WHERE id=1";
   
    $result = mysqli_query($con, $query);

    $json_arr = array();
    
    while($row = mysqli_fetch_array($result)){
        $row_array['ip'] = $row['ip'];
        $row_array['port'] = $row['port'];

        array_push($json_arr, $row_array);
    }

    echo json_encode($json_arr);
    mysqli_close($con);

?>
