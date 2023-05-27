# Show Renamer
Renames movie and tv show file names to a much nicer format.

## Example
|         | Before                                       | After                                       |
|   ---   | -------------------------------------------- | ------------------------------------------- |
| Movie   | some.random.movie.2010.1080p.blueray.x264    | Some Random Movie (2010)                    |
| TV Show | some.random.tv.show.s01e01.1080p.bluray.x264 | Some Random TV Show - S01E01 - Episode Name |

## Showcase
![ShowRenamer GUI](https://i.imgur.com/5XG8YWJ.png)

## Features
- Renames movies, tv shows and tv show episodes
- Works with files and directories
- Allows custom rename format

## Usage
1. Currently requires downloading [Amazon Corretto JDK 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html). This is a [planned fix](https://github.com/c-eg/ShowRenamer/issues/75) for the future.
1. Download the latest jar from the [releases](https://github.com/c-eg/ShowRenamer/releases)
2. Double click to run the jar
3. If step 2. doesn't work, try running from command line using: `java -jar JAR_NAME.jar`

## Local Development
### Prerequisites
- JDK 17 (Amazon Corretto)

### Setup
1. Fork this repository, and clone it using "git clone"
2. Create an account for [themoviedb.org](https://www.themoviedb.org/), and get an API key (currently version 3)
3. Go to the properties file `/src/main/resources/properties/show-renamer.properties`, and update it with your api key
4. Install using maven: `mvn clean install`
5. Run using `uk.co.conoregan.showrenamer.ShowRenamerLauncher.main`

### Helpful Tools
- [SceneBuilder](https://gluonhq.com/products/scene-builder/) is a fanstastic tool to create user interfaces for JavaFX

## License
GNU General Public License v3.0
