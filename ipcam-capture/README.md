# [KOREN 2019] 멀티 클라우드 기반 스마트 사이니지 IP camera 포워딩 모듈
- Opencv 라이브러리 기반 스트리밍 캡쳐 기능
- 캡쳐된 이미지를 추론 엔진으로 포워딩하는 기능

# 설치 요구사항
#### 
~~~
cmake version 3.15.0에서 테스트
~~~

# 설치 방법
~~~
git clone https://github.com/kongseokhwan/smart_signage.git

cd smart_signage/ipcam-capture

mkdir build

cd build

cmake ..

make

~~~

# 실행 방법
~~~
./cap -c {ip 카메라 주소} -u {추론 엔진 url} 
Option : 
        -c camera address, default: rtsp://admin:kulcloud@123&&@10.1.101.1:554
        -u inference engine url, default: http://localhost:5000 

~~~