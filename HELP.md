# DEVELOPER GUIDE - Getting Started with a new GitHub project.

### Step 1 - Generate Spring Boot Project spring initializr
https://start.spring.io

### Step 2 - Initialize Git Locally
cd c:\Users\[login_user]\IdeaProjects\[project_folder]\
git init
echo "target/" > .gitignore  # Add other ignored paths too
git add .
git commit -m "Initial Spring Boot project"

### Step 3 - Create a GitHub Repository 
Go to GitHub ? New Repository
Don’t initialize with README, .gitignore, or license (to avoid conflicts)

### Step 4 - Link Local Repo to GitHub
git remote add origin https://github.com/[github_user]/store.git
git branch -M main
git push -u origin main