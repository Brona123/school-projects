<?php
  require_once("conf/conf.php");
  $con = mysqli_connect(MYSQL_SERVER, MYSQL_LOGINNAME, MYSQL_PASSWORD, MYSQL_DATABASE);

  $query = "SELECT * FROM bugsmash_highscore ORDER BY score DESC";
  
  $result = mysqli_query($con, $query);

  $json_arr = array();

  while($row = mysqli_fetch_array($result)){
    $row_array['nick'] = $row['nick'];
    $row_array['score'] = $row['score'];
    $row_array['streak'] = $row['streak'];

    array_push($json_arr, $row_array);
  }

  echo json_encode($json_arr);
  mysqli_close($con);
?>
