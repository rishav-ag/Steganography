'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

var stegoFileForm = document.querySelector('#stegoFileForm');
var fileDownloadError = document.querySelector('#fileDownloadError');
var fileDownloadSuccess = document.querySelector('#fileDownloadSuccess');

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/upload-single-file");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function downloadStegoFile() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/download-decrypted-file");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            fileDownloadError.style.display = "none";
            fileDownloadSuccess.innerHTML = "<p>Decrypted File: .</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            fileDownloadSuccess.style.display = "block";
        } else {
            fileDownloadSuccess.style.display = "none";
            fileDownloadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }
    xhr.send();
}

singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);

stegoFileForm.addEventListener('submit', function(event) {
    downloadStegoFile();
    event.preventDefault();
}, true);