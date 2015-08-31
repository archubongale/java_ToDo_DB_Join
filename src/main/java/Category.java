import java.util.ArrayList;
import org.sql2o.*;
import java.util.List;

public class Category {

  private String name;
  private int id;

  public Category(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public static List<Category> all() {
    String sql = "SELECT id, name FROM Categories";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }

  @Override
  public boolean equals(Object otherCategory){
    if (!(otherCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories where id=:id";
      Category Category = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Category.class);
      return Category;
    }
  }

  public List<Task> getTasks() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT tasks.* FROM categories JOIN categories_tasks ON (categories_tasks.category_id = categories.id) JOIN tasks ON (categories_tasks.task_id = tasks.id) WHERE category_id=:id ORDER BY due_date ASC";
    return con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetch(Task.class);
    }
  }


  public void addTask(Task task) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_tasks (category_id,task_id) VALUES (:category_id, :task_id)";
      con.createQuery(sql)
          .addParameter("category_id", this.getId())
          .addParameter("task_id", task.getId())
          .executeUpdate();

    }
  }

}
