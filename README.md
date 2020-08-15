# ShowRenamer
Renames movies and tv shows in the format the user has specified.  
  <br/>
![ShowRenamer GUI](https://i.imgur.com/F7UQto6.png)
This is a pre-release screenshot of the GUI, changes will be made in the released versions.

## Project Status
Currently under development, pre-release is now out!

## Usage
### Windows
1. Download the jar from the [releases](https://github.com/c-eg/ShowRenamer/releases)
2. Double click to run the jar
3. If step 2. doesn't work, try running from command line using: `java -jar JAR_NAME.jar`

### Linux
Todo

### Mac
Todo

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
