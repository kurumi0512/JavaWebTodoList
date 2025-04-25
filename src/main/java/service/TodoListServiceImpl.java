package service;

import java.util.List;

import dao.TodoListDao;
import dao.TodoListDaoImpl;
import model.dto.TodoDTO;
import model.entity.Todo;

public class TodoListServiceImpl implements TodoListService {
	// 因為要調用資料庫的東西所以還是要NEW一個dao的實作
	private TodoListDao dao = new TodoListDaoImpl();

	/*
	 * 下面有簡化的寫法 @Override public List<TodoDTO> findAllTodos() { List<Todo> todos =
	 * dao.findAllTodos(); // List<Todo> 轉 List<TodoDTO> List<TodoDTO> todoDTOs =
	 * new ArrayList<>(); for(Todo todo : todos) {
	 * todoDTOs.add(transferToDTO(todo)); //下面有寫 建一個todo回傳一個todoDTO } return
	 * todoDTOs; }
	 */

	@Override
	public List<TodoDTO> findAllTodos() {
		return dao.findAllTodos().stream().map(this::transferToDTO) // 把每個元素轉換成dto
				// .map(todo -> transferToDTO(todo))
				.toList();
	}

	// 轉換成dTO
	@Override
	public TodoDTO getTodo(Integer id) {
		return transferToDTO(dao.getTodo(id));
	}

	@Override
	public void addTodo(String text, Boolean completed) {
		Todo todo = new Todo(0, text, completed);
		dao.addTodo(todo);
	}

	@Override
	public void updateTodoComplete(Integer id, Boolean completed) {
		dao.updateTodoComplete(id, completed);
	}

	@Override
	public void updateTodoText(Integer id, String text) {
		dao.updateTodoText(id, text);
	}

	@Override
	public void deleteTodo(Integer id) {
		dao.deleteTodo(id);
	}

	// 轉換成 DTO
	private TodoDTO transferToDTO(Todo todo) {
		return new TodoDTO(todo.getId(), todo.getText(), todo.getCompleted());
	}

	// 轉換成 Entity
	private Todo transferToEntity(TodoDTO todoDTO) {
		return new Todo(todoDTO.getId(), todoDTO.getText(), todoDTO.getCompleted());
	}

}