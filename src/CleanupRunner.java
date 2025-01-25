public class CleanupRunner {
    public static void main(String[] args) {
        // Register shutdown hook to clean up .class files
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cleaning up .class files...");
            cleanupClassFiles();
        }));

        // Your main program logic goes here
        System.out.println("Program running...");
    }

    private static void cleanupClassFiles() {
        try {
            java.io.File currentDir = new java.io.File(".");
            java.io.File[] files = currentDir.listFiles();
            
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.getName().endsWith(".class")) {
                        if (file.delete()) {
                            System.out.println("Deleted: " + file.getName());
                        } else {
                            System.out.println("Failed to delete: " + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
} 