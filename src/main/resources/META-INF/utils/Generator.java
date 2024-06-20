import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

class Generator {

    private Path projectPath = Paths.get(request.outputDirectory, request.artifactId)
    private Path pomFile = Paths.get(request.outputDirectory, request.artifactId, "pom.xml")
    private Path dbModuleDirectoryPath = projectPath.resolve(request.artifactId + "-db")
    private Path clientModuleDirectoryPath = projectPath.resolve(request.artifactId + "-client")
    private Properties properties = request.properties
    private String includeDBProperty = properties.get("includeDb")
    private String includeClientProperty = properties.get("includeClient")

    public static void main(String[] args) {
        generateMicroservice(request.artifactId, projectPath.toString(), properties.get("revision"))
    }

    public void generateMicroservice(String serviceName, String packageName, String version) {
        // Создание структуры директорий
        createDirectoryStructure(serviceName);

        // Генерация файлов pom.xml
        generatePomFiles(serviceName, packageName, version);

        // Генерация Java-файлов
        generateJavaFiles(serviceName, packageName);

        // Генерация конфигурационных файлов
        generateConfigFiles(serviceName);

        // Форматирование сгенерированного кода
        formatGeneratedCode(serviceName);

        System.out.println(String.format("Микросервис %s успешно сгенерирован", serviceName));
    }

    private void createDirectoryStructure(String serviceName) {
        String baseDir = generatorConfig.getOutputDir() + "/" + serviceName;
        String apiDir = baseDir + "/" + serviceName + "-api/src/main/java";
        String clientDir = baseDir + "/" + serviceName + "-client/src/main/java";
        String dbDir = baseDir + "/" + serviceName + "-db/src/main/java";
        String sharedDir = baseDir + "/" + serviceName + "-shared/src/main/java";
        String starterDir = baseDir + "/" + serviceName + "-starter/src/main/java";

        try {
            Files.createDirectories(Paths.get(apiDir));
            Files.createDirectories(Paths.get(clientDir));
            Files.createDirectories(Paths.get(dbDir));
            Files.createDirectories(Paths.get(sharedDir));
            Files.createDirectories(Paths.get(starterDir));
        } catch (IOException e) {
            log.error("Ошибка при создании структуры директорий для микросервиса {}", serviceName, e);
            throw new RuntimeException("Ошибка при создании структуры директорий", e);
        }
    }

    private void generatePomFiles(String serviceName, String packageName, String version) {
        String pomTemplate = generatorConfig.getTemplateDir() + "/pom.xml.vm";
        String apiPomFile = generatorConfig.getOutputDir() + "/" + serviceName + "/" + serviceName + "-api/pom.xml";
        String clientPomFile = generatorConfig.getOutputDir() + "/" + serviceName + "/" + serviceName + "-client/pom.xml";
        String dbPomFile = generatorConfig.getOutputDir() + "/" + serviceName + "/" + serviceName + "-db/pom.xml";
        String sharedPomFile = generatorConfig.getOutputDir() + "/" + serviceName + "/" + serviceName + "-shared/pom.xml";
        String starterPomFile = generatorConfig.getOutputDir() + "/" + serviceName + "/" + serviceName + "-starter/pom.xml";

        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init();

            Template template = velocityEngine.getTemplate(pomTemplate);

            VelocityContext context = new VelocityContext();
            context.put("serviceName", serviceName);
            context.put("packageName", packageName);
            context.put("version", version);

            // Генерация файлов pom.xml для каждого модуля
            generatePomFile(template, context, apiPomFile);
            generatePomFile(template, context, clientPomFile);
            generatePomFile(template, context, dbPomFile);
            generatePomFile(template, context, sharedPomFile);
            generatePomFile(template, context, starterPomFile);
        } catch (Exception e) {
            log.error("Ошибка при генерации файлов pom.xml для микросервиса {}", serviceName, e);
            throw new RuntimeException("Ошибка при генерации файлов pom.xml", e);
        }
    }

    private void generatePomFile(Template template, VelocityContext context, String pomFile) throws IOException {
        try (Writer writer = new FileWriter(pomFile)) {
            template.merge(context, writer);
        }
    }

    public void generateJavaFiles(String serviceName, String packageName) throws IOException {
        String directoryPath = "src/main/java/" + packageName.replace('.', '/');
        Files.createDirectories(Paths.get(directoryPath));

        String className = serviceName + "Service";
        String javaFilePath = directoryPath + "/" + className + ".java";
        String javaFileContent = "package " + packageName + ";\n\n" +
                "public class " + className + " {\n" +
                "    // TODO: Implement service methods\n" +
                "}";

        Files.write(Paths.get(javaFilePath), javaFileContent.getBytes());
        System.out.println("Generated Java file: " + javaFilePath);
    }

    public void generateConfigFiles(String serviceName) throws IOException {
        String directoryPath = "src/main/resources/config";
        Files.createDirectories(Paths.get(directoryPath));

        String configFilePath = directoryPath + "/" + serviceName + ".properties";
        String configFileContent = "service.name=" + serviceName + "\n" +
                "service.description=Configuration for " + serviceName;

        Files.write(Paths.get(configFilePath), configFileContent.getBytes());
        System.out.println("Generated config file: " + configFilePath);
    }

    public void formatGeneratedCode() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mvn", "formatter:format");
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("Formatted generated code.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
