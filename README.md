# Android Nougat(7.0) Camera
Android 7.0(SDK 24)로 업데이트 되면서 기존 앱을 이용하는데 있어 제약이 걸리는 이슈가 하나 발생하였습니다.
그 것은 바로 파일 시스템 권한이 변경되었다는 것인데, 개인 파일의 보안을 강화하기 위해 Android 7.0 이상의 버전에서는 개인 디렉터리의 엑세스가 제한됩니다.

그 중에 가장 빈번하고 많이 사용되는 기능이 카메라 기능인데, 카메라로 찍은 사진 데이터의 저장소를 지정하려하면 FileUriExposedException이 나면서 앱이 crash됩니다.

이를 해결하기 위해서는 FileProvider를 사용하여 file 경로에 접근하여야 합니다.

FileProvider의 자세한 내용은 아래의 링크를 통하여 확인하고, 해당 소스는 FileProvider를 통한 카메라/갤러리 접근하는 예제 샘플입니다.
https://developer.android.com/reference/android/support/v4/content/FileProvider.html


안드로이드 7.0 변경 사항은 이 곳을 참조하세요.
https://developer.android.com/about/versions/nougat/android-7.0-changes.html?hl=ko

