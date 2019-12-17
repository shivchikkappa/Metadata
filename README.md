# Metadata
MicroService to store the metadata for datahub. Different types of Metadata are as follows

1. SourceData: Used to capture the source information like host, port, type and directory path to read the file. Common 
               source types would be FTP, SFTP, S3
2. RawData: Used to capture the details of the row headers of the csv file. 
3. TargetData: use to capture the target information like host, port, type and diretory to post the data.

For more detailed information of the model object, please refer to the swagger API documentation at http://3.133.99.71:9080/swagger-ui.html#/

# Technologies
JDK 1.8.0

ElasticSearch 7.0.0

Springboot 2.2.2

Maven 3.6.1

Git 2.14.1

Swagger 2.9.2

AWS EC2 instances for deployment.

# Build, Run and test application
Clone the repo from github

cd Metadata

mvn clean install

Start ElasticSearch on default port 9020.

java -jar target/CustomerMetadata-0.0.1-SNAPSHOT.jar

Application will start running on port 9080

Application admin port is configured on port 9085

# API documentation and invoking REST API
Access the Swagger URI at http://3.133.99.71:9080/swagger-ui.html#/ 

Select on raw-metadata-resource or search-metadata-resource or source-metadata-resource or target-metadata-resource

Select the HTTP request method

Click on "Try it out" to fill in the request body for POST methods and Execute the request

GET /company/{company}/customer/metadata/{id} -- API to fetch individual source/raw/target metadata

use "cupid" for company and "C9192009C8BD4E5585B184C0DD9C9425" for id value to fetch the source metadata.

use "cupid" for company and "479BA348C4F94D7CAFC39A197D779919" for id value to fetch the target metadata.

use "cupid" for company and "3A40E5B807C44A6A9CBD968FBA6BD00C" for id value to fetch the raw metadata.

GET /company/{company}/customer/metadata/source/{id} -- API to fetch all the raw data associated with source

use "cupid" for company and "C9192009C8BD4E5585B184C0DD9C9425" for source id value 

# Springboot metrics using actuator
Springboot admin is configured to run on port 9085. 

Access http://3.133.99.71:9085/actuator to access all the management end points

Simple HealthCheck is implemented at http://3.133.99.71:9085/actuator/health to veriy application is running and can connect to ElasticSearch
