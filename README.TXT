AUTHORS:            John Geddes
PROJECT TITLE:      Genetic AI 2D
PURPOSE OF PROJECT: To learn how genetic programming can be applied to video games and AI.
VERSION or DATE:    Nov 5, 2013 - Mar 3, 2014
HOW TO START THIS PROJECT:
  1)  Download the 2D graphics program "Greenfoot" at http://www.greenfoot.org/download
  2)  Download all files in this repository and place into a single folder
  3)  Launch Greenfoot and load "project.greenfoot"
USER INSTRUCTIONS:
  1)  After loading the Greenfoot project, wait for the world to load. You should see 11 arrows lining the bottom and a brick at the top.
  2)  Right-click the black background to open the context menu and click the option "Inspect."
    a)  The menu that opens displays the "Background" class data. If you want to look at the scores of the Shooters, double-click the arrow labeled "private ArrayList<Shooter> shooters." Then dig into the array with the next arching arrows. The labels should be self-explanatory.
  3)  Click the "Run" button to start the simulation.
    a)  If you want to speed up the simulation, move the "Speed" slider futher to the right.
  4)  [Broken] The simulation is limited to 100 generations, because by about 50 generations, all of the Shooters follow the same path (and are therefore almost identical). The limit is needed because the values of the AI will not change much once all Shooters follow the same path.
  5)  [Broken](For the number lovers) Now look at the "scores" ArrayList inside each Shooter. Each Shooter tracks its scores for the current generation. Scores are calculated based on how close each bullet got to the Target or if the bullet hit the Target. At the end of the first few geneartions, the scores for every Shooter should be vastly different. After 100 generations, the scores for every Shooter should be almost identical.

-----------------------------------------------------------------------------------------------------------

EXPLANATION OF THE BROKEN STATE OF THE PROJECT:
  I built this project for my junior year of high school for an annual science fair project required by my high school. I built the project over the course of 4 months, but I didn't publish the progress on GitHub due to it being a private project that I was submitting to regional and national science fairs. I printed most of the finished project in an appendix of my research paper (this is important). After I finished the project, I left it on my laptop and still never uploaded it to the cloud. Around May of 2014, I tried to install Linux on my laptop to dual-boot with Windows. I did everything right, including creating hard drive partitions for both operating systems and making sure the bootloader on my laptop let me boot either OS. Shortly after I finished installing Ubuntu, it asked to update. Here's where I messed up.
  When Ubuntu wanted to update, I chose the option "Remove current Ubuntu installation and install new version." I interpreted this as "Ubuntu is going to remove the current installation on the Linux partition." I was incredibly wrong. Ubuntu ended up wiping my entire hard drive just to install Linux. This meant all of my previous papers, projects, programs, pictures, music, and video game saves were gone. I didn't back up my super-important data, and I lost this project.
  Luckily, there was still a copy of my code in the research paper, which only existed in print form. After aquiring a legitimate copy of Windows 8.1 from a friend, I manually copied all 700+ lines of code onto my now-fresh laptop. Unfortunately, when I printed the paper with the code, I was in the process of optimizing the algorithms, but some code didn't quite work. So the new project had some big bugs that would require recreating my previous, working code.
