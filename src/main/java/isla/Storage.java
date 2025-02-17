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

    /**
     * Constructs a new storage object with the given file path.
     */
    public Storage(String filePath) {
        savePath = Paths.get(filePath);
    }

    /**
     * Loads the save file and returns the list of deserialized tasks.
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
     * Writes the given TaskList to the save file.
     *
     * @param tasks TaskList to save.
     * @throws IslaException If exception is encountered when saving.
     */
    public void save(TaskList tasks) throws IslaException {
        try {
            Files.createDirectories(savePath.getParent());
            Files.write(savePath, tasks.serialize());
        } catch (Exception e) {
            throw new IslaException("Error when saving.");
        }
    }
}
