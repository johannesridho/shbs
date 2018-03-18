$.fn.dataTable.ext.errMode = 'none';

var Utility = {

    convertToGMT7: function (dateString) {
        if (typeof dateString !== 'undefined') {
            var utcDate = moment.utc(dateString);
            return utcDate.utcOffset(GMTConfiguration.HOUR_DIFFERENCE).format(GMTConfiguration.MOMENTJS_MYSQL_DATE_FORMAT) +
                ' ' + GMTConfiguration.TIMEZONE_ABBREVIATION;
        }
        return '';
    },

    getUrlParameter: function (param) {
        var pageURL = decodeURIComponent(window.location.search.substring(1)),
            URLVariables = pageURL.split('&'),
            parameterName,
            i;

        for (i = 0; i < URLVariables.length; ++i) {
            parameterName = URLVariables[i].split('=');

            if (parameterName[0] === param) {
                return parameterName[1] === undefined ? true : parameterName[1];
            }
        }
    },

    convertNumberToLocaleFormat: function (number) {
        return number.toLocaleString('id-ID');
    },

    showConfirmDialog: function() {
        return confirm('Are you sure?');
    },

    dataTableColumnsWrapper: function (columns) {
        var i = 0,
            length = columns.length;

        for (; i < length; ++i) {
            if (!columns[i].render) {
                // text() render will be used to avoid XSS attack
                columns[i].render = $.fn.dataTable.render.text();
            }
        }

        return columns;
    },

    escapeHtml: function (text) {
        return $('<span/>').text(text).html();
    },

    passwordsAreEquals: function (passwordName, rePasswordName, errorText) {
        var pass = $('input[name=' + passwordName + ']');
        var rePass = $('input[name=' + rePasswordName + ']');
        if (pass.val() != rePass.val()) {
            $(errorText).addClass('text-danger');
            $(errorText).text('New passwords not match');
            $('#submit-btn').attr('disabled', 'disabled');
        } else {
            $(errorText).text('');
            $('#submit-btn').removeAttr('disabled');
        }
    },

    convertToUTCDateTimeString: function (dateString, hours, minutes, seconds) {
        var isoDate = new Date(dateString);
        isoDate.setUTCHours(hours);
        isoDate.setUTCMinutes(minutes);
        isoDate.setUTCSeconds(seconds);

        return isoDate.toISOString();
    },

    getDataTableQueryString: function (table) {
        return $.param(table.ajax.params())
    },

    yadcf: {
        init: function (tableId, dataTable, columnProperties) {
            var yadcfProperties = [];

            for (var columnNumber in columnProperties) {
                var properties = columnProperties[columnNumber];
                if (typeof properties != 'object') {
                    properties = {type: properties};
                }

                var yadcfProperty = {};
                yadcfProperty['column_number'] = columnNumber;
                yadcfProperty['filter_type'] = properties.type;
                yadcfProperty['filter_delay'] = DatatablesFilterConfiguration.FILTER_DELAY_IN_MILLISECONDS;

                switch (properties.type) {
                    case 'range_date':
                        yadcfProperty['datepicker_type'] = DatatablesFilterConfiguration.DATEPICKER_TYPE;
                        yadcfProperty['filter_plugin_options'] = DatatablesFilterConfiguration.DATETIMEPICKER_OPTIONS;
                        yadcfProperty['date_format'] = GMTConfiguration.MOMENTJS_MYSQL_DATE_FORMAT;
                        yadcfProperty['moment_date_format'] = GMTConfiguration.MOMENTJS_MYSQL_DATE_FORMAT;
                        break;
                    case 'select':
                        yadcfProperty['data'] = properties.data;
                        break;
                    case 'select2':
                        yadcfProperty['data'] = properties.data;
                        yadcfProperty['select_type_options'] = properties.select2Options;
                        break;
                }

                yadcfProperties.push(yadcfProperty);
            }

            yadcf.init(dataTable, yadcfProperties);

            Utility.yadcf.setRangeDateOnChangeListener(tableId, yadcfProperties);
            Utility.yadcf.setRangeDateOnRequestListener(tableId, yadcfProperties);
        },

        setRangeDateOnChangeListener: function (tableId, columnProperties) {
            columnProperties.forEach(function (properties) {
                if (properties.filter_type === 'range_date') {
                    $('#yadcf-filter--' + tableId + '-from-date-' + properties.column_number + ', ' +
                        '#yadcf-filter--' + tableId + '-to-date-' + properties.column_number).change(function (event) {
                        yadcf.rangeDateKeyUP('-' + tableId, GMTConfiguration.MOMENTJS_MYSQL_DATE_FORMAT, event);
                    })
                }
            });
        },

        setRangeDateOnRequestListener: function (tableId, columnProperties) {
            $('#' + tableId).on('preXhr.dt', function (e, settings, data) {
                columnProperties.forEach(function (properties) {
                    var columnNumber = properties.column_number;
                    var filterValue = data.columns[columnNumber].search.value;
                    if (properties.filter_type === 'range_date' && filterValue !== '') {
                        if (filterValue !== '') {
                            data.columns[columnNumber].search.value =
                                Utility.yadcf.getUTCRangeDateString(filterValue, DatatablesFilterConfiguration.RANGE_SEARCH_FILTER_DELIMITER);
                        }
                    }
                });
            });
        },

        getUTCRangeDateString: function (dateRangeSearchString, delimiter) {
            var result = '',
                dateValues = dateRangeSearchString.split(delimiter);

            var dateFrom = moment(dateValues[0]).utc();
            if (dateFrom.isValid()) {
                dateFrom.millisecond(0);
                result += dateFrom.toISOString();
            }

            result += delimiter;

            var dateTo = moment(dateValues[1]).utc();
            if (dateTo.isValid()) {
                dateTo.millisecond(999);
                result += dateTo.toISOString();
            }

            return result;
        }
    }
};
