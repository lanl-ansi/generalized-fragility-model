package gov.lanl.nisac.fragility.GFMcore;

public class HazardFieldFactory {

    public HazardField getHazardField(String fileName, GFMEngine broker) {

        String extension = null;
        int idx = fileName.lastIndexOf(".");

        if (idx > 0 ) {
            extension = fileName.substring(idx + 1);

        }
        else {
            System.out.println("No file extension");
            System.exit(1);
        }

        // Factory
        if (extension.equalsIgnoreCase("ASC")) {
            return new HazardAsc(fileName, broker);
        } else if (extension.equalsIgnoreCase("tif")) {
            return new HazardTif(fileName, broker);
        }
        else{
            System.out.println("Cannot recognize file extension");
            System.exit(1);
        }
        return null;
    }
}
