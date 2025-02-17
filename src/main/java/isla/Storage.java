package isla;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import isla.task.Task;
import isla.task.TaskList;

/**
 * Storage class to handle saving and loading of tasks to disk.
 */
public class Storage {
    private final Path savePath;

    public Storage(String filePath) {
        savePath = Paths.get(filePath);
    }

    /**
     * Loads the save file and returns the deserialized list of tasks.
     *
     * @return List of deserialized tasks.
     * @throws IslaException If IOException is encountered when reading.
     */
    public List<Task> load() throws IslaException {
        List<String> serializedTasks;
        try {
            serializedTasks = Files.readAllLines(savePath);
        } catch (IOException e) {
            throw new IslaException("Error when reading save file.");
        }

        ArrayList<Task> tasks = new ArrayList<>();
        for (String serializedTask : serializedTasks) {
            tasks.add(TaskList.deserialize(serializedTask));
        }

        return tasks;
    }

    /**
     * Writes the given list of serialized tasks to the save file.
     *
     * @param serializedTasks List of serialized tasks to save.
     * @throws IslaException If IOException is encountered when saving.
     */
    public void save(List<String> serializedTasks) throws IslaException {
        try {
            Files.createDirectories(savePath.getParent());
            Files.write(savePath, serializedTasks);
        } catch (Exception e) {
            throw new IslaException("Error when saving.");
        }
    }
}
