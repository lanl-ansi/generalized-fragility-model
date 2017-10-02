# turbo-fresh-gfm

This is a new rewrite of [micot-general-fragility](https://github.com/lanl-ansi/micot-general-fragility).

The General Fragility Modeling (GFM) tool is an extensible software tool 
that provides a framework for modelers to easily write customized fragility 
routines using generalized software components.  GFM basically accepts a set of geographic 
raster fields for each hazard quantity and a collections of assets that are exposed to those
hazard fields.  It then uses custom-made response estimator routines to evaluate exposure 
response of each asset. 

The remainder of this document will cover the following:

* Installation overviews
* Quick/Example use
* Data input and options
* Customized response estimators
* Resilient Design Tool implementation 
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


GFM follows the [GeoJSON](https://tools.ietf.org/html/rfc7946) format, which is a data interchange format 
based on JavaScript Object Notation (JSON).  The current implementation uses only FeatureCollection objects 
"Point" and "LineString" objects.  Future implementations are described later in this document. 

## Asset Data Input
GFM expects that all asset attributes/data are defined in a Feature using a 
"properties" member. At minimum, properties must have an unique "id" member specified.

For example:

```
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

``` java -jar Fragility.jar -a < AssetData.geojson > -hf <hazard1.asc;hazard2.asc> -i "wind;ice" -e "windIce" ```

From the previous command line arguments, all asset identifiers have exposures assigned:

| key | array values |
| ---  | --- |
| id0 | {ExposureValue[0], ExposureValue[1]} |
| id1 | {ExposureValue[0], ExposureValue[1]} |


# Customized Response Estimators

TODO





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


