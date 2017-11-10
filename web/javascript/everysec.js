

    function everysec(i){

        var time = document.getElementById("time");
        time.innerHTML = i;
        i--;

        window.setTimeout("everysec(i)", 1000);
    }


