
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>
<html lang="en">
<head>
<title>Select Business Center</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/png" href="../images/favicon-32x32.png" sizes="32x32" />
<link rel="icon" type="image/png" href="../images/favicon-16x16.png" sizes="16x16" />
<link rel="shortcut icon" href="#" />    
<link rel="stylesheet" href="../css/jquery-ui.css">
<link rel="stylesheet" href="../css/redmond-jquery-ui.css">
<link rel="stylesheet" href="../css/ui.jqgrid.css">
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="../css/font-awesome.css">
<link rel="stylesheet" href="../css/pixie-apt.css">
<link rel="stylesheet" href="../css/pixie-main.css">
<link rel="stylesheet" href="../css/pixie-media.css">
 
<script type="text/javascript" src="../js/pixie_js/respond.min.js"></script>
<script type="text/javascript" src="../js/pixie_js/modernizr-custom.js"></script>
<script type="text/javascript" src="../js/pixie_js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="../js/pixie_js/popper.min.js"></script>
<script type="text/javascript" src="../js/pixie_js/jquery-ui.js"></script>
<script type="text/javascript" src="../js/pixie_js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="../js/pixie_js/grid.locale-en.js"></script>
<script type="text/javascript" src="../js/pixie_js/bootstrap.min.js"></script>
<script type="text/javascript" src="../js/pixie_js/moment.min.js"></script>
<script type="text/javascript" 	src="../js/pixie_js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="../js/pixie_js/aes.js"> </script>
<!-- <script type="text/javascript" src="../js/customJS/BusinessCenterPop-up.js"></script> -->
</head>
<body>
<div class="wrapper">
   <jsp:include page="/pages/menu_hide.jsp" />
   <!-- <div class="loader-box">
    <div class="spinner-border text-primary" role="status">
    	<span class="sr-only">Loading...</span>
    </div> -->
</div> 
       <div class="content-page">
           <div class="content">
               <div class="modal fade" id="exampleModalCenter"  tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false" aria-labelledby="businessCenterModelTitle" aria-hidden="true">
                   <div class="modal-dialog modal-dialog-centered" role="document">
                       <div class="modal-content">
                           <div class="modal-header">
                               <h5 class="modal-title" id="exampleModalLongTitle">Select Business Center</h5>
                           </div>
                           <div class="modal-body">
                               <form action="/user" method="POST">
                                   <div class="form-group">
                                       <select class="form-control" id="exampleFormControlSelect1">
                                       <option value="0"  id="0">Select Business Center</option>
                                       </select>
                                   </div>
 										<!-- <a class="btn common-btn" id="savebtn" href='javascript:void(0);'>Save</a>   -->
 										<button class="btn common-btn" type="submit" id="savebtn">Save</button>  
                               </form>
                           </div>
                       </div>
                   </div>
               </div>
           </div>
       </div>
   </div>   
</body>
<script>
    $(document).ready(function() {
    $('#exampleModalCenter').modal('show');
  });
</script>


</html>