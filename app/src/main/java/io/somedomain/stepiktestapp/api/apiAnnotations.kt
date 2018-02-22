package io.somedomain.stepiktestapp.api

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Unwrap(val key: String)

inline fun <reified T : Annotation> Array<out Annotation>?.getAnnotation(): T? {
    if (this == null) return null
    return firstOrNull { it is T } as T?
}