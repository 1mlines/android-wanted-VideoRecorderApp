# 원티드 프리온보딩 안드로이드
# 2팀 VideoRecorderApp

## 팀원

<div align="center">
  <table style="font-weight : bold">
      <tr>
          <td align="center">
              <a href="https://github.com/tjrkdgnl">                 
                  <img alt="tjrkdgnl" src="https://avatars.githubusercontent.com/tjrkdgnl" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/dudwls901">                 
                  <img alt="gyurim" src="https://avatars.githubusercontent.com/u/66052467?v=4" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/014967 ">                 
                  <img alt="lsy524" src="https://avatars.githubusercontent.com/014967 " width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/DavidKwon7">                 
                  <img alt="davidKwon7" src="https://avatars.githubusercontent.com/u/70066242?v=4" width="80" />            
              </a>
          </td>
      </tr>
      <tr>
          <td align="center">서강휘</td>
          <td align="center">김영진</td>
          <td align="center">김현국</td>
          <td align="center">권혁준</td>
      </tr>
  </table>
</div>

## 김현국
### 맡은 역할
- 아키텍처 설계

### 아키텍처 구조
<img src= "https://user-images.githubusercontent.com/62296097/196941657-8179f6f5-5b1f-4002-9f55-5693bff08655.png">

- Domain Layer는 Repository interface와 UseCase interface을 포함하고 있으며 인터페이스의 구현체는 Data Layer에서 구현하였습니다.

### 프로젝트 구조 - 멀티모듈
<img src="https://user-images.githubusercontent.com/62296097/196951134-165fec0b-6a07-42d5-9431-9f71a2f11aa9.png">


- Presentation Layer
    - UI과 관련된 작업으로 구성되어있습니다.
    - 대표적으로 Activity, Fragment, ViewModel이 있습니다.

- Domain Layer
    - 비지니스 로직에서 수행되어져야할 행동들을 Interface로 정의하고 제공됩니다.
    - Presentation Layer에 제공되는 비지니스 모델이 포함됩니다. 
    

- Data Layer
    - Domain Layer에서 정의된 Interface(Repository, Usecase)의 구현체와 DataSource가 존재합니다.
    - Hilt에 의해 인터페이스의 구현체가 제공됩니다. 
    - DataSource는 Local과 Remote로 나뉘어 각각 Room Database와 Firebase에 접근합니다.



- Gradle
    - KTS를 통해 gradle를 Kotlin Script로 구성했습니다. 
- Version Catalog
    - Library와 Plugin의 버전을 관리합니다.



___

## 김영진
### 맡은 역할


___


## 서강휘
### 맡은 역할

___

## 권혁준
### 맡은 역할



## Convention

### Branch Convention

```feat/<branch name>  ```

- e.g) ```feat/Base Architecture ```


### Commit convention

``` [prefix]: <commit content> ```

- e.g) ``` feat: DAO 개발완료 ```

- e.g) ``` fix: room crash 수정 ```

- e.g) ``` refactor: MVVM 아키텍처 구조 리팩토링 ```

### Issue Convention
``` [prefix] 작업할 내용 ```

- e.g) ``` [feat] base architecture 생성 ```
- e.g) ``` [fix] room crash 수정 ```
- e.g) ``` [refactor] Sensor구조 일부 수정 ```

- 브랜치를 생성하기 전, github issue를 생성해주세요.
- branch 명의 issue number는 해당 issue Number로 지정합니다.

### PR Convention
``` [Issue-#number] PR 내용 ```

- e.g) ``` [Issue-#7] Timer 추가 ``` 

-----
