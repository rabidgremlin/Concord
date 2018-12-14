## Plugins framework

This allows you to create and use your own plugins in Concord.
For example to create a custom label suggester which is based on your own NLP model.

### Components: 

* Label suggester
  * Responsible for suggesting labels when given phrases
     * Can be based on a underlying NLP/NLU model or simply return all available labels etc.
  
* Credentials validator
  * Responsible for validating credentials to the concord app
     * Can be a simple userId/password check or a token system etc.

### Usage

* Implement all components 
  * [Label Suggester](src/main/java/com/rabidgremlin/concord/plugin/LabelSuggester.java) 
  * [Credentials Validator](src/main/java/com/rabidgremlin/concord/plugin/CredentialsValidator.java) 
  * Can simply use the examples included in the [server module](../server)
* Put them on class path
* Include them in [server.yml](../server/src/main/yml/server.yml) (with config if needed)