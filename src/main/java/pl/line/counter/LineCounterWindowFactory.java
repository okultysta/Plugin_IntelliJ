package pl.line.counter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;


public class LineCounterWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Tutaj stworzymy zawartość okna
        LineCounterWindow window = new LineCounterWindow(project);

        // Pobieramy fabrykę do tworzenia zawartości
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(window.getContent(), "", false);

        // Dodajemy zawartość do okna
        toolWindow.getContentManager().addContent(content);
    }
}
