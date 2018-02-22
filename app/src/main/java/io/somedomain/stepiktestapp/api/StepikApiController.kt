package io.somedomain.stepiktestapp.api

import io.reactivex.Observable
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.PageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StepikApiController {

    @GET("search-results")
    @Unwrap("search-results")
    fun searchCourses(@Query("query") query: String,
                      @Query("page") page: Int): Observable<PageResponse<MutableList<Course>>>

}