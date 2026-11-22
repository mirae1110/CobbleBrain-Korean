# GitHub Actions에서 빌드하기

이 저장소는 **미검증 개발 시제품**입니다. GitHub에 올린 뒤 Actions 빌드를 실행해도 오류가 발생할 수 있으며, 빌드가 성공해도 Minecraft 실제 작동은 별도 검증이 필요합니다.

1. GitHub에서 새 공개 또는 비공개 저장소를 만듭니다.
2. 이 ZIP을 풀고 **폴더 안의 모든 파일과 `.github` 폴더**를 저장소 루트에 업로드합니다. (ZIP 파일을 통째로 업로드하지 마세요.)
3. 저장소의 Actions 탭에서 **Build CobbleBrain Korean Addon** 선택 → Run workflow → Run workflow.
4. 초록 체크라면 해당 실행의 **Artifacts**에서 `CobbleBrain-Korean-Addon-Prototype`을 받습니다.
5. 빨간 X라면 Build Fabric mod 단계의 오류 로그를 확인해야 합니다.

**주의:** `build/libs` 폴더의 `-sources.jar`는 설치용이 아닙니다. 일반 JAR만 테스트하며, 원본 모드팩을 복제해서 시험하세요.
