# turbo-fresh-gfm

This is a new rewrite of [micot-general-fragility](https://github.com/lanl-ansi/micot-general-fragility).

The Generalized Fragility Model (GFM) is an extensible software tool 
that provides a framework for modelers to easily write customized fragility 
routines using predefined software components.  GFM basically accepts a set of geographic 
raster fields for each hazard quantity and a collection of assets, which are then exposed to those
hazard fields by spatial location.  It then provides an interface for users to create their own
custom-made response estimator routines. 

<img src="https://github.com/tscrawford/turbo-fresh-gfm/blob/master/test_data/dataFlow.PNG" width="400" height="200" />

The remainder of this document will cover the following:

* Installation overview
* Example use
* Data input, options, schema
* Customized response estimators
* Resilient Design Tool implementation 
* Tutorial 
* Future Work

# Installation
## Maven  Installation 

GFM is distributed as a Maven project:
1. Download [Apache Maven](https://maven.apache.org/download.cgi) into a directory
2. Update PATH variable to point to Maven's bin directory
3. Download and install [Java 8 (or later)](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
4. Update JAVA_HOME to point at directory of your Java installation.
5. Install a git tool ([download](https://git-scm.com/downloads), apt-get, etc.)
6. Download GFM repository using the following git command: 
``` git clone https://github.com/tscrawford/turbo-fresh-gfm.git```
7. Build and package code into an executable file using ```mvn -Dmaven.test.skip=true package```

### LANL Only
If you are behind LANL's firewall, Maven needs to know the location of the proxy server.
In ${USER_HOME_DIR}/.m2/settings.xml, add the following (create settings.xml if it does not exist)
```xml
<?xml version="1.0"?>
<settings xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns="http://maven.apache.org/SETTINGS/1.0.0">
<proxies>
<proxy>
<id>lanlproxy</id>
<active>true</active>
<protocol>http</protocol>
<host>proxyout.lanl.gov</host>
<port>8080</port>
</proxy>
</proxies>
</settings>

```

# Data Input and Options

For asset inputs, GFM follows the [GeoJSON](https://tools.ietf.org/html/rfc7946) format, which is a data interchange format 
based on JavaScript Object Notation (JSON).  The current implementation uses "Point" and "LineString" feature objects.  
Future implementations are described later in this document. 

## Asset Data Input
GFM expects that all asset attributes/data are defined in a Feature using a 
"properties" member. At minimum, properties must have an unique "id" member specified.

For example:

```json
 {
       "type": "FeatureCollection",
       "features": [{
           "type": "Feature",
           "geometry": {
               "type": "Point",
               "coordinates": [-72.65509, 41.74908]
           },
           "properties": {
               "id": "UniqueId"
               "prop0": "value0",
               "prop1": "value1",
               .
               .
               .
           }
       }
       .
       .
       .
```

### Options 

For multiple values, use a space separator.

### Schema 

GFM's schema details can be found [here](schema.md)

##### Wind and Ice Fields Example:
``` 
java -jar Fragility.jar -a < AssetData.geojson > -hf <hazard1.asc> <hazard2.asc> -i wind ice -e windIce
    
        ...........................................................................................................
        -a      asset data 
        -hf     hazard field input files
        -i      hazard identifiers
        -e      estimator identifier
        -o      output file name (optional) - defaults to "fragility_output.json" 
 ```
 
### Data Assumptions
GFM uses the unique "id" member to track associated exposures values 
from hazard field data.  For instance, if an asset is exposed to two hazard fields, GFM preserves input order 
using a hash table structure that associates unique identifiers to an array of exposure values - based on the input
order.

``` java -jar Fragility.jar -a < AssetData.geojson > -hf <hazard1.asc hazard2.asc> -i "wind;ice" -e "windIce" ```

From the above command line arguments, Point type assets have exposures assigned as follows:

| hazard field key | identifier key | list of array values |
| ---  | --- | --- |
| hazard 1 | id0 | { ExposureValue[0] } |
| hazard 1 | id1 | { ExposureValue[0] } |
| ... | ... | ... |
| hazard 2 | id0 | { ExposureValue[0] } |
| hazard 2 | id1 | { ExposureValue[0] } |

LineString types can hold multiple exposure values, and look something like this:

| hazardField key | identifier key | list of array values |
| ---  | --- | --- |
| hazard 1 | id0 | { ExposureValue[0], ExposureValue[1], ExposureValue[2] } |
| hazard 1 | id1 | { ExposureValue[0], ExposureValue[1] } |
| ... | ... | ... |
| hazard 2 | id0 | { ExposureValue[0], ExposureValue[1], ExposureValue[2] } |
| hazard 2 | id1 | { ExposureValue[0], ExposureValue[1] } |


Notice how the exposure values are in the same order as specified with the -hf option.  For LineStrings,
exposure values are in the order of first and last coordinates.



#### Options overview

```-hf``` This tells fragility where your hazard raster data is located 

# Customized Response Estimators

Customizing your own response estimator routines is outlined in the following steps:

1. Using _ResponseEstimateTemplate.java_, create new routine
2. Update _ResponseEstimatorFactory.java_ (gov.lanl.nisac.core)
* to include your new response estimator class and unique string identifier for command line
input


## Tutorial

Tutorial is [here](tutorial.md).



# RDT Input descriptions


This option takes in RDT data and approximates poles locations along distribution lines.
    
```
-r      RDT proccessing option
-ro     generated poles output path (optional)    
-num    number of scenarios to generate - default is one (optional)
-so     scenario block file output (optional)

```

Example:

``` 
java -jar Fragility.jar -r test_data/inputs/example_rdt.json -i wind -e wind -hf test_data/fields/windField_example.asc -ro RDT_Poles.json -o repsonses.json -so SCENARIOS.json -num 13 
```


# Future Work
In no particular order:

* incorporate hazard fields in Esri shapefile format
* add functionality for GeoJSON MultiPoints, polygons, MultiLineString, etc.

\alpha 


