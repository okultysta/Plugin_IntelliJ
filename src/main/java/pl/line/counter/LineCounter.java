package pl.line.counter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class LineCounter {

    public List<Stats> countLinesInProject(Project project) {
        com.intellij.openapi.vfs.LocalFileSystem.getInstance()
                .refresh(false);
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

        int total = 0, code = 0, comments = 0, blanks = 0;

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(file.getPath());
            System.out.println("Czytam z dysku: " + path + " | istnieje: " + java.nio.file.Files.exists(path));
            try (BufferedReader reader = java.nio.file.Files.newBufferedReader(path)) {
                String line;
                boolean inBlockComment = false;
                while ((line = reader.readLine()) != null) {
                    total++;
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) {
                        blanks++;
                    } else if (inBlockComment || trimmed.startsWith("/*")) {
                        comments++;
                        if (trimmed.contains("*/")) inBlockComment = false;
                        if (trimmed.startsWith("/*") && !trimmed.contains("*/")) inBlockComment = true;
                    } else if (trimmed.startsWith("//")) {
                        comments++;
                    } else {
                        code++;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return new Stats(file.getName(), extension, total, code, comments, blanks);
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
