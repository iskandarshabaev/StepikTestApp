package io.somedomain.stepiktestapp.api

enum class ResponseType {
    OBJECT,
    ARRAY,
    STRING,
    LONG,
    INT,
    BOOL
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Unwrap(val key: String, val type: ResponseType = ResponseType.OBJECT)

inline fun <reified T : Annotation> Array<out Annotation>?.getAnnotation(): T? {
    if (this == null) return null
    return firstOrNull { it is T } as T?
}