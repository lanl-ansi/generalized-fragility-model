package gov.lanl.micot.application.fragility.io;

import gov.lanl.micot.application.fragility.core.GeometryObject;
import gov.lanl.micot.application.utilities.json.AssetDataFromJackson;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import gov.lanl.micot.application.utilities.gis.RasterField;
import gov.lanl.micot.application.utilities.json.JsonDataFromJackson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class provides methods to read formatted files and stores them in local field variables.
 *
 * @author Trevor Crawford
 */
public class GFMDataReader {

    private List<Map<String, PropertyData>> properties = new ArrayList<>();
    private ArrayList<GeometryObject> geometryObjects = new ArrayList<>();
    private ArrayList<HazardField> hazardFields = new ArrayList<>();

    /**
     * Default constructor.
     */
    public GFMDataReader() {
    }

    /**
     * Method for accessing an array list of <tt>GeometryObjects</tt>.
     *
     * @return array list of GeometryObjects
     */
    public ArrayList<GeometryObject> getGeometryObjects() {
        return this.geometryObjects;
    }

    /**
     * Method for accessing an array list of property objects.
     *
     * @return list of asset data hashmaps
     */
    public List<Map<String, PropertyData>> getProperties() {
        return this.properties;
    }

    /**
     * Method that reads in a GeoJSON file by file location.
     *
     * @param FileLocation string formatted file location
     */
    public void readGeoJsonFile(String FileLocation) {

        AssetDataFromJackson jsonData = new JsonDataFromJackson();
        jsonData.readGeoJsonFile(FileLocation);
        geometryObjects = jsonData.getGeometryObjects();
        properties = jsonData.getPropertyObjects();
    }


    /**
     * @param fileName String array of file locations
     * @param id       string array of identifiers in tandem with <tt>fileName<tt/>
     * @return an array list of type <tt>HazardField<tt/>
     */
    public ArrayList<HazardField> readHazardFile(String[] fileName, String[] id) {

        int numberFiles = (fileName == null) ? 0 : fileName.length;
        int numberIdentifiers = (id == null) ? 0 : id.length;

        if (numberFiles != numberIdentifiers) {
            System.out.println("EXITING:  Number of files and identifiers aren't equal");
            System.exit(2);
        }

        for (int i = 0; i < numberFiles; i++) {
            RasterField hf = new RasterField(fileName[i], id[i]);
            this.hazardFields.add(hf);
        }

        return this.hazardFields;
    }

}
