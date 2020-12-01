# report-exporter
Export reports from the NCI Thesaurus.  The Report Exporter utilizes the EVS REST API for querying its data.
## Development Setup
### Report Exporter UI Setup
The UI is developed using [Vue.js](https://vuejs.org/). 
Vue.js can be installed with NPM.  NPM is available through [Node.js](https://nodejs.org/en/).

Once Vue.js and NMP are installed, the Report Exporter UI can be build and run the client in dev mode.  To do this, go to the **report-exporter/clientui** directory.

To build the UI, run the following command:

    npm run build
    
To run the UI, run the following command:

    npm run serve


### Report Exporter Service Setup
## Deployment Setup - Tomcat
The Report Exporter Application has been built and tested on a Tomcat 8 webserver.  Our instructions will focus on deploying to a Tomcat server.

### Report Exporter Build

At the root of the git project directory, run the Maven command that builds both the client and the server and encapsulates them in on war file.

**Maven builds for their respective tier**
* mvn clean install -DBuild_Env=build-dev
* mvn clean install -DBuild_Env=build-qa
* mvn clean install -DBuild_Env=build-stage
* mvn clean install -DBuild_Env=build-prod 

Add the war file from the Maven build to Tomcat webapps directory. The war file will be located under **report-exporter/service/target/**

#### Configure Tomcat Server
You will need to create a file called **Tomcat8/conf/Catalina/localhost/rewrite.config** and add the following contents in it.

    #-------------------------------------------------------------------------------
    # site that has example of this.
    # https://stackoverflow.com/questions/34619751/tomcat-8-url-rewrite-issues
    #-------------------------------------------------------------------------------
    #-------------------------------------------------------------------------------
    # Any subsequent file that the index.html file needs, needs to pass through.
    # This would be the js and css files along with other miscellaneous ones.
    #-------------------------------------------------------------------------------
    RewriteCond %{REQUEST_URI} .*\.(css|map|js|html|png|jpg|jpeg|gif|txt|ttf|json|woff|ico)$ [OR]

    #----------------------------------------
    # Allow these service calls to go through
    #----------------------------------------
    RewriteCond %{REQUEST_URI} ^(/reportexporter/download).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/properties).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/codereadrest).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/roots).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/curated).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/resolve\-).*$ [OR]

    RewriteRule ^(.*)$ - [L]
    RewriteRule ^(.*)$ /reportexporter/index.html


Update the **Tomcat8/conf/server.xml** file for Tomcat. Add the following **"RewriteValve"** entry to the Host section:

    <Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">

          <Valve className="org.apache.catalina.valves.rewrite.RewriteValve" />
    </Host>
    
    
 Start the Tomcat server
 
 Open a browser and go to  http://localhost:8080/reportexporter
