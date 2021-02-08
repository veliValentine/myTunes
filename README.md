# myTunes
myTunes is Thymeleaf/REST java application. Read more about the application [here](#description)
and more about the use cases [here](#usage).

Application is running in [heroku](https://velivalentine-mytunes.herokuapp.com/) at 

[https://velivalentine-mytunes.herokuapp.com/](https://velivalentine-mytunes.herokuapp.com/)

## Table of contents

- [Description](#description)
    - [User interface](#descriptionUI)
    - [Rest API](#descriptionRest)
- [Usage](#usage)
    - [User interface](#usageFront)
    - [Rest API](#usageRest)
      - [(GET) all customers](#1)
      - [(POST) new customer](#2)
      - [(PUT) update customer](#3)
      - [(GET) customers per country](#4)
      - [(GET) customers spending](#5)
      - [(GET) customers popular genre](#6)
- [Structure](#structure)

<a name="description"></a>

## Description

Application contains simple user interface (UI) and REST API. The UI is build using Java thymeleaf and the API Java
Spring. Application relies on Chinook-database that can be
founded [here](https://github.com/lerocha/chinook-database/raw/master/ChinookDatabase/DataSources/Chinook_Sqlite.sqlite)
.

Application is running in [heroku](https://velivalentine-mytunes.herokuapp.com/).

<a name="descriptionUI"></a>

#### User interface

The UI has two main page. Home page and song page.

The Home page shows 5 random songs, artist and music genres. User also has the possibility to search for songs by song
name.

Search results are shown in the song page, listing all founded songs. If there is only one song matching the search
parameter, a song page is shown.

<a name="descriptionRest"></a>
#### REST API

The api has 6 main functionalities:

- Returning all customers (GET)
- Adding new customers (POST)
- Updating a customer (PUT)
- Returning number of customers in a country (GET)
- Returning highest spending customers (GET)
- Returning most popular music genre for a given customer (GET)

<a name="usage"></a>
## Usage

<a name="usageFront"></a>
### User interface

User interface has a [main page](https://velivalentine-mytunes.herokuapp.com/). Main page shows 5 random song, artist
and genres. User can search songs by name.

When [no songs are found](https://velivalentine-mytunes.herokuapp.com/song?songName=lol), user is notified that no
result are found. User can research new song or use header bar to navigate back to home.

[A list of songs](https://velivalentine-mytunes.herokuapp.com/song?songName=lil) are shown when search result contains
more than 1 song.

When search result contains only one song, user is redirected
to [song page](https://velivalentine-mytunes.herokuapp.com/song/2623). User can navigate to single song page by clicking
search result when multiple songs are found. Random song option redirect to this page if there is no other songs with
same song name.

<a name="usageRest"></a>
### Rest API

Root URI to first version of applications API
is [https://velivalentine-mytunes.herokuapp.com/api/v1/](https://velivalentine-mytunes.herokuapp.com/api/v1/)

[POSTMAN_COLLECTION](/documents/myTunesApi.postman_collection.json) of API example methods. Collection
uses http://localhost:8080 as root URI.

API returns JSON objects. Here are example snippets of responses made to APIs main functionalities.

<a name="1"></a>
#### (GET) ALL CUSTOMERS

[/api/v1/customers](https://velivalentine-mytunes.herokuapp.com/api/v1/customers) returns a list of Customer objects

```json
{
  "id": "2",
  "firstName": "Leonie",
  "lastName": "Köhler",
  "country": "Germany",
  "postalCode": "70174",
  "phoneNumber": "+49 0711 2842222",
  "email": "leonekohler@surfeu.de"
}
```

<a name="2"></a>
#### (POST) ADDING NEW CUSTOMER

[/api/v1/customers](https://velivalentine-mytunes.herokuapp.com/api/v1/customers) (HTTP method POST) returns boolean
value. True if customer added successfully and false otherwise.

Method requires Customer content in the body of the request.

Example body:

```json
{
  "firstName": "paavo",
  "lastName": "Pesusieni",
  "country": "Finland",
  "postalCode": "00100",
  "phoneNumber": "+358401234567",
  "email": "paavo@pesusieni.com"
}
```

<a name="3"></a>
#### (PUT) UPDATE CUSTOMER

[/api/v1/customers/:customerID](https://velivalentine-mytunes.herokuapp.com/api/v1/customers) (HTTP method PUT).

Returns true if customer is modified. Otherwise, false. Also make sure that id in URI and in method body are the same.

Example of method body:

```json
{
  "id": "1",
  "firstName": "perus paavo",
  "lastName": "Pesusieni",
  "country": "Caribbean",
  "postalCode": "0000",
  "phoneNumber": "new number",
  "email": "paavo@atVacation.com"
}
```

<a name="4"></a>
#### (GET) RETURNING CUSTOMER COUNT PER COUNTRY

[/api/v1/customers/country](https://velivalentine-mytunes.herokuapp.com/api/v1/customers/country) returns a list of
countries, and the number of customers country has.

Example of list object:

```json
{
  "country": "USA",
  "count": 13
}
```

<a name="5"></a>
#### (GET) RETURN HIGHEST SPENDING CUSTOMER

[/api/v1/customers/spending](https://velivalentine-mytunes.herokuapp.com/api/v1/customers/spending)
returns a list of customer object with additional totalSpending attribute.

Example of customer object with total spending:

```json
{
  "id": "6",
  "firstName": "Helena",
  "lastName": "Holý",
  "country": "Czech Republic",
  "postalCode": "14300",
  "phoneNumber": "+420 2 4177 0449",
  "email": "hholy@gmail.com",
  "totalSpending": 49.62
}
```

<a name="6"></a>
#### (GET) MOST POPULAR MUSIC GENRE FOR CUSTOMER

[/v1/customers/:customerId/genres](https://velivalentine-mytunes.herokuapp.com/api/v1/customers/12/genres)
returns a list of customers favorite music genre.

Example of customer genres with the id 12:

```
[
  "Rock",
  "Latin"
]
```

<a name="structure"></a>

## Structure

```
| README.md
| pom.xml
|-src
| |-main
|   |-java
|   | |-group.demo
|   | | | MyTunes (main)
|   | | |-controllers (controllers for thymleaf)
|   | | |-restControllers (API controller)
|   | | |-dataAccess (Connection with db)
|   | | |-logger
|   | | |-models
|   |
|   |-resources
|   | | data base (Chinook)
|   | |-templates
|   | | HTML templates
|
|-documents
```

Author
[veliValentine](https://github.com/veliValentine)