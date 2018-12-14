## Plugins framework

This allows you to create and use your own plugins in Concord.
For example to create a custom label suggester.

Components: 

* Label suggester
  * Responsible for suggesting labels when given phrases
  * Can be based on a underlying NLP model or simply return all available labels
  
* Credentials validator
  * Responsible for validating credentials to the concord app

## Usage

* Implement the [label suggester](src/main/java/com/rabidgremlin/concord/plugin/LabelSuggester.java) and [credentials validator](src/main/java/com/rabidgremlin/concord/plugin/CredentialsValidator.java) (or use the examples)

* Put them on class path
* Include them in [server.yml](../server/src/main/yml/server.yml) (with config)