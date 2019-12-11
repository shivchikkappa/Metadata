# Metadata
MicroService to store the metadata for datahub. Differnet types of Metadata are as follows

1. SourceData: Used to capture the source information like host, port, type and directory path to read the file. Common 
               source types would be FTP, SFTP, S3
2. RawData: Used to capture the details of the row headers of the csv file. 

For more detailed information of the model object, please refer to the swagger API documentation at http://ec2-3-133-99-71.us-east-2.compute.amazonaws.com:9080/swagger-ui.html#/

# Technologies
JDK 1.8.0

ElasticSearch 7.0.0

Springboot 2.2.2

Maven 3.6.1

Git 2.14.1

Swagger 2.9.2

AWS EC2

# Build, Run and test application
Clone the repo from github

cd Metadata

mvn clean install

Start ElasticSearch on default port 9020.

java -jar target/CustomerMetadata-0.0.1-SNAPSHOT.jar

Application will start running on port 9080

Application admin port is configured on port 9085


# API documentation and invoking REST API
Access the Swagger URI at http://ec2-3-133-99-71.us-east-2.compute.amazonaws.com:9080/swagger-ui.html#/ 

Click on raw-metadata-resource or search-metadata-resource or source-metadata-resource

Select the HTTP request method

Click on "Try it out" to fill in the request body for POST methods and Execute the request


Sample "Try it out" for Search GET method, use "cupid" for company and "" for id value to fetch the existing value.