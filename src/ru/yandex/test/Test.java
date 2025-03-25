package ru.yandex.test;

import ru.yandex.controller.TaskManager;
import ru.yandex.model.Epic;
import ru.yandex.model.SubTask;
import ru.yandex.model.Task;
import ru.yandex.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.System.out;

public class Test {
    static TaskManager taskManager;

    public static void main(String[] args) {
        testGetTask();
        testDeleteAllTask();
        testGetById();
        testUpdateTask();
        testDeleteByID();
        testGetSubTaskById();
    }

    public static void testGetSubTaskById() {
        initialTaskManager();
        List<Epic> epics = initialEpic();
        List<SubTask> subTasks = initialSubTask();

        for (Epic epic : epics) {
            subTasks.forEach(s -> {
                if (s.getEpicId() == epic.getId()) epic.addSubTask(s);
            });
        }

        Set<SubTask> subTasksEpic4 = epics.get(0).getSubTasks();
        Set<SubTask> subTasksEpic5 = epics.get(1).getSubTasks();
        Set<SubTask> subTasksEpic6 = epics.get(2).getSubTasks();

        out.println("Тестирование получение подзадач по эпику: ");
        out.println("\t сравнение подзадач эпика с id " + 4 + ": "
                + colorForBoolean(subTasksEpic4.equals(taskManager.getSubTaskByEpic((Epic) taskManager.getTaskById(4)))));
        out.println("\t сравнение подзадач эпика с id " + 5 + ": "
                + colorForBoolean(subTasksEpic5.equals(taskManager.getSubTaskByEpic((Epic) taskManager.getTaskById(5)))));
        out.println("\t сравнение подзадач эпика с id " + 6 + ": "
                + colorForBoolean(subTasksEpic6.equals(taskManager.getSubTaskByEpic((Epic) taskManager.getTaskById(6)))));


    }

    public static void testDeleteByID() {
        List<Task> tasks = new ArrayList<>(initialTask());
        List<Epic> epics = new ArrayList<>(initialEpic());
        List<SubTask> subTasks = new ArrayList<>(initialSubTask());

        tasks.removeFirst();
        epics.remove(1);
        subTasks.remove(3);
        subTasks.remove(3);


        initialTaskManager();
        out.println("Тестирование удаление задачи по id: ");

        int[] idTasksDelete = new int[]{0, 1, 5, 10, 100};
        boolean[] resultDelete = new boolean[]{false, true, true, false, false};

        int i = 0;
        for (int id : idTasksDelete) {
            boolean result = taskManager.deleteById(id);
            out.print("\tУдаление задачи с id " + id);
            out.print(" ,ожидание: " + resultDelete[i] + ", результат: " + result);
            out.println(" - " + colorForBoolean(result == resultDelete[i]));
            i++;
        }

        out.println("Список Task ожидания равен результату: "
                + colorForBoolean(tasks.equals(taskManager.getTaskList())));
        out.println("Список Subtask ожидания равен результату: "
                + colorForBoolean(subTasks.equals(taskManager.getSubTaskList())));
        out.println("Список Epic ожидания равен результату: "
                + colorForBoolean(epics.equals(taskManager.getEpicList())));


        initialTaskManager();
        out.println("Проверка статуса Epic при удалении подзадачи");

        subTasks = new ArrayList<>(initialSubTask());

        subTasks.get(1).setTaskStatus(TaskStatus.IN_PROGRESS);
        subTasks.get(2).setTaskStatus(TaskStatus.IN_PROGRESS);
        subTasks.get(3).setTaskStatus(TaskStatus.IN_PROGRESS);
        subTasks.get(4).setTaskStatus(TaskStatus.DONE);

        taskManager.updateTask(subTasks.get(0));
        taskManager.updateTask(subTasks.get(1));
        taskManager.updateTask(subTasks.get(2));
        taskManager.updateTask(subTasks.get(3));
        taskManager.updateTask(subTasks.get(4));

        Task checkEpic1 = taskManager.getTaskById(4);
        Task checkEpic2 = taskManager.getTaskById(5);

        out.println("\t Статус задачи с Id " + 4 + " до удаления: "
                + checkEpic1.getTaskStatus() + ", ожидание: " + TaskStatus.IN_PROGRESS);
        out.println("\t Статус задачи с Id " + 5 + " до удаления: "
                + checkEpic2.getTaskStatus() + ", ожидание: " + TaskStatus.IN_PROGRESS);

        taskManager.deleteById(8);
        taskManager.deleteById(9);
        taskManager.deleteById(10);

        out.println("\t Статус задачи с Id " + 4 + " после удаления: "
                + checkEpic1.getTaskStatus() + ", ожидание: "
                + TaskStatus.NEW + " - " + colorForBoolean(checkEpic1.getTaskStatus() == TaskStatus.NEW));
        out.println("\t Статус задачи с Id " + 5 + " после удаления: "
                + checkEpic2.getTaskStatus() + ", ожидание: "
                + TaskStatus.DONE + " - " + colorForBoolean(checkEpic1.getTaskStatus() == TaskStatus.NEW));
    }

    public static void testUpdateTask() {
        List<Task> assert1 = initialTask();
        List<Epic> assert2 = initialEpic();
        List<SubTask> assert3 = initialSubTask();

        out.println("Тестирование обновления задачи: ");
        out.println("Обновление Task: ");
        initialTaskManager();

        assert1.get(0).setTaskStatus(TaskStatus.IN_PROGRESS);
        assert1.get(1).setTaskStatus(TaskStatus.DONE);
        assert1.get(2).setName("Новое имя для задачи 1");

        taskManager.updateTask(assert1.get(0));
        taskManager.updateTask(assert1.get(1));
        taskManager.updateTask(assert1.get(2));

        int taskId = 1;
        for (Task task : assert1) {
            Task checkTask = taskManager.getTaskById(taskId);
            out.println("\tРезультат сравнения задачи с id "
                    + taskId + ": "
                    + colorForBoolean(task.equals(checkTask)));
            out.println("\tИмя задачи: " + task.getName() + ", " + checkTask.getName());
            out.println();
            taskId++;
        }

        out.println("Обновление Epic: ");
        initialTaskManager();

        assert2.get(0).setName("Новое имя для эпика 1");
        assert2.get(1).setDescription("Новое описание для эпика 2");
        assert2.get(2).setName("Новое имя эпика 3");
        assert2.get(2).setTaskStatus(TaskStatus.DONE);

        taskManager.updateTask(assert2.get(0));
        taskManager.updateTask(assert2.get(1));
        taskManager.updateTask(assert2.get(2));

        int epicId = 4;
        for (Epic task : assert2) {
            Task checkTask = taskManager.getTaskById(epicId);
            out.println("\tРезультат полного обновления задачи с id "
                    + epicId + ": "
                    + colorForBoolean(task.equals(checkTask)));
            out.println("\tИмя задачи: " + task.getName() + ", " + checkTask.getName());
            out.println("\tИмя задачи: " + task.getDescription() + ", " + checkTask.getDescription());
            out.println();
            epicId++;
        }

        initialTaskManager();
        out.println("Обновление SubTask: ");

        assert3.get(0).setName("Новое имя подзадачи 1");
        assert3.get(0).setDescription("Новое описание подзадачи 1");
        assert3.get(1).setTaskStatus(TaskStatus.IN_PROGRESS);
        assert3.get(2).setTaskStatus(TaskStatus.DONE);
        assert3.get(3).setTaskStatus(TaskStatus.DONE);
        assert3.get(4).setTaskStatus(TaskStatus.DONE);

        taskManager.updateTask(assert3.get(0));
        taskManager.updateTask(assert3.get(1));
        taskManager.updateTask(assert3.get(2));
        taskManager.updateTask(assert3.get(3));
        taskManager.updateTask(assert3.get(4));

        int subTuskId = 7;
        for (SubTask task : assert3) {
            Task checkTask = taskManager.getTaskById(subTuskId);
            out.println("\tРезультат полного обновления задачи с id "
                    + subTuskId + ": "
                    + colorForBoolean(task.equals(checkTask)));
            out.println("\tИмя задачи: " + task.getName() + ", " + checkTask.getName());
            out.println("\tИмя задачи: " + task.getDescription() + ", " + checkTask.getDescription());
            out.println("\tСтатус задачи: " + task.getTaskStatus() + ", " + checkTask.getTaskStatus());
            out.println();
            subTuskId++;
        }

        TaskStatus[] assertTaskStatusEpics = new TaskStatus[]
                {TaskStatus.IN_PROGRESS, TaskStatus.DONE, TaskStatus.NEW};

        int idTaskStatus = 0;
        out.println("Обновление статуса Epic по новому статусу SubTask: ");
        for (Epic epic : taskManager.getEpicList()) {
            out.println("\tEpic c id: " + epic.getId());
            out.print("\tСтатус: " + epic.getTaskStatus() + ", Ожидание: " + assertTaskStatusEpics[idTaskStatus]);
            out.println(" - " + colorForBoolean(epic.getTaskStatus() == assertTaskStatusEpics[idTaskStatus]));
            out.println();
            idTaskStatus++;
        }

    }

    public static void testGetById() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Имя задачи 1");
        task1.setDescription("Описание задачи 1");
        task1.setTaskStatus(TaskStatus.NEW);

        Epic epic2 = new Epic();
        epic2.setId(5);
        epic2.setName("Имя эпика 2");
        epic2.setDescription("Описание эпика 2");

        SubTask subTask4 = new SubTask("Имя подзадачи 4",
                "Описание подзадачи 4",
                TaskStatus.NEW,
                5);
        subTask4.setId(10);


        initialTaskManager();
        out.println("Тестирование получение задачи по id:");

        int[] checkId = new int[]{0, 1, 5, 10, 100};
        Task[] assertTasks = new Task[]{
                null,
                task1,
                epic2,
                subTask4,
                null
        };

        int i = 0;
        for (int id : checkId) {
            if (Objects.isNull(assertTasks[i])) {
                out.println("\t id " + id + ": " + colorForBoolean(Objects.isNull(taskManager.getTaskById(id))));
                i++;
                continue;
            }
            out.println("\t id " + id + ": " + colorForBoolean(taskManager.getTaskById(id).equals(assertTasks[i])));
            i++;
        }

    }

    public static void testDeleteAllTask() {
        out.println("Тестирование удаления всех задач:");
        boolean isRemoveTask;
        boolean isRemoveEpic;
        boolean isRemoveSubTask;

        initialTaskManager();
        taskManager.deleteAllTask();
        isRemoveTask = taskManager.getTaskList().isEmpty();
        isRemoveEpic = taskManager.getEpicList().size() == 3;
        isRemoveSubTask = taskManager.getSubTaskList().size() == 5;
        out.println("\t Task: " + colorForBoolean(isRemoveTask && isRemoveSubTask && isRemoveEpic));
        out.println("\t\t Task пуст " + isRemoveTask);
        out.println("\t\t Epic не пуст " + isRemoveEpic);
        out.println("\t\t SubTask не пуст " + isRemoveSubTask);

        initialTaskManager();
        taskManager.deleteAllEpic();
        isRemoveTask = taskManager.getTaskList().size() == 3;
        isRemoveEpic = taskManager.getEpicList().isEmpty();
        isRemoveSubTask = taskManager.getSubTaskList().isEmpty();
        out.println("\t Epic: " + colorForBoolean(isRemoveTask && isRemoveSubTask && isRemoveEpic));
        out.println("\t\t Task не пуст " + isRemoveTask);
        out.println("\t\t Epic пуст " + isRemoveEpic);
        out.println("\t\t SubTask пуст " + isRemoveSubTask);

        initialTaskManager();
        taskManager.deleteAllSubTask();
        isRemoveTask = taskManager.getTaskList().size() == 3;
        isRemoveEpic = taskManager.getEpicList().size() == 3;
        isRemoveSubTask = taskManager.getSubTaskList().isEmpty();
        out.println("\t SubTask: " + colorForBoolean(isRemoveTask && isRemoveSubTask && isRemoveEpic));
        out.println("\t\t Task не пуст " + isRemoveTask);
        out.println("\t\t Epic не пуст " + isRemoveEpic);
        out.println("\t\t SubTask пуст " + isRemoveSubTask);
    }

    public static void testGetTask() {
        initialTaskManager();
        List<Task> tasks = taskManager.getTaskList();
        List<Epic> epics = taskManager.getEpicList();
        List<SubTask> subTasks = taskManager.getSubTaskList();

        List<Task> assert1 = initialTask();
        List<Epic> assert2 = initialEpic();
        List<SubTask> assert3 = initialSubTask();

        out.println("Сравнение возвращаемых коллекций: ");
        out.println("\tTask сравнения: " + colorForBoolean(tasks.toString().equals(assert1.toString())));
        out.println("\tEpic сравнения: " + colorForBoolean(epics.toString().equals(assert2.toString())));
        out.println("\tSubtask сравнения: " + colorForBoolean(subTasks.toString().equals(assert3.toString())));

    }

    private static List<Task> initialTask() {
        Task task1 = new Task("Имя задачи 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Имя задачи 2", "Описание задачи 2", TaskStatus.NEW);
        Task task3 = new Task("Имя задачи 3", "Описание задачи 3", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        return List.of(task1, task2, task3);
    }

    private static List<Epic> initialEpic() {
        Epic epic1 = new Epic("Имя эпика 1", "Описание эпика 1");
        Epic epic2 = new Epic("Имя эпика 2", "Описание эпика 2");
        Epic epic3 = new Epic("Имя эпика 3", "Описание эпика 3");
        epic1.setId(4);
        epic2.setId(5);
        epic3.setId(6);
        return List.of(epic1, epic2, epic3);
    }

    private static List<SubTask> initialSubTask() {
        Epic epic1 = new Epic("Имя эпика 1", "Описание эпика 1");
        Epic epic2 = new Epic("Имя эпика 2", "Описание эпика 2");
        Epic epic3 = new Epic("Имя эпика 3", "Описание эпика 3");

        epic1.setId(4);
        epic2.setId(5);
        epic3.setId(6);

        SubTask subTask1 = new SubTask("Имя подзадачи 1",
                "Описание подзадачи 1",
                TaskStatus.NEW,
                epic1.getId());

        SubTask subTask2 = new SubTask("Имя подзадачи 2",
                "Описание подзадачи 2",
                TaskStatus.NEW,
                epic1.getId());

        SubTask subTask3 = new SubTask("Имя подзадачи 3",
                "Описание подзадачи 3",
                TaskStatus.NEW,
                epic1.getId());

        SubTask subTask4 = new SubTask("Имя подзадачи 4",
                "Описание подзадачи 4",
                TaskStatus.NEW,
                epic2.getId());

        SubTask subTask5 = new SubTask("Имя подзадачи 5",
                "Описание подзадачи 5",
                TaskStatus.NEW,
                epic2.getId());

        subTask1.setId(7);
        subTask2.setId(8);
        subTask3.setId(9);
        subTask4.setId(10);
        subTask5.setId(11);

        return List.of(subTask1, subTask2, subTask3, subTask4, subTask5);
    }

    public static void initialTaskManager() {
        taskManager = new TaskManager();

        Task task1 = new Task("Имя задачи 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Имя задачи 2", "Описание задачи 2", TaskStatus.NEW);
        Task task3 = new Task("Имя задачи 3", "Описание задачи 3", TaskStatus.NEW);

        Epic epic1 = new Epic("Имя эпика 1", "Описание эпика 1");
        Epic epic2 = new Epic("Имя эпика 2", "Описание эпика 2");
        Epic epic3 = new Epic("Имя эпика 3", "Описание эпика 3");

        SubTask subTask1 = new SubTask("Имя подзадачи 1",
                "Описание подзадачи 1",
                TaskStatus.NEW,
                4);

        SubTask subTask2 = new SubTask("Имя подзадачи 2",
                "Описание подзадачи 2",
                TaskStatus.NEW,
                4);

        SubTask subTask3 = new SubTask("Имя подзадачи 3",
                "Описание подзадачи 3",
                TaskStatus.NEW,
                4);

        SubTask subTask4 = new SubTask("Имя подзадачи 4",
                "Описание подзадачи 4",
                TaskStatus.NEW,
                5);

        SubTask subTask5 = new SubTask("Имя подзадачи 5",
                "Описание подзадачи 5",
                TaskStatus.NEW,
                5);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        taskManager.addTask(epic3);
        taskManager.addTask(subTask1);
        taskManager.addTask(subTask2);
        taskManager.addTask(subTask3);
        taskManager.addTask(subTask4);
        taskManager.addTask(subTask5);
    }

    private static String colorForBoolean(boolean b) {
        return b ? "\u001B[32m" + true + "\u001B[0m" : "\u001B[31m" + false + "\u001B[0m";
    }
}

