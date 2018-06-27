# Power Pole Wind (and Ice) Stress Fragility Module

This model evaluates the probability of a pole failure by comparing the pole strength with the total tensile stress
τ, on the pole where the power and communications cables are anchored.  The model assumes a standard normal cumulative
distribution of pole strength.

The ice model assumes ice accumulation on lines (ignores accumulation on poles, cross-arms, and conductors).

The model is based off Yingshi Sa's Thesis:

Sa, Y. (2002). Reliability analysis of electric distribution lines (Doctoral dissertation, McGill University Libraries).



## Model Formulation

Quick outline of pole fragility:

probability of failure = the cumulative normal distribution of the pole strength (given mean and standard deviations) evaluated at the pole tensile stress

poleTensileStress = polePowerCableTensileStress + poleCommCableTensileStress – compressiveStress

compressiveStress = totalDeadLoad / baseDiameter

totalDeadLoad = poleWeight + powerCableWeight + commCableWeight

poleWeight = poleMass x G = G x PI x baseDiameter^2 x poleHeight x woodDensity / 4

cableWeight = G x PI x baseDiameter^2 x numCables x span / 4

poleCableTensileStress = cableWindMoment / (PI  x baseDiameter^3 / 32)

cableWindMoment = cableWindForce x cableAttachmentHeight

cableWindForce = cableArea x windDynamicPressure

windDynamicPressure = 0.5 x airDensity x sqrt(windSpeed)

cableArea = numCables x cableDiameter x span
