# Vehicle Survey

A coding challenge for Aconex. Written for Java 8.

## Instructions

* `./gradlew build`
* `java -jar build/libs/vehicle-survey.jar sampledata.txt`

You may also specify a start time, if you know it, to display output data with accurate timing, for example:

* `java -jar build/libs/vehicle-survey.jar sampledata.txt 2018-09-11`

## Explanation

I chose this challenge because it seemed both more interesting and more challenging than the alternative. (I'd also seen such sensors
on roads and wondered how they'd worked...)

The software works in a simple way. No complex patterns are used, no attempts at optimization have been made, and no attempts at error
handling are in place beyond a global exception catch. But I think it fits its purpose. Here are the basic steps:

1. Validate user input (ensure file exists and if present, start time is formatted correctly)
2. Read sensor data from file
3. Interpret sensor data into a list of vehicles
4. Query a Survey class to get some sample data from the vehicles and print it to stdout

Here are some potential pitfalls, or areas for improvement:

* It's totally unoptimized. In particular, the Survey class iterates over the whole set of vehicles with each call. If we knew exactly
what data was requested, we could iterate over the vehicles just once, generating all the data a single time.
* It would probably choke on larger data sets. A time-series database would be a better choice for this sort of thing.
* Querying a new statistic (e.g. fastest vehicle on a certain day) requires editing Java code. It would be cool to support an actual
query language.
* There is a bug I've discovered that can occur if a car travels northbound during the signals of a southbound car. I have written a test
in VehicleFactoryTest.java that reproduces it - an exception is thrown. I think I could resolve this with a stack and perhaps some fuzzy
logic to figure out what we expect a vehicle to look like.