mvn archetype:generate -DgroupId=cs.toronto.edu -DartifactId=pgsample -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
cp Main.xml pgsample/pom.xml
cp Main.java pgsample/src/main/java/cs/toronto/edu/
cp -r ./src pgsample/src/main/java/cs/toronto/edu/

cp -r ./pgsample/src ../backend/src/main/java/backend/logic
rm pgsample/src/main/java/cs/toronto/edu/App.java