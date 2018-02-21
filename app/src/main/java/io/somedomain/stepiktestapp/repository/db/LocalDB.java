package io.somedomain.stepiktestapp.repository.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.somedomain.stepiktestapp.model.Course;
import io.somedomain.stepiktestapp.model.CourseDao;
import io.somedomain.stepiktestapp.model.DaoMaster;
import io.somedomain.stepiktestapp.model.DaoSession;

/**
 * Локлаьная база данных
 */
public class LocalDB {

    public static final int DEFAULT_LIMIT = 20;

    private static LocalDB INSTANCE;
    private DaoSession daoSession;

    /**
     * @param context Application context
     */
    private LocalDB(@NotNull Context context) {
        DBOpenHelper helper = new DBOpenHelper(context, "localdb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * Database initialization
     *
     * @param context Application context
     */
    public static void initDB(@NotNull Context context) {
        INSTANCE = new LocalDB(context);
    }

    public static LocalDB getInstance() {
        return INSTANCE;
    }

    /**
     * Drop all tables
     */
    public void clear() {
        DaoMaster.dropAllTables(daoSession.getDatabase(), true);
        DaoMaster.createAllTables(daoSession.getDatabase(), true);
    }

    public List<Course> loadCourses() {
        return daoSession.getCourseDao().loadAll();
    }

    public void saveCourse(Course course) {
        daoSession.getCourseDao().insertOrReplace(course);
    }

    public void removeCourse(long courseId) {
        daoSession.getCourseDao()
                .queryBuilder()
                .where(CourseDao.Properties.Id.eq(courseId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

}
