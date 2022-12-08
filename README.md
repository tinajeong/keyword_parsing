# keyword_parsing
- 마케터 친구의 부탁을 받아 1시간동안 만든 첫번째 시트의 키워드를 두번째 시트의 글에서 확인해 키워드를 덧붙이는 프로그램입니다.  

## Pre-requisite
- jdk 1.8이상

## 프로그램 실행방법

### keyword_parsing 폴더로 이동
```
$ ls
build           gradle          gradlew.bat     sample.xlsx     src
build.gradle    gradlew         settings.gradle
```

### 파싱을 원하는 엑셀파일 복붙하기
```
sample.xlsx
```

### 빌드하기
```
$ ./gradlew build
```

### 실행하기
```
$ ./gradlew run —args='sample.xlsx'
```
### new로 시작하는 엑셀파일이 생성되는지 확인
