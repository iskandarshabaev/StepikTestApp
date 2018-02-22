package io.somedomain.stepiktestapp.util

import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.Meta
import io.somedomain.stepiktestapp.data.model.PageResponse

object Courses {
    fun getValidCourses(size: Int): PageResponse<MutableList<Course>> {
        return getValidCourses(size, false)
    }

    fun getValidCourses(size: Int, isFavourite: Boolean): PageResponse<MutableList<Course>> {
        val list = mutableListOf<Course>()
        for (i in 1..size) {
            list.add(Course().also {
                it.id = i.toLong()
                it.course = i.toLong()
                it.courseTitle = "some_title $i"
                it.courseCover = "some_cover $i"
                it.isFavourite = isFavourite
            })
        }
        return PageResponse(Meta(1, false, false), list)
    }

    fun getCourse(isFavourite: Boolean): Course {
        return Course().also {
            it.id = 1
            it.course = 1
            it.courseTitle = "some_title"
            it.courseCover = "some_cover"
            it.isFavourite = isFavourite
        }
    }
}