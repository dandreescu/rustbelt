package ru.compscicenter.edide.course;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: lia
 * Date: 21.06.14
 * Time: 18:40
 */
public class Lesson {
  public String name;
  public List<Task> taskList = new ArrayList<Task>();
  private Course myCourse = null;
  public int myIndex = -1;
  public static final String LESSON_DIR = "lesson";

  public StudyStatus getStatus() {
    for (Task task : taskList) {
      StudyStatus taskStatus = task.getStatus();
      if (taskStatus == StudyStatus.Unchecked || taskStatus == StudyStatus.Failed) {
        return StudyStatus.Unchecked;
      }
    }
    return StudyStatus.Solved;
  }

  public List<Task> getTaskList() {
    return taskList;
  }


  /**
   * Creates lesson directory in its course folder in project user created
   *
   * @param courseDir    project directory of course
   * @param resourceRoot directory where original lesson stored
   * @throws IOException
   */
  public void create(VirtualFile courseDir, File resourceRoot) throws IOException {
    String lessonDirName = LESSON_DIR + Integer.toString(myIndex + 1);
    VirtualFile lessonDir = courseDir.createChildDirectory(this, lessonDirName);
    for (int i = 0; i < taskList.size(); i++) {
      taskList.get(i).setIndex(i);
      taskList.get(i).create(lessonDir, new File(resourceRoot, lessonDir.getName()));
    }
  }


  /**
   * Initializes state of lesson
   *
   * @param course course which lesson belongs to
   */
  public void init(Course course, boolean isRestarted) {
    myCourse = course;
    for (Task task : taskList) {
      task.init(this, isRestarted);
    }
  }

  public Lesson next() {
    if (myIndex + 1 >= myCourse.getLessons().size()) {
      return null;
    }
    return myCourse.getLessons().get(myIndex + 1);
  }

  public void setIndex(int index) {
    myIndex = index;
  }

  public int getIndex() {
    return myIndex;
  }

  public Lesson prev() {
    if (myIndex - 1 < 0) {
      return null;
    }
    return myCourse.getLessons().get(myIndex - 1);
  }
}
