# ShowRenamer
Renames movie and tv show file names to a much nicer format

some.random.movie.2010.1080p.blueray.x264 --> Some Random Movie (2010) <br/>
some.random.tv.show.s01e01.1080p.bluray.x264 --> Some Random TV Show - S01E01 - {episode name} <br/>

## Project Status
Currently under development, pre-release is now out!

### Planned features to be implemented
- Search page for shows if file doesn't include title
- Allow to be used on Command Line Interface
- User specified format to rename shows to
- Subtitle download page
- Support for multiple themes e.g. light and dark mode

## Usage
### Windows
1. Download the jar from the [releases](https://github.com/c-eg/ShowRenamer/releases)
2. Double click to run the jar
3. If step 2. doesn't work, try running from command line using: `java -jar JAR_NAME.jar`

### Linux
Todo

### Mac
Todo

## Pictures
![ShowRenamer GUI](https://i.imgur.com/JQc4uRL.png)
This is a pre-release screenshot of the GUI, changes will be made in the released versions.

## How to setup project locally
1. Fork this repository, and clone it using "git clone"
2. Import maven to project
3. Create an account for [themoviedb.org](https://www.themoviedb.org/), and get an API key
4. Move the file in: `api_key_default.json` to `/code/src/main/resources/config/api_key.json`. And update it with your api key
5. Install using maven: `mvn install`

## Contributing
Contributions are always welcome! Make sure to check out the [Code of Conduct](https://github.com/c-eg/ShowRenamer/blob/master/CODE_OF_CONDUCT.md) and the [Contribution Guidelines](https://github.com/c-eg/ShowRenamer/blob/master/CONTRIBUTING.md) first.<br>
If you're not sure what to work on, check out the current [issues](https://github.com/c-eg/ShowRenamer/issues).

## License
GNU General Public License v3.0
