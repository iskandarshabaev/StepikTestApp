package io.somedomain.stepiktestapp.data.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(
        active = true,
        generateGettersSetters = true
)
public class Course {

    @Id
    @SerializedName("id")
    private long id;
    @SerializedName("position")
    private long position;
    @SerializedName("score")
    private double score;
    @SerializedName("target_id")
    private long targetId;
    @SerializedName("target_type")
    private String targetType;
    @SerializedName("course")
    private long course;
    @SerializedName("course_owner")
    private long courseOwner;
    @SerializedName("course_title")
    private String courseTitle;
    @SerializedName("course_slug")
    private String courseSlug;
    @SerializedName("course_cover")
    private String courseCover;
    @SerializedName("is_favourite")
    private boolean isFavourite;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2063667503)
    private transient CourseDao myDao;

    @Generated(hash = 140416384)
    public Course(long id, long position, double score, long targetId, String targetType,
            long course, long courseOwner, String courseTitle, String courseSlug,
            String courseCover, boolean isFavourite) {
        this.id = id;
        this.position = position;
        this.score = score;
        this.targetId = targetId;
        this.targetType = targetType;
        this.course = course;
        this.courseOwner = courseOwner;
        this.courseTitle = courseTitle;
        this.courseSlug = courseSlug;
        this.courseCover = courseCover;
        this.isFavourite = isFavourite;
    }

    @Generated(hash = 1355838961)
    public Course() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPosition() {
        return this.position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return this.targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public long getCourse() {
        return this.course;
    }

    public void setCourse(long course) {
        this.course = course;
    }

    public long getCourseOwner() {
        return this.courseOwner;
    }

    public void setCourseOwner(long courseOwner) {
        this.courseOwner = courseOwner;
    }

    public String getCourseTitle() {
        return this.courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseSlug() {
        return this.courseSlug;
    }

    public void setCourseSlug(String courseSlug) {
        this.courseSlug = courseSlug;
    }

    public String getCourseCover() {
        return this.courseCover;
    }

    public void setCourseCover(String courseCover) {
        this.courseCover = courseCover;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public boolean getIsFavourite() {
        return this.isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 94420068)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCourseDao() : null;
    }

}
