#./mvnw install -Dmaven.test.skip=true

./mvnw deploy:deploy-file -Durl=http://ir-nexus-gcp.inf-impact.net:8081/repository/thirdparty \
                       -DrepositoryId=thirdparty \
                       -Dfile=impl/target/jjwt-impl-0.12.5-IMPACT-PATCHED.jar \
                       -DpomFile=impl/pom.xml \
                       -DgroupId=io.jsonwebtoken \
                       -DartifactId=jjwt-impl \
                       -Dversion=0.12.5-IMPACT-PATCHED \
                       -Dpackaging=jar \
                       -DgeneratePom=false

./mvnw deploy:deploy-file -Durl=http://ir-nexus-gcp.inf-impact.net:8081/repository/thirdparty \
                       -DrepositoryId=thirdparty \
                       -Dfile=api/target/jjwt-api-0.12.5-IMPACT-PATCHED.jar \
                       -DpomFile=api/pom.xml \
                       -DgroupId=io.jsonwebtoken \
                       -DartifactId=jjwt-api \
                       -Dversion=0.12.5-IMPACT-PATCHED \
                       -Dpackaging=jar \
                       -DgeneratePom=false

./mvnw deploy:deploy-file -Durl=http://ir-nexus-gcp.inf-impact.net:8081/repository/thirdparty \
                       -DrepositoryId=thirdparty \
                       -Dfile=tdjar/target/jjwt-0.12.5-IMPACT-PATCHED.jar \
                       -DpomFile=pom.xml \
                       -DgroupId=io.jsonwebtoken \
                       -DartifactId=jjwt \
                       -Dversion=0.12.5-IMPACT-PATCHED \
                       -Dpackaging=jar \
                       -DgeneratePom=false