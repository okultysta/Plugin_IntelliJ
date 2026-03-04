package pl.line.counter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LineCounter {

    public List<Stats> countLinesInProject(Project project) {
        List<Stats> results = new ArrayList<>();
        VirtualFile projectDir = project.getBaseDir();

        if (projectDir == null) return results;

        visitFiles(projectDir, results);

        return results;
    }

    private void visitFiles(VirtualFile dir, List<Stats> results) {
        VirtualFile[] children = dir.getChildren();
        for (VirtualFile file : children) {
            if (file.isDirectory()) {
                visitFiles(file, results);
            } else {
                Stats stats = countLinesInFile(file);
                if (stats != null) {
                    results.add(stats);
                }
            }
        }
    }

    private Stats countLinesInFile(VirtualFile file) {
        String extension = file.getExtension();
        if (!isSourceFile(extension)) return null;

        int total = 0;
        int code = 0;
        int comments = 0;
        int blanks = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;
            boolean inBlockComment = false;

            while ((line = reader.readLine()) != null) {
                total++;
                String trimmed = line.trim();

                if (trimmed.isEmpty()) {
                    blanks++;
                } else if (inBlockComment || trimmed.startsWith("/*")) {
                    comments++;
                    if (trimmed.contains("*/")) {
                        inBlockComment = false;
                    }
                    if (trimmed.startsWith("/*") && !trimmed.contains("*/")) {
                        inBlockComment = true;
                    }
                } else if (trimmed.startsWith("//")) {
                    comments++;
                } else {
                    code++;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return new Stats(
                file.getName(),
                extension,
                total,
                code,
                comments,
                blanks
        );
    }


    private boolean isSourceFile(String extension) {
        return extension != null && (
                extension.equals("java") ||
                        extension.equals("kt") ||
                        extension.equals("xml") ||
                        extension.equals("properties") ||
                        extension.equals("gradle")
        );
    }
}
