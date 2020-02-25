# Project Base for Vaadin Flow and Spring Boot

테넌트 생성 시 식별자는 application.properties의 com.kulcloud.signage.tenant 키에 대한 값으로 설정해야 한다.
com.kulcloud.signage.tenant의 값은 MySQL의 사용자 계정명과 일치해야 하며 해당 테넌트의 db 스키마명을 signage-[테넌트 식별]로 
생성해야 한다.
그리고 db는 innodb(MySQL 기본)형으로 생성한다.