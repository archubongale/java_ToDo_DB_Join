import java.util.List;
import org.sql2o.*;

public class Task {
  private String description;
  private int id;
  private Date due_date;
  private boolean is_completed;



  public Task(String description, Date due_date, boolean is_completed) {
    this.description = description;
    this.due_date = due_date;
    this.is_completed = is_completed;

  }

  public String getDescription() {
    return description;
  }


  public int getId() {
    return id;
  }

  public Date getDueDate() {
    return due_date;
  }

  public boolean getIsCompleted() {
    return is_completed;
  }


  public static List<Task> all() {
    String sql = "SELECT * FROM tasks";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  @Override
  public boolean equals(Object otherTask){
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      System.out.println(this.getCategoryId());
      System.out.println(newTask.getCategoryId());
      return this.getDescription().equals(newTask.getDescription()) &&
             this.getId() == newTask.getId();
    }
  }

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO Tasks (description, due_date, isCompleted ) VALUES (:description, :due_date, :is_completed)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("description", description)
      .addParameter("due_date",due_date)
      .addParameter("is_completed",is_completed)
      .executeUpdate()
      .getKey();
    }
  }

  public static Task find(int id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM Tasks where id=:id";
    Task task = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Task.class);
    return task;
    }
  }

  public void updateIsCompleted(boolean is_completed) {
   try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET is_completed = :is_completed) WHERE id = :id";
      con.createQuery(sql)
        .addParameter("is_completed", is_completed)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  // public void update(String description, boolean is_completed) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "UPDATE tasks SET description = :description, is_completed = :is_completed) WHERE id = :id";
  //     con.createQuery(sql)
  //       .addParameter("description", description)
  //       .addParameter("is_completed", is_completed)
  //       .addParameter("id", id)
  //       .executeUpdate();
  //   }
  // }

  public void delete() {
      try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM tasks WHERE id = :id;";
        con.createQuery(sql)
          .addParameter("id", id)
          .executeUpdate();
      }

      String joinDeleteQuery = "DELETE FROM categories_tasks WHERE task_id = :taskId";
      con.createQuery(joinDeleteQuery)
        .addParameter("taskId", this.getId())
        .executeUpdate();
    }

}
