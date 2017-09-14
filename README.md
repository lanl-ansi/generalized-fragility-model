# turbo-fresh-gfm

This is a new version of micot-general-fragility.

#### Assumptions

(1) asset input data must follow the GeoJSON specification
- https://tools.ietf.org/html/rfc7946
- assign all values in properties



### Example usage 

For multiple values use a ";" as a separator.

Wind and Ice Fields:

    java -jar Fragility.jar -a < file.json > -hf <hazard1.asc;hazard2.asc> -i "wind;ice" -e "windIce"

    ............................................
    -a    asset data 
  
    -hf   hazard field files
  
    -i    identifiers
  
    -e    estimator identifier
  
    -o    output file name (optional) - defaults to "fragility_output.json"
  
#### RDT Input

This option takes in RDT data and approximates poles locations along distribution lines.

java -jar Fragility.jar -r 
    
