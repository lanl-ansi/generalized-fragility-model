
# MICOT Resilience Design Tool (RDT) Input descriptions


This module covers options that are aligned with the [Resilience Design Tool](https://github.com/lanl-ansi/micot) (RDT).
Currently, these options provide helper functions for reading RDT inputs and generating approximate power pole locations
along spatially detailed distribution lines.  When ```so``` is enabled, an RDT scenario block is generated.  If desired,
multiple scenarios are generated when combined with ``` num ``` option.

RDT input data examples can be found exploring JSON formatted files
 [here](https://github.com/lanl-ansi/micot/tree/master/application_data/lpnorm)

#### RDT specific options
    
```
-r      RDT proccessing option
-ro     generated poles output path (optional)    
-num    number of scenarios to generate - default is one (optional)
-so     scenario block file output (optional)

```

Examples:

Generate 13 scenarios - no asset (poles) input, only RDT
``` 
java -jar Fragility.jar -r test_data/inputs/example_rdt.json -i wind -e wind -hf test_data/fields/windField_example.asc -ro RDT_Poles.json -o repsonses.json -so SCENARIOS.json -num 13 
```

To generate RDT scenario block based on pole input data (using "lineId" member), with 15 scenarios:

```
java -jar Fragility.jar -a test_data/inputs/example_poles.json -i wind -e wind -so blockOUTPUT.json -num 15 -hf test_data/fields/windField_example.asc
```
> Notice ```-a``` option for pole inputs.
