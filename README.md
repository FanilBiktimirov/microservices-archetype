# microservices-archetype

Это шаблон для создания микросервиса с подготовленной структурой и минимально необходимым набором зависимостей и
настроек

# Создание проекта на основе архетипа

Существует два простых способа создания проекта через архетип - через интерфейс IntelliJ IDEA и через стандартный
maven-плагин

## Создание с использованием среды разработки

1. в среде разработки выполните следующие действия `File -> New -> Project -> Maven`

2. установите флажок `Create from archetype` и нажмите на кнопку `add archetype`

3. в появившемся окне заполните:

* groupId: `ru.microservice.archetype`
* artifactId: `microservice-archetype`
* version: **<актуальная версия>**

5. выберите из списка архетипов `microservice-archetype`

6. заполните `groupId`, `artifactId` & `version`

7. в списке `properties` передайте следующие переменные:

|              Переменная              | Значение по умолчанию |                    Описание                    |
|:------------------------------------:|:---------------------:|:----------------------------------------------:|
|          springBootVersion           |         3.2.6         |        Версия фреймворка `spring-boot`         |
| springBootVersionVersionDefaultValue |         3.2.6         | Версия фреймворка по умолчанию `spring-boot`   |
|            revisionValue             |         1.0.0         |             Версия maven-артефакта             |
|              includeDb               |         false         |   Флаг добавления опционального модуля `-db`   |
|            includeClient             |         true          | Флаг добавления опционального модуля `-client` |

## Создание с использованием командной строки

1. в командной строке перейдите в директорию, в которой хотите создать проект, при помощи команды `cd ...`

2. введите команду `mvn archetype:generate -DarchetypeGroupId=ru.microservice.archetype -DarchetypeArtifactId=microservice-archetype -DarchetypeVersion=` **<актуальная версия>**

3. из предложенного введите:

- `revisionValue`
- `includeDb` (установите `true` если в проекте нужен модуль `-db`)
- `includeClient` (установите `false` если в проекте не нужен модуль `-client`)

4. для всех остальных предлагаемых `properties` нажимать просто `enter`
