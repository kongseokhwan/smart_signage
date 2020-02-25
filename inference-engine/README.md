# [KOREN 2019] 멀티 클라우드 기반 스마트 사이니지 추론 엔진
- author by luzhcs@kulcloud.net
Opencv 라이브러리 기반 Classification 이후 influxdb에 추론 결과를 저장하는 모듈입니다. 

# 설치 요구사항
#### Python Version
~~~
3.7이상 권장
~~~
#### Pip requirements
~~~
다음 패키지 리스트가 필요함
aiohttp==3.6.2
astroid==2.3.1
async-timeout==3.0.1
atomicwrites==1.3.0
attrs==19.2.0
certifi==2019.9.11
chardet==3.0.4
Click==7.0
ffmpeg-python==0.2.0
Flask==1.1.1
future==0.18.1
idna==2.8
importlib-metadata==0.23
influxdb==5.2.3
isort==4.3.21
itsdangerous==1.1.0
Jinja2==2.10.3
lazy-object-proxy==1.4.2
MarkupSafe==1.1.1
mccabe==0.6.1
more-itertools==7.2.0
multidict==4.5.2
numpy==1.17.2
opencv-python==4.1.1.26
packaging==19.2
Pillow==6.2.1
pluggy==0.13.0
py==1.8.0
pylint==2.4.2
pyparsing==2.4.2
pytest==5.2.0
python-dateutil==2.8.1
python-vlc==3.0.7110
pytz==2019.3
PyYAML==5.1.2
requests==2.22.0
rtsp==1.1.6
six==1.12.0
tornado==6.0.3
typed-ast==1.4.0
urllib3==1.25.6
vcrpy==2.1.0
wcwidth==0.1.7
Werkzeug==0.16.0
wrapt==1.11.2
yarl==1.3.0
zipp==0.6.0
~~~

# 설치 방법
~~~
git clone https://github.com/kongseokhwan/smart_signage.git

cd smart_signage/inference-engine

pip install -r requirements.txt
~~~

# 실행 방법
~~~
aiohttp 서버 포트 기본값 5001

python main.py
~~~

~~~
handler/opencvdnn.py 의 43 line --> influxdb 주소 설정 필요
~~~

# 모델 설명
~~~
Gender Net : https://www.dropbox.com/s/iyv483wz7ztr9gh/gender_net.caffemodel?dl=0"

Age Net : https://www.dropbox.com/s/xfb20y596869vbb/age_net.caffemodel?dl=0"
~~~



