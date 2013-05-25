citeseerx-restore-tool
======================

#### tool to parse citeseerx metadata dump, download papers and save to db

### What is this?
This tool allows:

1. Parse citeseerx metadata dump and import it into mongodb databsse. Metadata contains such info as title, authors, descritions, links to another papers, link to paper and so on.
2. Download papers from links extracted from metadata. There about 700000 linked papers, but some of them are missing.

### Prerequisite
1. MongoDB
2. Gradle
3. A lot of free space (10-600GB)
4. Fast internet connection
5. Download this file http://www.cs.purdue.edu/commugrate/data/citeseer/oai_dc.tar.gz, unpack and run
  
                                cat * > bigfile 


### How to build

1. clone repo
2. go to folder with project
3. run gradle distZip
4. go to build/distribution and run  unzip citeseerx-restore-tool-0.1.zip

### How to run

go to bin/ folder and run

`./citeseerx-restore-tool --import <path to bigfile>` to import data file into mongo db

`./citeseerx-restore-tool --download`    to download files to mongodb. Attention! Total volume of files is really huge(about 300-600GB)! 

You can change default settings in tool.properties file.
