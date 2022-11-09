# Data Processing Framework

## Scope

## System Requirments

### Functional



### Non Functional

- **SYS-NF-Scalibility-0010**: 
- **SYS-NF-Testibility-0010**: Each component of the system shall be able to indepently testable  
- **SYS-NF-Maintainability-0010**: 
## Architectural Decisions

### Event-driven architecture

- Data flows in one direction. 
- Event producers fire an event and don't wait any response.
- Event consumers are unaware about event producers and rest of the system. 
- An event consumer registers itself to an event type. When the expected event is fired, it consumes the event and produces another event to notify rest of the system. 
- event queues work based on producer-consumer pattern 

```mermaid
flowchart TB
```

### Microservices

- All components of the  

### Centralized Monitoring


## System Design 

**Legend**
| Shape       | Desciption |
|-------------|------------|
| Solid  line | Event flow |
| Dotted line | File  flow |

### Level 0

```mermaid
flowchart TB
  SRC[External Source]
  subgraph DPF
  ING[Ingestion]
  CAT[(Catalog)]
  ARC([Archive])
  TM[Task Manager]
  PROC[Processor Pool]
  DIS[Dissemination]
  CONF[Central Configuration]
  MC[Monitoring & Control]
  end
  DST[External Destination]
  
  SRC -.-> ING
  ING -.-> ARC
  ING --> CAT
  CAT --> TM
  CAT --> DIS
  TM --> PROC
  ARC -.-> PROC
  ARC -.-> DIS
  PROC -.-> ING
  DIS -..-> DST
```

### Level 1
### Ingestion
```mermaid
  flowchart TB
  SRC[External Source]
  subgraph Ingestion
  IN([Incoming Folder])
  FW[File Watcher]
  IQ[Ingestion Queue]
  FTD[(File Type Definitions)]
  IP[Ingester Pool]
  end
  CAT[(Catalog)]
  ARC([Archive])

  SRC -.-> IN
  IN --- FW
  FW --> IQ
  IQ --> IP
  FTD -.-> IP
  IP --> CAT
  IP -.-> ARC
```


##### Metadata extraction

Sample Scenario configuration

<instrument>_<satellite>_<station>_<creatation>_<product>_<level>_<extension1>.<extension2>

<instrument>_<creationday>_<creationtime>_<satellite>_<orbit>_<station>_<level>

<instrument>_<product_type>_<satellite>_<sensing_start>_<sensing_end>_<processing_time>_<orbit>_<station>_<level>.<extension>

INSTNAME_PRODUCTTYPE_SAT_SENSINGSTART_SENSINGEND_MODE_PRO
CTIME.bz2 

INSTNAME_YYYYMMDD_HHMI_SAT_ORBIT_STATION

FDR_L1C_HIRS3_NOAA15_20050908131002_20050908145109_R01.0.nc
AVHR_GAC_1B_N19_20170613222203Z_20170613222503Z_N_O_20170613234201Z.bz2
avhrr_20170510_005800_noaa19.hrp.bz2

AVHR_HRP_00_M01_20211019094900Z_20211019095000Z_N_O_20211019095106Z.bz2
hirs_20130318_1008_metopb_2581_mas.l1d.bz2\
W_XX-EUMETSAT-mon,amsua,DBNet+noaa19+mon_C_EUMS_20170730234611_amsua_20170730_2200_noaa19_43683_mon_l1c_bufr.bin


- Satellite 01
  - Instrument 01
  - Product level


###### From the file name

###### From the additional metadata file

## Open Points

- [ ] If the MQ goes down, all components fail. A fallback mechanism is needed when the MQ goes down .
  - https://stackoverflow.com/questions/58110868/fallback-mechanism-when-rabbitmq-goes-down 