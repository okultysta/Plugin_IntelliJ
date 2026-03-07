package pl.line.counter;

public class Stats {
    private final String fileName;
    private final String fileType;
    private final int totalLines;
    private final int codeLines;
    private final int commentLines;
    private final int blankLines;

    public Stats(String fileName, String fileType, int totalLines,
                 int codeLines, int commentLines, int blankLines) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.totalLines = totalLines;
        this.codeLines = codeLines;
        this.commentLines = commentLines;
        this.blankLines = blankLines;
    }

    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public int getTotalLines() { return totalLines; }
    public int getCodeLines() { return codeLines; }
    public int getCommentLines() { return commentLines; }
    public int getBlankLines() { return blankLines; }
}
