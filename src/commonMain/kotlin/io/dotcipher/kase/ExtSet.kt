package io.dotcipher.kase

internal fun <T> Set<T>.containsNone(set: Set<T>): Boolean {
    for (element in set) {
        if (contains(element)) {
            return false
        }
    }
    return true
}
