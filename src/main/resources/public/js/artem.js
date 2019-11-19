"use strict";

$(document).ready(function () {
});

function jQueryUiSetup() {
    // Activate jQuery UI tabs section:
    // Also remember its state across sessions:
    var activeTab = localStorage.artemActiveTab ?
            parseInt(localStorage.artemActiveTab) : 0;
    $("#tabs").tabs({
        active: activeTab,
        activate: function (event, ui) {
            localStorage.artemActiveTab = $("#tabs")
                    .tabs("option", "active");
        }
    });
    // Activate jQuery UI accordion - left-hand nav.
    // Also remember its state across sessions:
    var activeAccordion = localStorage.artemActiveAccordion ?
            parseInt(localStorage.artemActiveAccordion) : false;
    $("#accordion").accordion({
        collapsible: true,
        active: activeAccordion,
        heightStyle: "content",
        activate: function (event, ui) {
            localStorage.artemActiveAccordion = $("#accordion")
                    .accordion("option", "active");
        }
    });
}

// Process any validation errors and/or action outcomes,
// resulting from server-side processing of html form data.
function handleFormErrors(sqlError, errors) {
    $(document).ready(function () {
        // data validation errors (hibernate validators):
        if (errors) {
            $.each($.parseJSON(errors), function (index, error) {
                if (error.field === 'class-level-error') {
                    showFormLevelError(error.message);
                } else {
                    showFieldLevelError(error);
                }
            });
        }
        // database errors - e.g. FK or unique index constraints:
        if (sqlError) {
            showFormLevelError(sqlError);
        } else {
            // A status of OK is indicated by a GET query parameter
            // at the end of the POST-redirect-GET cycle:
            var queryString = location.search;
            if (queryString && queryString.includes('result=ok')) {
                $('#' + 'artem-form-check-icon').show();
            }
        }
    });
}

function showFieldLevelError(error) {
    var fieldInError = $('#' + error.field);
    var errorMessageContainer = $('#' + error.field + '-row div.artem-field-error-msg');
    var errorIcon = $('#' + error.field + '-row img.artem-field-icon');
    $(fieldInError).addClass('artem-invalid-data-field');
    $(errorMessageContainer).text(error.message);
    $(errorIcon).show();
    // finally, put back the in-error value if we have one (e.g. from parse errors):
    if (error.value) {
        $(fieldInError).val(error.value);
    }
}

function showFormLevelError(message) {
// some validation errors are at the form level; not tied
// to one specific field (e.g. Latitude + Longitude fields).
    $('#' + 'artem-form-error-msg').text(message);
    $('#' + 'artem-form-cross-icon').show();
}

function artemDocReady(tableName) {

// This uses a language collation - English (en) suffices. See:
// https://cdn.datatables.net/plug-ins/1.10.19/sorting/intl.js
// This ensures words like "église" are sorted alongside "eglise",
// instead of after "zebra".  It also ensure "E" is sorted
//  alongside "e", instead of using sequential binary code values.
    $.fn.dataTable.ext.order.intl('en');

    // get previously stored page length and search text:
    var pageLength = localStorage.artemPageLength ? parseInt(localStorage.artemPageLength) : 10;
    var searchText = sessionStorage.artemSearchTerm ? sessionStorage.artemSearchTerm : '';

    var table = $('#' + tableName).DataTable({
        "pageLength": pageLength, // re-apply previously used page length
        "columnDefs": [
            {
                "visible": false, // hidden col - contains search data
                "targets": [-1]   // -1 means the final column in the table
            }
        ],
        // need to keep a space between [ [ and ] ] - these have
        // a special meaning in the thymeleaf template processor.
        // Type Alt+0160 to enter a non-breaking space!
        "order": [ [0, 'asc'] ],
        "search": {
            "search": searchText
        },
        "initComplete": function () {
            doAfterTableInitComplete(table, tableName);
        }
    });
    // Remove diacritics from search input, so that we can match things
    // like "église" with "eglise". Note we also have to remove diacritics
    // from the data to be searched.  That is done on the server - the
    // results are stored in an extra hidden searchable table column.
    // The below Unicode range covers only the most common (Latin alphabet)
    // diacritic marks - it is not comprehensive. The regex \p{Mark} does
    // not appear to be supported sufficiently across browser implementations.
    $('#' + tableName + '_filter input').off().keyup(function () {
        // let regex = /\p{Mark}/ug; - not widely implemented?
        // As an alternative, the below extension...
        // jQuery.fn.DataTable.ext.type.search.string(this.value);
        // ...is from here:
        // https://cdn.datatables.net/plug-ins/1.10.19/filtering/type-based/diacritics-neutralise.js
        var newval = splitLigatures(this.value.normalize("NFD").replace(/[\u0300-\u036f]/g, ""));
        sessionStorage.artemSearchTerm = newval;
        table.search(newval).draw();
    });
    // capture page length changes:
    $('#' + tableName).on('length.dt', function (e, settings, len) {
        localStorage.artemPageLength = len;
    });
}

// see the java method org.dataartem.util.splitLigatures().
function splitLigatures(value) {
    return value.replace(/æ/g, "ae")
            .replace(/Æ/g, "ae")
            .replace(/ꜵ/g, "ao")
            .replace(/Ꜵ/g, "ao")
            .replace(/ꜷ/g, "au")
            .replace(/Ꜷ/g, "au")
            .replace(/ﬀ/g, "ff")
            .replace(/ﬃ/g, "ffi")
            .replace(/ﬄ/g, "ffl")
            .replace(/ﬁ/g, "fi")
            .replace(/ﬂ/g, "fl")
            .replace(/œ/g, "oe")
            .replace(/Œ/g, "oe")
            .replace(/ꝏ/g, "oo")
            .replace(/Ꝏ/g, "oo")
            .replace(/ß/g, "ss")
            .replace(/ẞ/g, "ss")
            .replace(/ᵫ/g, "ue");
}

// This can be called after a datatable has finished initializing.  See
// the following: https://datatables.net/reference/option/initComplete.
function doAfterTableInitComplete(table, tableName) {
    // wrap table controls in a new div, for alignment:
    $('#' + tableName + '_length').next().addBack()
            .wrapAll("<div class='artem-data-table-ctls-wrapper'/>");
}

// Used to support form checkboxes.  Ensures a value
// is always submitted in a form, even for unchecked
// checkboxes. See the Thymeleaf 'input-checkbox'
// fragment in forms.html.
function handleCheckBoxState(field) {
    var tgtFieldID = '#' + field.name.substring(0, field.name.length - 6);
    if ($(field).is(':checked')) {
        $(tgtFieldID).val("1");
    } else {
        $(tgtFieldID).val("0");
    }
}
