package javache.enums;

public enum Folder {

    ASSETS("/assets"),
    PAGES("/pages");

    private final String folder;

    Folder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
