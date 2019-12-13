jQuery(function ($) {
    $('#form').submit(function (e) {
        if (!$('.industry-checkbox').is(':checked')) {
            e.preventDefault();
            document.getElementById("checkbox-alert").innerHTML =
                "<div class='alert alert-warning mt-4 mb-5 text-center'>" +
                "  <strong>Industry Selection Missing!</strong> Please choose at least one Industry." +
                "</div>";
        }
    })
})