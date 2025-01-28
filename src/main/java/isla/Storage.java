package isla;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Storage class to handle saving and loading of tasks to disk.
 */
public class Storage {
    private Path savePath;

    public Storage(String filePath) {
        savePath = Paths.get(filePath);
    }

    /**
     * Reads the save file and returns the serialized task list.
     *
     * @return List of serialized tasks.
     * @throws IslaException If IOException is encountered when reading.
     */
    public List<String> load() throws IslaException {
        List<String> serializedTasks;

        try {
            serializedTasks = Files.readAllLines(savePath);
        } catch (IOException e) {
            throw new IslaException("Error when reading save file.");
        }

        return serializedTasks;
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
        } catch (IOException e) {
            throw new IslaException("Error when saving.");
        }
    }
}
