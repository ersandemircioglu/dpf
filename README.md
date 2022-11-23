# Data Processing Framework (DPF)

DPF is a platform which provides a common way to process data from multiple Earth Observation (EO) missions. To be able to provide multi mission support, following points should be taken under consideration: 

- **Data Frequency and Load**: The data frequency of an EO mission is the depends on the orbit of the satellite(s) and the location of the ground station(s). Lets assume that there is one ground station located in mid latitudes. 
  - *GEO and MEO satellites*: the satellite is almost always visibly by the ground station and can provide data continuously. The load on data processing system can be estimated and dynamic scalability mostly not needed. 
  - *LEO satellites*: the satellite visibility is limited and mostly it has 2 or 3 passes over the ground station each takes around 10-20 minutes. The satellite has to dump all its data during the pass and the data must be processed in a predefined time window (timeliness). In order to use computational resources efficiently, dynamic scalability of the system is important.  
  - *Reprocessing of the data*: for some cases, like validation of a new processing algorithm, calibration or quality assessment of products, reprocessing of data is needed. The processing system has to handle vast amount of data in short time. 
- **Variance of input data**: Mainly because of instrument differences, different missions produce different products. The processing system should be able to accept different input types, while providing a common experience to the user of the system.
- **Variance of data processing applications**: Some missions use a set of applications which are designed only for that mission. These applications are mostly static and HW/SW dependencies are not frequently changed. However some missions use Commercially available off-the-shelf (COTS) products. These applications are subject to regular updates and sometimes their HW/SW dependencies are changed.    

## Architectural Decisions

The architecture of the system is the mixture of following three architectural styles.

- Microservices
- Event-driven
- Big Data (HPC) 

Before explaining the Architectural Decisions, it is necessary to declare the decomposition levels of the system:

```mermaid
flowchart LR
  subgraph System
    subgraph Module 1
       subgraph Component 1
       end
       subgraph Component ...
       end
       subgraph Component n
       end
    end
    subgraph Module ...
       subgraph Component 1
       end
       subgraph Component ...
       end
       subgraph Component n
       end
    end
    subgraph Module n
       subgraph Component 1
       end
       subgraph Component ...
       end
       subgraph Component n
       end
    end
  end
```

### Microservices Architecture

Components of the system shall be designed based on following  aspects of the Microservice architecture.

- Each component implements a single functionality
- Each component is loosely coupled and communication between components are provided by queues (asynchronous) and REST APIs (synchronous). 
- Each component can be developed and deployed without coordination.  

### Event-driven Architecture

Event-driven architecture has three main components, namely, event producers, event queue and event consumers. Event producers generates events and push them into the event queue. Event consumers listen the event queue and when an event is received, they process the event. Producers are decoupled from consumers and consumers are also decoupled from each other. In addition to the decoupling of the components, event driven architecture provides buffering of event and when needed enable scaling of the processing. 

Communication between modules and components is designed based on following principals: 

- Data flows in one direction. 
  - An event producer fires an event and doesn't wait any response.
  - Event consumers are unaware about event producers, each others and rest of the system. 
- An event consumer registers itself to an event type. When the expected event is fired, it consumes the event and produces another event to notify rest of the system. 
- Event queues are based on producer-consumer pattern, an event is processed only by a consumer.  

### Big Data Architecture

Big data solutions are based on following principals 

- Tasks must be finite and independent. A work can be split into tasks. They can run in parallel across many cores. 
- The system doesn't need to stay up all the time. in case of load system can can provision cores as needed to do work, and then release them.

The "Process Management" module of the system is based on Big Data Architecture principals 
- Each processing application must be finite. This means, an application takes the input, processes it and produces the output. After the production is completed, it exits gracefully. 
- To be able to provide parallelism, there should be no dependency between processing applications. If there is a coupling between applications, they have to be bundled under same component. From the system level, they must be seen as a single component. 

## Design Decisions

### NoSQL Database
The input data must be catalogued in a common structure which can be queried by processing applications. For a single mission system, all data types are know and can be stored in a Relational DB system (RDBMS). However for a multi mission, it is hard to create a common data structure in order to support all missions. Adding a new mission to the system requires update on DB tables level. Also different missions have different criteria for querying data from the catalogue. 

A document-oriented NoSQL databases can solve this issue. In a Document-oriented NoSQL database, records are stored in documents which are encoded in some standard format like JSON or XML. Each mission can decide its own property set to define an input data and query it as needed. During the ingestion phase, the properties of the input data extracted as defined by the mission and stored in the catalogue. A mission doesn't need to know others data structure and can implement processing application independently from others. This also applicable for the common auxiliary data. For example TLE is commonly used by almost all mission to estimate the satellites orbit. Each mission can create it own record in the catalogue. Ingestion module can create a record for each system while storing only one copy of the data in the Archive. 

### Orchestration


### Centralized Monitoring


## Technologies used

Following technologies are used during the development of the prototype

- **Java and Spring Boot**: are the programming language and framework to implement the modules of the system.  
- **Apache CouchDB**: is a document-oriented NoSQL database. CouchDB provides a REST API for inserting and searching records. Thus, it is used as a Catalogue Module of the system. Another document-oriented NoSQL database could be also used instead of CouchDB but in this case, a REST API might be needed.     
- **RabbitMQ**: is the  queue/communication layer between modules. 
- **The ELK Stack**: is used to capture, review and query logs generated by modules.
- **Docker**: is used to containerize the components and the processing applications. In a final system, Kubernetes would be a better option for the container management, but in this prototype, Docker's Java API is used for the container management.    

## System Design 

### Level 0

The DPF system is composed of following modules:
- **Ingestion**: The ingestion module converts the unstructured input data into a structured format that can be processed by processors.
- **Catalogue & Archive**: 
- **Processing**:
- **Configuration**:
- **Monitor & Control**:

![Level 0](/docs/level_0_system.svg)

### Level 1
#### Ingestion

![Level 1 - Ingestion](/docs/level_1_ingestion.svg)

- **File Reception**
- **Ingestor**
  - *Ingestor Configuration*
  - *Filename parser*
  - *Metadata parser*

#### Ingestion

![Level 1 - Processing](/docs/level_1_processing.svg)

- **Process Manager**
  - *Processor Definitions*
- **Processor Pool**
  - *Containers*
  - *HTC*
  - *Stream Processors*


## Open Points

- [ ] If the MQ goes down, all components fail. A fallback mechanism is needed when the MQ goes down .
  - https://stackoverflow.com/questions/58110868/fallback-mechanism-when-rabbitmq-goes-down 