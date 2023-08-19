/**
 * bootstrap-admin-ext.js
 * 
 * Copyright 2012 Sega Corporation
 */

/* ===========================================================
 * bootstrap-tooltip-admin-ext.js v2.0.4
 * ========================================================== */
!function ($) {

    "use strict"; // jshint ;_;


    /* EXT-TOOLTIP PUBLIC CLASS DEFINITION
     * =============================== */

    var ExtTooltip = function ( element, options ) {
        this.init('exttooltip', element, options);
    };


    /* NOTE: ExtTooltip EXTENDS BOOTSTRAP-TOOLTIP.js
       ========================================== */

    ExtTooltip.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype, {

        constructor: ExtTooltip

        , isHTML: function(text) {
            if (!this.options.html) {
                return false;
            }
            // html string detection logic adapted from jQuery
            return typeof text != 'string'
                || ( text.charAt(0) === "<"
                && text.charAt( text.length - 1 ) === ">"
                && text.length >= 3
                ) || /^(?:[^<]*<[\w\W]+>[^>]*$)/.exec(text);
        }

    });


    /* EXT-TOOLTIP PLUGIN DEFINITION
     * ======================= */

    $.fn.exttooltip = function (option) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('exttooltip')
                , options = typeof option == 'object' && option;
            if (!data) $this.data('exttooltip', (data = new ExtTooltip(this, options)));
            if (typeof option == 'string') data[option]();
        });
    };

    $.fn.exttooltip.Constructor = ExtTooltip;

    $.fn.exttooltip.defaults = $.extend({} , $.fn.tooltip.defaults, {
        html: true
    });

}(window.jQuery);


/* ===========================================================
 * bootstrap-popover-table.js v2.0.4
 * ========================================================== */
!function ($) {

    "use strict"; // jshint ;_;


    /* POPOVER PUBLIC CLASS DEFINITION
     * =============================== */

    var PopoverTable = function ( element, options ) {
        this.init('popover_table', element, options);
    };


    /* NOTE: POPOVER_TABLE EXTENDS BOOTSTRAP-POPOVER.js
       ========================================== */

    PopoverTable.prototype = $.extend({}, $.fn.popover.Constructor.prototype, {

        constructor: PopoverTable

      , setContent: function () {
            var $tip = this.tip();
            var title = this.getTitle();
            var content = eval("("+ this.getContent() +")");
            var table = "";
            for (var key in content) {
                if (table == "") {
                    table = "<table class=\"table\"><tbody>";
                }
                // create table row with html escape
                table += "<tr><td>" + $("<span/>").text(key).html() + "</td>"
                    + "<td>" + $("<span/>").text(content[key]).html() + "</td></tr>";
            }
            if (table != "") {
                table += "</tbody></table>";
            }
            

            $tip.find('.popover-title')[this.isHTML(title) ? 'html' : 'text'](title);
            $tip.find('.popover-content > *')['html'](table);

            $tip.removeClass('fade top bottom left right in');
        }

    });


    /* POPOVER_TABLE PLUGIN DEFINITION
     * ======================= */

    $.fn.popover_table = function (option) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('popover_table')
                , options = typeof option == 'object' && option;
            if (!data) $this.data('popover_table', (data = new PopoverTable(this, options)));
            if (typeof option == 'string') data[option]();
        });
    };

    $.fn.popover_table.Constructor = PopoverTable;

    $.fn.popover_table.defaults = $.extend({} , $.fn.popover.defaults, {});

}(window.jQuery);