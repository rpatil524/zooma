FROM tomcat:9-jdk8-openjdk

# ENV ZOOMA_HOME=/opt/zooma cant set this because hard coded in build-virtuoso-index.sh 
ENV ZOOMA_OPTS="-Xms1g -Xmx4g"

RUN mkdir /opt/zooma_github /opt/zooma
RUN apt-get update && apt-get install -y nano maven
COPY . /opt/zooma_github/

RUN rm -rf /root/.m2/
RUN tar -xvzf /opt/zooma_github/ols-client/lib/m2directory.tar.gz -C /root/

ENV OJDBC6="https://www.oracle.com/webapps/redirect/signon?nexturl=https://download.oracle.com/otn/utilities_drivers/jdbc/11204/ojdbc6.jar"
RUN wget $OJDBC6 -O /lib/ojdbc6.jar \
    && mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dpackaging=jar -Dversion=11.2.0.4 -Dfile=/lib/ojdbc6.jar \
    && test /root/.m2/repository/com/oracle/ojdbc6/11.2.0.4/ojdbc6-11.2.0.4.jar

RUN mvn install:install-file -DcreateChecksum=true -Dpackaging=jar -Dfile=/opt/zooma_github/zooma-lodestar/lib/jena-core-2.12.0.jar -DgroupId=org.apache.jena -DartifactId=jena-core -Dversion=2.12.0
RUN mvn install:install-file -DcreateChecksum=true -Dpackaging=jar -Dfile=/opt/zooma_github/zooma-lodestar/lib/jena-arq-2.12.0.jar -DgroupId=org.apache.jena -DartifactId=jena-arq -Dversion=2.12.0
RUN mvn install:install-file -DcreateChecksum=true -Dpackaging=jar -Dfile=/opt/zooma_github/zooma-lodestar/lib/lode-core-api-1.3.jar -DgroupId=ebi-lode -DartifactId=lode-core-api -Dversion=1.3
RUN mvn install:install-file -DcreateChecksum=true -Dpackaging=jar -Dfile=/opt/zooma_github/zooma-lodestar/lib/lode-virtuoso-impl-1.3.jar -DgroupId=ebi-lode -DartifactId=lode-virtuoso-impl -Dversion=1.3
RUN mvn install:install-file -DcreateChecksum=true -Dpackaging=jar -Dfile=/opt/zooma_github/zooma-ui/lib/jackson-mapper-asl-1.5.3.jar -DgroupId=org.codehaus.jackson -DartifactId=jackson-mapper-asl -Dversion=1.5.3
RUN mvn install:install-file -DcreateChecksum=true -Dpackaging=jar -Dfile=/opt/zooma_github/zooma-ui/lib/jackson-core-asl-1.5.3.jar -DgroupId=org.codehaus.jackson -DartifactId=jackson-core-asl -Dversion=1.5.3

RUN cd /opt/zooma_github && mvn clean package
RUN mkdir /opt/tmp && unzip /opt/zooma_github/zooma-builder-app/target/zooma-builder.zip -d /opt/tmp
RUN cp /opt/zooma_github/zooma-ui/target/zooma.war /usr/local/tomcat/webapps/

ENV VIRTUOSO_VERSION="7.2.5"
ENV VIRTUOSO_HOME=/opt/virtuoso/virtuoso-opensource
ENV VIRTUOSO="https://downloads.sourceforge.net/project/virtuoso/virtuoso/${VIRTUOSO_VERSION}/virtuoso-opensource.x86_64-generic_glibc25-linux-gnu.tar.gz"
# ?r=&ts=1597420967
RUN mkdir /opt/virtuoso && wget $VIRTUOSO -O /opt/virtuoso/virtuoso-${VIRTUOSO_VERSION}.tar.gz \
    && tar -xzvf /opt/virtuoso/virtuoso-${VIRTUOSO_VERSION}.tar.gz -C /opt/virtuoso

RUN sed -i 's@base=\${0%\/[*]}/..;@base=..;@g' /opt/tmp/bin/build-labels.sh
RUN sed -i 's@base=\${0%\/[*]}/..;@base=..;@g' /opt/tmp/bin/build-lucene-index.sh
RUN sed -i 's@base=\${0%\/[*]};@base=.;@g' /opt/tmp/bin/build-virtuoso-index.sh 
RUN sed -i 's@base=\${0%\/[*]}/..;@base=..;@g' /opt/tmp/bin/build-rdf.sh

# ERROR: $ZOOMA_DATA_DIR not set - using /root/.zooma/data (build-virtuoso-index.sh )
ENV ZOOMA_HOME=/root/.zooma
RUN ls -a /root/
RUN cp /opt/zooma_github/startup.sh /root/startup.sh
# The following is a bit of a hack to initialise the zooma directories
RUN startup.sh && sleep 20 && shutdown.sh 

# Overwriting server.xml is necessary only because of we had to add relaxedQueryChars='^{}[]|&quot;' to the main connector
COPY server.xml /usr/local/tomcat/conf/server.xml
#RUN mkdir $ZOOMA_HOME $ZOOMA_HOME/config
EXPOSE 8080
CMD ["bash","/root/startup.sh"]
#CMD ["catalina.sh","run"]
