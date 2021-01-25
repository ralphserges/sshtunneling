# sshtunneling

fyp objective: create a gui that perform ssh tunneling using local port forwarding,
                exchange file by dragging and dropping files between local and remote file system via SFTP.
                
Tools to download: Netbean IDE
Programming language:  Java 11

4 components for this project
-----------------------------
  1. SSH initialization.
  This stage involves password authentication to remote host and creates session 
  that supports local port forwarding

  2. Tree-like display of Local and remote file system.
  This stage displays all files and folders on local desktop directory and on remote host directory
  in tree-like structure. this is for user to navigate through files and folders. 

  3. Drag and Drop between local and remote file systems.
  This feature allows user to drag and drop files/folders onto local or remote file system. Behind the scene,
  the program will perform SFTP to do the transferring. 

  4. Logging system.
  This system logs user activities onto the program console for user's information.
  It also logs to remote database so that authority can utilize the logs for investigation
  for suspicious or malicious activities. 
