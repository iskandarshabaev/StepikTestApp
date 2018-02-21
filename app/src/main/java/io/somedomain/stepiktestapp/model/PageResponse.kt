package io.somedomain.stepiktestapp.model

class PageResponse<T>(
        var meta: Meta,
        var data: T
)