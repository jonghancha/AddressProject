# AddressProject
주소록 만들기 세미 프로젝트 제출용 메인 저장소

## 1. 시스템 설치법
### 본 시스템은 Tomcat서버를 사용하고 있습니다.

#### - Tomcat 서버 폴더 하위 ROOT 안에 test 라는 폴더를 생성해줍니다.(jsp와 파일업로드 될 경로)
- JSP Folder : Tomcat >> webapps >> ROOT >> test (GitHub의 JSP 폴더 안 파일을 test에 복사해주세요)
- Upload File Folder : Tomcat >> webapps >> ROOT >> test

  - insertMultipart.jsp 18행 realPath 설정.(웹서버에 저장되는 이미지와 같은 이름의 이미지가 저장되는 곳)
    - 맥os :  터미널에서 저장하고자 하는 폴더로 이동(cd)한 후, pwd 로 경로 가져와야 합니다
    - Windows :  예시) "C:\\\Program Files ~\\\ROOT\\\test"와 같이 역슬래시 두번써줘야 합니다


- activity PACKAGE 안 ShareVar 클래스에서 본인 IP로 변경하기
  <img src= "https://github.com/jonghancha/AddressProject/blob/main/mdImage/Inkedsharevar_LI.jpg">

## 2. Database backup 
  - JSP와 마찬가지로 GitHub의 DB안 파일을 사용해주세요

## 3. 외부 Module 설치법
- Upload Library : Tomcat >> lib <- cos.jar
- Android >> Gradle Scripts >> build.gradle(Module)에 okhttp3 추가
