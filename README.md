# Data Processing Framework

## System Requirments

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