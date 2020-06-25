package co.uk.magmo.puretickets.storage

enum class TimeAmount(val length: Long?) {
    DAY(86400), WEEK(604800), MONTH(2628000), YEAR(31556952), FOREVER(null);
}