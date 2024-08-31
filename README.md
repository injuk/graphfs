# GraphFS란?
Neo4j와 Cypher를 활용한 가상 파일 시스템 애플리케이션을 만들어보자!
> 실제 파일 시스템을 구현하는 정도는 아니고, Neo4j와 Cypher 문법에 익숙해지기 위한 토이 프로젝트.

### 도메인
#### 폴더
- 리소스를 품는 폴더 개념
- 파일 시스템은 하나의 루트 폴더를 갖고, 루트 폴더 아래에 트리 형태의 구조로 구성될 수 있음.

#### 리소스
- 폴더에 저장된 임의의 리소스를 대변

### 기능
- 폴더 CRUD
- 폴더 이동

### 패키지 구조
```
graphfs
├── domain
│   ├── useCase
│   ├── Folder
│   └── Resource
├── application
│   ├── service
│   │   ├── command
│   │   └── query        
│   ├── controller
│   └── gateway
└── infrastructure
│   ├── web
│   └── persistence
└── GraphfsApplication.kt    
```

### TODO
- [x] resource 추가 API 개발
- [ ] resource 삭제 API 개발
- [ ] resource 수정(이동) API 개발
- [x] folder 상세 API가 resource를 함께 반환하도록 개선
  - 이건 resource 목록 조회 API로 해결하는게 맞다고 봄
- [x] folder 삭제 API가 resource 존재 여부를 함께 체크하도록 개선
- [ ] folder 이름 기반 조회 API 개발
- [ ] folder 조상 조회(?) API 개발