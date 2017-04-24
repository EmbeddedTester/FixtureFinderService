FROM openjdk
RUN mkdir -p /opt/fixturefinder
WORKDIR /opt/fixturefinder
ENV PORT 8080
ADD ./target/fixture-finder-0.0.1.jar /opt/fixturefinder/bin/fixture-finder-0.0.1.jar