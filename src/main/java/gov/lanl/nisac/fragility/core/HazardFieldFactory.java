package gov.lanl.nisac.fragility.core;

public class HazardFieldFactory {

    public HazardField getHazardField(String fileName) {

        String extension = null;
        int idx = fileName.lastIndexOf(".");

        if (idx > 0) {
            extension = fileName.substring(idx + 1);

        } else {
            System.out.println("No file extension");
            System.exit(1);
        }

        // Factory
        if (extension.equalsIgnoreCase("asc")) {
            return new HazardAsc(fileName);
        } else if (extension.equalsIgnoreCase("tif")) {
            return new HazardTif(fileName);
        } else {
            System.out.println("Cannot recognize file extension");
            System.exit(1);
        }
        return null;
    }
}
