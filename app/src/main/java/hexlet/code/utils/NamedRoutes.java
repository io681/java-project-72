package hexlet.code.utils;

public class NamedRoutes {
    public static String rootPath() {
        return "/";
    }

    public static String allUrlsPath() {
        return "/urls";
    }

    public static String showUrlById(String id) {
        return "/urls/" + id;
    }
    public static String showUrlById(Long id) {
        return "/urls/" + id;
    }

    public static String checkPathById(String id) {
        return "/urls/" + id + "/checks";
    }

    public static String checkPathById(Long id) {
        return "/urls/" + id + "/checks";
    }
}
