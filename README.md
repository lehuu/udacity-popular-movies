# Popular Movies

This project was created as part of the Udacity Android nanodegree term 1.
Using the Movie DB API we can browse through a list of popular movies.

## Getting Started

To run this project you need a Movie DB API-Key.
Get it here:

```
https://www.themoviedb.org/settings/api
```

Next you need to add it to your global gradle properties.
You should find it here (create a gradle.properties if it doesn't exist yet):

```
Windows: C:\Users\<Your Username>\.gradle
Mac: /Users/<Your Username>/.gradle
Linux: /home/<Your Username>/.gradle
```

Here add the key with value MOVIE_DB_KEY e.g.

```
MOVIE_DB_KEY="<my-movie-db-key-value>"
```

## Libraries

* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Retrofit](https://github.com/square/retrofit)
* [Glide](https://github.com/bumptech/glide)

## License

    Copyright 2019 Phuoc Le

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.