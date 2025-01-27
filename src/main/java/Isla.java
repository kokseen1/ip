public class Isla {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Isla(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IslaException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        boolean isExit = false;
        ui.greet();

        while (!isExit) {
            String command = ui.readCommand();

            try {
                if (Parser.parseAndExecute(command, tasks, ui, storage) == 1)
                    isExit = true;
            } catch (IslaException e) {
                System.out.println(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Isla("./data/tasks.txt").run();
    }
}
