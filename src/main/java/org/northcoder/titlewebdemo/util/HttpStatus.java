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
    _100("Continue"),
    /**
     * Switching Protocols
     */
    _101("Switching Protocols"),
    /**
     * OK
     */
    _200("OK"),
    /**
     * Created
     */
    _201("Created"),
    /**
     * Accepted
     */
    _202("Accepted"),
    /**
     * Non-Authoritative Information
     */
    _203("Non-Authoritative Information"),
    /**
     * No Content
     */
    _204("No Content"),
    /**
     * Reset Content
     */
    _205("Reset Content"),
    /**
     * Partial Content
     */
    _206("Partial Content"),
    /**
     * Multi-Status
     */
    _207("Multi-Status"),
    /**
     * Already Reported
     */
    _208("Already Reported"),
    /**
     * IM Used
     */
    _226("IM Used"),
    /**
     * Multiple Choices
     */
    _300("Multiple Choices"),
    /**
     * Moved Permanently
     */
    _301("Moved Permanently"),
    /**
     * Found
     */
    _302("Found"),
    /**
     * See Other
     */
    _303("See Other"),
    /**
     * Not Modified
     */
    _304("Not Modified"),
    /**
     * Use Proxy
     */
    _305("Use Proxy"),
    /**
     * Switch Proxy
     */
    _306("Switch Proxy"),
    /**
     * Temporary Redirect
     */
    _307("Temporary Redirect"),
    /**
     * Permanent Redirect
     */
    _308("Permanent Redirect"),
    /**
     * Bad Request
     */
    _400("Bad Request"),
    /**
     * Unauthorized
     */
    _401("Unauthorized"),
    /**
     * Payment Required
     */
    _402("Payment Required"),
    /**
     * Forbidden
     */
    _403("Forbidden"),
    /**
     * Not Found
     */
    _404("Not Found"),
    /**
     * Method Not Allowed
     */
    _405("Method Not Allowed"),
    /**
     * Not Acceptable"
     */
    _406("Not Acceptable"),
    /**
     * Proxy Authentication Required
     */
    _407("Proxy Authentication Required"),
    /**
     * Request Timeout
     */
    _408("Request Timeout"),
    /**
     * Conflict
     */
    _409("Conflict"),
    /**
     * Gone
     */
    _410("Gone"),
    /**
     * Length Required
     */
    _411("Length Required"),
    /**
     * Precondition Failed
     */
    _412("Precondition Failed"),
    /**
     * Payload Too Large
     */
    _413("Payload Too Large"),
    /**
     * URI Too Long
     */
    _414("URI Too Long"),
    /**
     * Unsupported Media Type
     */
    _415("Unsupported Media Type"),
    /**
     * Range Not Satisfiable
     */
    _416("Range Not Satisfiable"),
    /**
     * Expectation Failed
     */
    _417("Expectation Failed"),
    /**
     * I'm a teapot (yes, really)
     */
    _418("I'm a teapot"),
    /**
     * Misdirected Request
     */
    _421("Misdirected Request"),
    /**
     * Unprocessable Entity
     */
    _422("Unprocessable Entity"),
    /**
     * Locked
     */
    _423("Locked"),
    /**
     * Failed Dependency
     */
    _424("Failed Dependency"),
    /**
     * Too Early
     */
    _425("Too Early"),
    /**
     * Upgrade Required
     */
    _426("Upgrade Required"),
    /**
     * Precondition Required
     */
    _428("Precondition Required"),
    /**
     * Too Many Requests
     */
    _429("Too Many Requests"),
    /**
     * Request Header Fields Too Large
     */
    _431("Request Header Fields Too Large"),
    /**
     * Unavailable For Legal Reasons
     */
    _451("Unavailable For Legal Reasons"),
    /**
     * Internal Server Error
     */
    _500("Internal Server Error"),
    /**
     * Not Implemented
     */
    _501("Not Implemented"),
    /**
     * Bad Gateway
     */
    _502("Bad Gateway"),
    /**
     * Service Unavailable
     */
    _503("Service Unavailable"),
    /**
     * Gateway Timeout
     */
    _504("Gateway Timeout"),
    /**
     * HTTP Version Not Supported
     */
    _505("HTTP Version Not Supported"),
    /**
     * Variant Also Negotiates
     */
    _506("Variant Also Negotiates"),
    /**
     * Insufficient Storage
     */
    _507("Insufficient Storage"),
    /**
     * Loop Detected
     */
    _508("Loop Detected"),
    /**
     * Not Extended
     */
    _510("Not Extended"),
    /**
     * Network Authentication Required
     */
    _511("Network Authentication Required"),
    /**
     * Unknown Status
     */
    UNKNOWN("Unknown Status");

    private final String status;

    public String getStatus() {
        return this.status;
    }

    HttpStatus(String status) {
        this.status = status;
    }
}
