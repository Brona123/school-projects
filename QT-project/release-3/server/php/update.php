<!DOCTYPE html>
<html>
<head>
</head>
<body>

  <form method="get" action="update.php">
    <input type="text" name="server" />
    <input type="text" name="db" />
    <input type="text" name="user" />
    <input type="text" name="pass"/>
    <input type="text" name="ip"/>
    <input type="text" name="port"/>
  </form>

</body>
</html>

<?php

  $server = $_GET['server'];
  $db = $_GET['db'];
  $user = $_GET['user'];
  $pass = $_GET['pass'];
  $ip = $_GET['ip'];
  $port = $_GET['port'];

    
    $con = mysqli_connect($server, $user, $pass, $db);

    $query = "UPDATE QTicTacToeServer SET ip='$ip', port='$port' WHERE id=1";

    if (mysqli_query($con, $query)){
      echo 'Query successful!';
    } else {
      echo 'Query failed';
    }

    mysqli_close($con);

?>
