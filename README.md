# Metadata
Metadata in data integration hub represents all of the "Control" data for the DIH and overall platform function sets. Each data stream processor, 
platform service, or platform function acts as producer/consumer client of the DIH Metadata API. The Metadata API would be implemented 
as a simple micro-service with a document based data store to allow for quick, easy, and flexible development requirements.

Any document storage mechanism should be able to handle the minimum requirements for this service, using Elasticsearch as 
the data store, beyond a simple document storage server, it offers additional indexing, searching/query, and analytic capabilities 
to the consuming service. These functions may not be required currently for the Metadata API function, 
but can easily be added in the future if built on top of Elasticsearch, rather than just a document store like MongoDB.

With an estimated size of about 500 - 1024 bytes per document with an average of 10,000 documents per company, 
the max storage requirement for each company would approximately be 5MB - 10MB of data per company. The amount of data storage 
required per company is negligible in terms of scalability, so a single cluster should be able to support the entire 
data set for the entire platform.

Different types of Metadata are as follows

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

AWS EC2 instances for deployment. t2.micro for springboot microservice and t2.large for ElasticSearch

# Build, Run and test application
Clone the repo from github

cd Metadata

mvn clean install

Start ElasticSearch on default ports or configure the spring.data.elasticsearch.* in application.properties file for custom configurations

java -jar target/CustomerMetadata-0.0.1-SNAPSHOT.jar

Application will start running on port 9080

Application admin port is configured on port 9085

# API documentation and invoking REST API

Two options to test the application deployed on AWS

1. Access the shared PostMan work space using link https://www.getpostman.com/collections/33084858293e163ded55 

2. Access the Swagger URI at http://3.133.99.71:9080/swagger-ui.html#/ 

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
