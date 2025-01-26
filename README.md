# Show Renamer
Renames movie and tv show file names to a much nicer format.

## Example
|         | Before                                             | After                                              |
|   ---   | -------------------------------------------------- | -------------------------------------------------- |
| Movie   | some.random.movie.2010.1080p.blueray.x264          | Some Random Movie (2010)                           |
| TV Show | some.random.tv.show.2010.s01e01.1080p.bluray.x264  | Some Random TV Show (2010) - S01E01 - Episode Name |

## Showcase
![ShowRenamer GUI](https://i.imgur.com/OddrNDK.png)

## Features
- Renames movies, tv shows and tv show episodes
- Works with files and directories
- Custom rename formats
- File type filtering

## Usage
1. Download the latest [release](https://github.com/c-eg/ShowRenamer/releases)
2. Install & run

## Local Development
### Prerequisites
- [JDK 21 (Amazon Corretto)](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
- [JavaFX SDK](https://openjfx.io/openjfx-docs/#install-javafx)
  - then add the modules in your VM options, e.g.

  ```--module-path "C:\Program Files\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml```

### Setup
1. Create an account for [themoviedb.org](https://www.themoviedb.org/), and get an API key (currently version 3)
2. Go to the properties file `/src/main/resources/properties/show-renamer.properties`, and update it with your api key
3. Run using `gradlew run`

### Helpful Tools
- [SceneBuilder](https://gluonhq.com/products/scene-builder/) is a fantastic tool to create user interfaces for JavaFX

## License
GNU General Public License v3.0
