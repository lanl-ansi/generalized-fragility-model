# Response Estimator Tutorial 

This tutorial will mirror a methodology and data described from the HAZUS
Earthquake Modeling technical manual.  The technical manual can be found
[here](https://www.fema.gov/media-library/assets/documents/24609).

Federal Emergency Management Agency [FEMA] 
(2015) Hazus-MH—MH 2.1 earthquake model technical manual. 
FEMA, Mitigation Division, Washington, DC, Unites States. 



## Building Fragility curves

We will follow the example given in section 5.4.3 (p.15-40). 

Here we are interested in the maximum relative displacement - the max ground displacement 
as a function of time.

### Asset Data

The example asset data is a FeatureCollection of buildings, with extracted property values from 
the HAZUS manual. The properties of interest and members names in this dataset:

* unique identifier **"id"**

The identifier can be any string value, as long as it's unique. For this tutorial we'll
use the prefix "b-", i.e. "b-0", "b-1", etc. 

* building type **"buildType"**

For the purpose of this example, we will use three different building types:
C1M (concrete moment frame, moderate code), 
S2M (Steel braced frame moderate), 
W1M (wood light frame, moderate)

* mean spectral displacement **"MSD"**

The MSD values are obtained from table 5.9a (p. 15-48), extensive displacement.

C1M - 9.0

S2M - 10.8

W1M - 5.04

* log-normal standard deviation **"LogNormStdDev"**

Median values are obtained from table 5.9a (p. 15-48), extensive displacement.

C1M - 0.68

S2M - 0.68

W1M - 0.85

The latitude and longitude values are randomly chosen, and example file is 
in test_data/inputs/example_buildings.json

> Building locations were randomly chosen and does not represent real building locations


### Hazard Field - Spectral Displacement

An example hazard field is provided in test_data/fields/spectralField_example.asc
> Spectral displacement field was randomly generated and doesn;t represent real data!

<img src="https://github.com/tscrawford/turbo-fresh-gfm/blob/master/test_data/hazardField.PNG" width="400" height="400" />

### Response Estimation 

#### ResponseEstimatorTemplate description

This class implements the Response Estimator interface and already has a predefined method 
"calcFragilty".  This is were your custom-made compute routines should be implemented.

> NOTE: Do not modify the constructor or the writeResults method.

All exposures are contained with the line 
```java
Map<String, HashMap<String, ArrayList<Double>>> exposures = gfmBroker.getExposures();
```
For convenience a field variable "responses" has been defined as the structure to store
you response results 
```java 
responses = new HashMap<>(); 
```
This allows responce calculations to be stored in a hash map: 
String identifier -> Double 

From this point on, you are free to do whatever you like.


#### Steps for example ResponseEstimatorSpectralDisplacement

Using ResponseEstimatorTemplate.java (inside responseEstimators package):
 
1. change the file name to "ResponseEstimatorSpectralDisplacement.java".  This java class will serve 
as the entry point for all fragility calculations with example data.

2. Let's call this example estimator "eqdisplacement" and paste the following code into ResponseEstimatorFactory.java
```java
else if (estimatorId.equalsIgnoreCase("eqdisplacement")) {
            return new ResponseEstimatorSpectralDisplacement(broker, fileOutputPath);
        }
```
This basically registers your new class to be specified from command line options ``` -e eqdisplacement ```.

3. below the ```responses``` declaration, paste the following code
```java
double failure = 0.0;

        /*
         ********  Calculate fragility here ********
         */
        for (JsonNode n : assets) {

            String id = n.get("id").asText();
            Double exposureValue = exposures.get("eqd").get(id).get(0);

            // store responses
            responses.put(id, failure);
        }
```
Now you should be able to build and run this example using the following

``` 
-a test_data/inputs/example_buildings.json -i eqd -e eqdisplacement -hf test_data/fields/spectralField_example.asc -o responses.json 
```
This will print out responses of 0.0 for all building identifiers in responses.json

Notice option ``` -i eqd ```,  this identifies the hazard field from ``` exposures ``` hash map.

``` exposureValue ``` isn't used yet, but this variable stores the hazard exposure value from spectralField_example.asc,
based on building spatial coordinates.


4. Add function calculation for the probability of being in or exceeding a given damage state,
modeled as a cumulative lognormal distribution.  This implementation is covered in the HAZUS
manual, page 15-40:

<img src="https://github.com/tscrawford/turbo-fresh-gfm/blob/master/test_data/equation.PNG"/>


```java 

        double failure = 0.0;
        NormalDistribution nd = null;

        /*
         ********  Calculate fragility here ********
         */
        for (JsonNode n : assets) {

            // getting asset identifier
            String id = n.get("id").asText();
            // getting median spectral value
            Double msd = n.get("MSD").asDouble();
            // getting standard deviation of spectral displacement
            Double stdDev = n.get("LogNormStdDev").asDouble();
            // gettting spectral displacement exposure
            Double exposureValue = exposures.get("eqd").get(id).get(0);

            // conditional probability of being in, or exceeding, a particular damage state,
            // given the spectral displacement
            Double dv = (1.0/stdDev)*Math.log(exposureValue/msd);
            nd = new NormalDistribution();
            failure = nd.cumulativeProbability(dv);

            // store responses
            responses.put(id, failure);
        }
```
 
Done.
