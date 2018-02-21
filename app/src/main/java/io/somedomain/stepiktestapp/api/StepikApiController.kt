package io.somedomain.stepiktestapp.api

import io.reactivex.Observable
import io.somedomain.stepiktestapp.model.PageResponse
import io.somedomain.stepiktestapp.model.Course
import retrofit2.http.GET
import retrofit2.http.Query

interface StepikApiController {

    @GET("search-results")
    @Unwrap("search-results", ResponseType.ARRAY)
    fun searchCourses(@Query("query") query: String): Observable<PageResponse<MutableList<Course>>>

}