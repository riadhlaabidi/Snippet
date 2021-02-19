function copyToClip() {
    let button = document.getElementById("copy_cb");
    let input = document.getElementById("copy_in");
    input.type = 'text';
    input.select();
    document.execCommand("copy");
    input.type = 'hidden';
    button.innerText = "Copied";
}