jQuery(function ($) {
    $('#form').submit(function (e) {
        if (!$('.industry_checkbox').is(':checked')) {
            e.preventDefault();
            document.getElementById("checkboxAlert").innerHTML =
                "<div class='alert alert-warning mt-4 mb-5 text-center'>" +
                "  <strong>Industry Selection Missing!</strong> Please choose at least one Industry." +
                "</div>";
        }
    })
})