import java.nio.file.Path
import java.nio.file.Paths

Path projectPath = Paths.get(request.outputDirectory, request.artifactId)
Path pomFile = Paths.get(request.outputDirectory, request.artifactId, "pom.xml")
Path dbModuleDirectoryPath = projectPath.resolve(request.artifactId + "-db")
Path clientModuleDirectoryPath = projectPath.resolve(request.artifactId + "-client")
Properties properties = request.properties
String includeDBProperty = properties.get("includeDb")
String includeClientProperty = properties.get("includeClient")

def sb = new StringBuilder()
new Scanner(pomFile).withCloseable { sc ->
    while (sc.hasNext()) {
        currentLine = sc.nextLine()
        if (isShouldSkip(currentLine, includeDBProperty, includeClientProperty)) {
            continue
        }
        appendInPom(sb, currentLine)
    }
}
new PrintWriter(pomFile.toFile()).withCloseable { writer ->
    writer.write(sb.toString())
}

if (!includeDBProperty.equals("true")) {
    deleteDirectory(dbModuleDirectoryPath.toFile())
}
if (!includeClientProperty.equals("true")) {
    deleteDirectory(clientModuleDirectoryPath.toFile())
}

initGitRepo()

def deleteDirectory(File directoryToBeDeleted) {
    def allContents = directoryToBeDeleted.listFiles();
    if (allContents) {
        for (File file : allContents) {
            deleteDirectory(file);
        }
    }
    return directoryToBeDeleted.delete();
}

def boolean isShouldSkip(String currentLine, String includeDBProperty, String includeClientProperty ) {
    currentLine.trim().length() == 0 ||
            (currentLine.contains("<module>" + request.artifactId + "-db</module>") &&
                    !includeDBProperty.equals("true")) ||
            (currentLine.contains("<module>" + request.artifactId + "-client</module>") &&
                    !includeClientProperty.equals("true"))
}

def appendInPom(StringBuilder sb, String currentLine) {
    int numSpacesAtStart = currentLine.indexOf(currentLine.trim());
    if (!currentLine.contains("<blankLine/>")) {
        if (numSpacesAtStart % 2 == 0) {
            sb.append("  ".repeat(numSpacesAtStart / 2 as int))
        }
        sb.append(currentLine)
    }
    sb.append("\n")
}

def initGitRepo() {
    runCommand("git init -b develop", "./${artifactId}")
}

static def runCommand(String command, String workDir) {
    def process = command.execute(null, new File(workDir))
    process.waitForProcessOutput((Appendable)System.out, System.err)
    if (process.exitValue() != 0) {
        throw new RuntimeException("Command ${command} was completed with error code ${process.exitValue()}")
    }
}
