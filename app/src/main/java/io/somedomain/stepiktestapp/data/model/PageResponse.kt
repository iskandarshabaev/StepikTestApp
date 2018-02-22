package io.somedomain.stepiktestapp.data.model

class PageResponse<T>(
        var meta: Meta,
        var data: T
)