FROM adoptopenjdk/openjdk11:latest
EXPOSE 8081
ENV TZ="Japan/Tokyo"
ADD target/OEKG-0.1.war OEKG-0.1.war
CMD ["java","-jar","/OEKG-0.1.war"]