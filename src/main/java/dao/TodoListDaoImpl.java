package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.Todo;

public class TodoListDaoImpl extends BaseDao implements TodoListDao {

	@Override
	public List<Todo> findAllTodos() {
		List<Todo> todos = new ArrayList<>();

		String sql = "select id, text, completed from todo order by id";
		// statement(存放SQL語法的容器),跟SQL有關,先準備一個容器放statement進去
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			// 逐一尋訪 rs 中的每一筆紀錄
			// 再將每一筆紀錄轉成 Todo 物件
			// 最後加入到 todos 集合中

			while (rs.next()) {
				Todo todo = new Todo();
				todo.setId(rs.getInt("id"));
				todo.setText(rs.getString("text"));
				todo.setCompleted(rs.getBoolean("completed"));

				todos.add(todo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return todos;
	}

	// ?號是1 所以setInt(1,id)的1就是第一個問號
	// try可以寫兩個,catch 只要有一個就好(會共用)
	public Todo getTodo(Integer id) {
		String sql = "select id, text, completed from todo where id=?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			// 得到 rs 中的紀錄(1或0筆)
			// 再將該筆紀錄轉成 Todo 物件並回傳
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Todo todo = new Todo();
					todo.setId(rs.getInt("id"));
					todo.setText(rs.getString("text"));
					todo.setCompleted(rs.getBoolean("completed"));

					return todo;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addTodo(Todo todo) {
		// id會自己新增所以不用加id
		String sql = "insert into todo(text, completed) value(?, ?)";
		// 只要有問號都要用PreparedStatement
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, todo.getText());
			pstmt.setBoolean(2, todo.getCompleted());

			int rowcount = pstmt.executeUpdate(); // 執行修改/新增
			System.out.println("新增 todo 筆數: " + rowcount);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateTodoComplete(Integer id, Boolean completed) {
		String sql = "update todo set completed=? where id=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setBoolean(1, completed);
			pstmt.setInt(2, id);

			int rowcount = pstmt.executeUpdate();
			System.out.println("修改 todo Complete 筆數: " + rowcount);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateTodoText(Integer id, String text) {
		String sql = "update todo set text=? where id=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, text);
			pstmt.setInt(2, id);

			int rowcount = pstmt.executeUpdate();
			System.out.println("修改 todo Text 筆數: " + rowcount);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteTodo(Integer id) {
		String sql = "delete from todo where id=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			int rowcount = pstmt.executeUpdate();
			System.out.println("刪除 todo 筆數: " + rowcount);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}