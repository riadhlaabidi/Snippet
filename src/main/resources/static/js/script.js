let restrictionOptions = document.getElementById("private_options")
let publicRadio = document.getElementById("public");
let restrictedRadio = document.getElementById("private");

restrictedRadio.addEventListener('change', function () {
    if (this.checked) {
        restrictionOptions.removeAttribute("hidden");
    }
});

publicRadio.addEventListener('change', function () {
    if (this.checked) {
        restrictionOptions.setAttribute("hidden", "")
    }
});

function sanitize(value) {
    let intValue = parseInt(value)
    if (isNaN(intValue) || intValue < 0) {
        return 0;
    }
    return intValue;
}

function send() {

    let code = document.getElementById("code_snippet").value;
    let time;
    let views;

    if (!code.trim().length) {
        alert("Unfortunately, can't share empty snippets !");
        return;
    }

    if (publicRadio.checked) {
        time = 0;
        views = 0;
    } else if (restrictedRadio.checked) {
        time = sanitize(document.getElementById("time_restriction").value);
        views = sanitize(document.getElementById("views_restriction").value);
        if (time === 0 && views === 0) {
            alert("You need to specify at least one restriction ! \n " +
                "0 or negative values means absence of restriction.");
            return;
        }
    } else {
        alert("You need to specify a privacy option !");
        return;
    }

    let object = {
        "code": code,
        "time": time,
        "views": views
    };

    let json = JSON.stringify(object);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
    xhr.send(json);

    if (xhr.status === 200) {
        let uuid = JSON.parse(xhr.responseText)["id"];
        let message = "Success! " + uuid;
        alert(message);
    }
}