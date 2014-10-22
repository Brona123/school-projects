<?php
  require_once("conf/conf.php");
  $con = mysqli_connect(MYSQL_SERVER, MYSQL_LOGINNAME, MYSQL_PASSWORD, MYSQL_DATABASE);

  $nick = $_GET['nick'];
  $streak = $_GET['streak'];
  $score = $_GET['score'];

  $query = "INSERT INTO bugsmash_highscore(nick, streak, score) VALUES('$nick', '$streak', '$score')";
  if (!mysqli_query($con, $query)){
    echo 'Error in inserting entry';
  }

  mysqli_close($con);
?>
