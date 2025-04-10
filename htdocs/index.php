<?php
$ie = (strpos($_SERVER['HTTP_USER_AGENT'],"MSIE") != false?true:false);
if($ie) header("Location: http://www.spreadfirefox.com/?q=affiliates&amp;id=205868&amp;t=215");
include('./header.php');
include('./main.php');
include('./footer.php');
?>