For collaborators to connect to the Git repository and work on the project in Android Studio, they need to follow these steps:

Step 1: Clone the Repository

Open Android Studio.
From the Welcome Screen or from the top menu: Go to File → New → Project from Version Control.
Select Git.
Enter the Repository URL: Provide the Git repository URL “https://github.com/Jandalslap/MIOK_Quick_Response_App”
Select the Directory: Choose a local directory where they want to clone the project.
Click Clone.
Step 2: Configure Git in Android Studio (If Not Set Up Already)

Set Git Executable: Go to File → Settings (or Preferences on Mac). Navigate to Version Control → Git. Ensure the Path to Git executable is correctly set (e.g., /usr/bin/git on Mac/Linux or C:\Program Files\Git\bin\git.exe on Windows).
Test Connection: In the same window, click Test to verify that Git is working correctly.
Step 3: Commit, Pull, and Push Changes

Make Changes: After cloning, they can modify files as needed.
Pull Latest Changes: Go to Git → Pull to get the latest updates from the repository.
Add Changes: Select the files to add by right-clicking on them → Git → Add.
Commit Changes: Use Ctrl + K (or Git→ Commit) to commit changes.
Push Changes: Use Ctrl + Shift + K (or Git → Push) to push changes to the repository.
Step 4: Resolve Conflicts (If Any) • If two collaborators edit the same file and there’s a conflict, they will be prompted to resolve the conflict within Android Studio's Merge Tool
