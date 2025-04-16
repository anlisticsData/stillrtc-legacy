 
  document.addEventListener("DOMContentLoaded", function(event) {
    console.log("DOM completamente carregado e analisado");

    try{

        const user=JSON.parse(Android.userInformations())

        $("#name").html(user.user)
        $("#pontos").html(user.pontos)
        $("#nivel").html(user.nivel)
        $("#email").html(user.email)
        $("#avatar-user").attr("src","data:image/jpeg;base64,"+user.avatar)




        $("#goToStore").on("click",function(event){
            window.location.href="lojastill.html"
        })

        $("#btn-open-camera").on("click",function(event){
          Android.pickCamera()
        })



        



    }catch(e){}


 






  });
 
