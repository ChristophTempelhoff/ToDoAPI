package de.tempelhoff.ToDoAPI.repository;

import de.tempelhoff.ToDoAPI.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    List<ToDo> findByIsDone(boolean isDone);
    List<ToDo> findAllByUserID(Integer userID);
}
