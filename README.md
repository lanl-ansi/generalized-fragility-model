# Generalized Fragility Model (GFM)

[![Build Status](https://travis-ci.org/lanl-ansi/generalized-fragility-model.svg?branch=master)](https://travis-ci.org/lanl-ansi/generalized-fragility-model)

[![codecov](https://codecov.io/gh/lanl-ansi/generalized-fragility-model/branch/master/graph/badge.svg)](https://codecov.io/gh/lanl-ansi/generalized-fragility-model)


This is a rewrite of [micot-general-fragility](https://github.com/lanl-ansi/micot-general-fragility).

The Generalized Fragility Model (GFM) is an extensible software tool 
that provides a framework and template for modelers to easily write customized fragility 
routines using predefined software components.  This code is provided under a BSD license as part of the
Multi-Infrastructure Control and Optimization Toolkit (MICOT) project, LA-CC-13-108.

GFM basically accepts a set of geographic raster fields for each hazard quantity and a collection of assets, 
which are then exposed to those hazard fields by spatial location.  It then provides a data structure for users 
to create their own custom-made response responseEstimator routines.

<img src="https://github.com/lanl-ansi/generalized-fragility-model/blob/master/test_data/dataFlow.PNG" width="400" height="200" />

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
```git clone https://github.com/tscrawford/turbo-fresh-gfm.git```
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
GFM expects that all asset attributes/data are defined in a Feature using the 
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
 }
```

### Options 

For multiple values, use a space separator (see example below).
```     
        -a      asset data 
        -hf     hazard field input files
        -i      hazard identifiers
        -e      responseEstimator identifier
        -o      output file name (optional) - defaults to "fragility_output.json" 
 ```

##### Wind and Ice Fields Example:
``` java -jar Fragility.jar -a < AssetData.geojson > -hf <hazard1.asc> <hazard2.asc> -i wind ice -e windIce ```

### Schema 

Some of GFM's schema details can be found [here](schema.md)

 
### Data Assumptions
GFM uses the unique "id" member to track associated exposures values  from hazard field data.  For instance, if an 
asset is exposed to two hazard fields, GFM preserves input order using a hash table structure that associates unique 
identifiers to an array of exposure values - based on the input order.

``` java -jar Fragility.jar -a < AssetData.geojson > -hf <hazard1.asc hazard2.asc> -i wind ice  -e PowerPoleWindIceStress ```

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
exposure values are in the order of first and last coordinates; as specified in the GeoJSON's geometry coordinates. 


#### Options overview

```-hf``` This tells fragility where your hazard raster data is located 
```-i ``` This is the identifier that fragility routines use to extract exposure values from the general hash map structure
```-e ``` Specifies the responseEstimator routine by exact class name
```-o ``` output file path
```-a ``` Asset data file location 


# Customized Response Estimators

Customizing your own response responseEstimator routines is outlined in the following steps:

-- Using _ResponseEstimateTemplate.java_, create new routine (located in _test_data_ directory)
* create new response responseEstimator class
* class name is used to uniquely identify routine from command line input

-- Develop fragility routine inside public method _calcFragility_.  This method serves as the anchor point for accessing
assets and exposure values.

-- Ensure your new fragility class lives inside package: gov.lanl.micot.application.fragility.responseEstimators 
* Again, implement your routine by using the ```-e``` option, using exact class name.

-- If desired, you you can overwrite the ``` writeResults``` method in ResponseEstimator for your own purposes.  By 
default, response outputs are JSON formatted.


## Tutorial

Here's a tutorial that provides some steps on how to create new fragility routine [here](tutorial.md).


## MICOT Resilience Design Tool (RDT)

Fragility options that are aligned with MICOT RDT tool [here](rdt.md)


# Future Work
In no particular order:

* Incorporate hazard fields in Esri shapefile format
* For Symmetric hazard fields, allow GFM to read in a look-up-table
* Add new (generic) fragility routines: earthquakes, blast overpressure, etc.
 

