
$(document).ready(function() {
    $(".btn-warning").click(function() {
        $(this).closest("tr").find(".badge-warning").removeClass("badge-warning").addClass("badge-info").text("In Progress");
    });

    $(".btn-success").click(function() {
        $(this).closest("tr").find(".badge-warning, .badge-info").removeClass("badge-warning badge-info").addClass("badge-success").text("Completed");
    });

    $("select.custom-select-sm").change(function() {
        var status = $(this).val();
        var badgeClass;
        var badgeText;

        switch(status) {
            case "In Progress":
                badgeClass = "badge-info";
                badgeText = "In Progress";
                break;
            case "Completed":
                badgeClass = "badge-success";
                badgeText = "Completed";
                break;
            default:
                badgeClass = "badge-warning";
                badgeText = "Pending";
                break;
        }

        $(this).closest("tr").find(".badge-warning, .badge-info, .badge-success").removeClass("badge-warning badge-info badge-success").addClass(badgeClass).text(badgeText);
    });
});