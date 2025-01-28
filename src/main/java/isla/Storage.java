package isla;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Storage {
    private Path savePath;

    public Storage(String filePath) {
        savePath = Paths.get(filePath);
    }

    public List<String> load() throws IslaException {
        List<String> serializedList;

        try {
            serializedList = Files.readAllLines(savePath);
        } catch (IOException e) {
            throw new IslaException("Error when reading save file.");
        }

        return serializedList;
    }

    public void save(List<String> serializedList) throws IslaException {
        try {
            Files.createDirectories(savePath.getParent());
            Files.write(savePath, serializedList);
        } catch (IOException e) {
            throw new IslaException("Error when saving.");
        }
    }
}
