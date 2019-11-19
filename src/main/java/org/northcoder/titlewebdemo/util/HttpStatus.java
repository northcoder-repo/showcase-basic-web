package org.northcoder.titlewebdemo.util;

/**
 * Translates HTTP status numbers to descriptions (e.g. 404 is "Not Found").
 * There does not appear to be any readily available - and up-to-date - enum for
 * this in the core Java libraries.<p>
 * Enum names cannot start with digits - hence the use of underscores.
 *
 * @see
 * <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes" target="_blank">List
 * of HTTP status codes</a>.
 */
public enum HttpStatus {
    /**
     * Continue
     */
    _100("Continue", 100),
    /**
     * Switching Protocols
     */
    _101("Switching Protocols", 101),
    /**
     * OK
     */
    _200("OK", 200),
    /**
     * Created
     */
    _201("Created", 201),
    /**
     * Accepted
     */
    _202("Accepted", 202),
    /**
     * Non-Authoritative Information
     */
    _203("Non-Authoritative Information", 203),
    /**
     * No Content
     */
    _204("No Content", 204),
    /**
     * Reset Content
     */
    _205("Reset Content", 205),
    /**
     * Partial Content
     */
    _206("Partial Content", 206),
    /**
     * Multi-Status
     */
    _207("Multi-Status", 207),
    /**
     * Already Reported
     */
    _208("Already Reported", 208),
    /**
     * IM Used
     */
    _226("IM Used", 226),
    /**
     * Multiple Choices
     */
    _300("Multiple Choices", 300),
    /**
     * Moved Permanently
     */
    _301("Moved Permanently", 301),
    /**
     * Found
     */
    _302("Found", 302),
    /**
     * See Other
     */
    _303("See Other", 303),
    /**
     * Not Modified
     */
    _304("Not Modified", 304),
    /**
     * Use Proxy
     */
    _305("Use Proxy", 305),
    /**
     * Switch Proxy
     */
    _306("Switch Proxy", 306),
    /**
     * Temporary Redirect
     */
    _307("Temporary Redirect", 307),
    /**
     * Permanent Redirect
     */
    _308("Permanent Redirect", 308),
    /**
     * Bad Request
     */
    _400("Bad Request", 400),
    /**
     * Unauthorized
     */
    _401("Unauthorized", 401),
    /**
     * Payment Required
     */
    _402("Payment Required", 402),
    /**
     * Forbidden
     */
    _403("Forbidden", 403),
    /**
     * Not Found
     */
    _404("Not Found", 404),
    /**
     * Method Not Allowed
     */
    _405("Method Not Allowed", 405),
    /**
     * Not Acceptable"
     */
    _406("Not Acceptable", 406),
    /**
     * Proxy Authentication Required
     */
    _407("Proxy Authentication Required", 407),
    /**
     * Request Timeout
     */
    _408("Request Timeout", 408),
    /**
     * Conflict
     */
    _409("Conflict", 409),
    /**
     * Gone
     */
    _410("Gone", 410),
    /**
     * Length Required
     */
    _411("Length Required", 411),
    /**
     * Precondition Failed
     */
    _412("Precondition Failed", 412),
    /**
     * Payload Too Large
     */
    _413("Payload Too Large", 413),
    /**
     * URI Too Long
     */
    _414("URI Too Long", 414),
    /**
     * Unsupported Media Type
     */
    _415("Unsupported Media Type", 415),
    /**
     * Range Not Satisfiable
     */
    _416("Range Not Satisfiable", 416),
    /**
     * Expectation Failed
     */
    _417("Expectation Failed", 417),
    /**
     * I'm a teapot (yes, really)
     */
    _418("I'm a teapot", 418),
    /**
     * Misdirected Request
     */
    _421("Misdirected Request", 421),
    /**
     * Unprocessable Entity
     */
    _422("Unprocessable Entity", 422),
    /**
     * Locked
     */
    _423("Locked", 423),
    /**
     * Failed Dependency
     */
    _424("Failed Dependency", 424),
    /**
     * Too Early
     */
    _425("Too Early", 425),
    /**
     * Upgrade Required
     */
    _426("Upgrade Required", 426),
    /**
     * Precondition Required
     */
    _428("Precondition Required", 428),
    /**
     * Too Many Requests
     */
    _429("Too Many Requests", 429),
    /**
     * Request Header Fields Too Large
     */
    _431("Request Header Fields Too Large", 431),
    /**
     * Unavailable For Legal Reasons
     */
    _451("Unavailable For Legal Reasons", 451),
    /**
     * Internal Server Error
     */
    _500("Internal Server Error", 500),
    /**
     * Not Implemented
     */
    _501("Not Implemented", 501),
    /**
     * Bad Gateway
     */
    _502("Bad Gateway", 502),
    /**
     * Service Unavailable
     */
    _503("Service Unavailable", 503),
    /**
     * Gateway Timeout
     */
    _504("Gateway Timeout", 504),
    /**
     * HTTP Version Not Supported
     */
    _505("HTTP Version Not Supported", 505),
    /**
     * Variant Also Negotiates
     */
    _506("Variant Also Negotiates", 506),
    /**
     * Insufficient Storage
     */
    _507("Insufficient Storage", 507),
    /**
     * Loop Detected
     */
    _508("Loop Detected", 508),
    /**
     * Not Extended
     */
    _510("Not Extended", 510),
    /**
     * Network Authentication Required
     */
    _511("Network Authentication Required", 511),
    /**
     * Unknown Status
     */
    UNKNOWN("Unknown Status", 999);

    private final String statusName;
    private final int statusCode;

    public String getStatusName() {
        return this.statusName;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    HttpStatus(String statusName, int statusCode) {
        this.statusName = statusName;
        this.statusCode = statusCode;
    }
}
