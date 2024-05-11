const toggleSidebar = () =>{
	if($(".sidebar").is(":visible")){
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}
	
	else {
		
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
};

function myFunction() {
  var x = document.getElementById("myInput");
  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }
}