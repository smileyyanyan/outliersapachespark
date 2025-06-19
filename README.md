### Outlier Analysis with Apache Spark KMeans  


This project calculates outliers from a list of project management data stored in csv using the KMeans algorithm with z-score thresholding. 

##### Analysis Outline

* Read the project management data from csv.
* Use Spark KMeans Clustering to create centers
* Assign each project to a center
* Calculate distance of each project to its assigned center
* Calculate mean and standard deviation for the distance
* Calculate z-score of each project 
* Define z-score threshold for outliers
* Filter for the outliers.


##### Java run configurations

--add-exports java.base/sun.nio.ch=ALL-UNNAMED
--add-exports java.base/java.nio.ch=ALL-UNNAMED
--add-opens=java.base/java.nio=ALL-UNNAMED
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED