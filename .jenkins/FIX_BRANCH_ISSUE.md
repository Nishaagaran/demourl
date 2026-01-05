# Fix Jenkins Git Branch Issue

## Problem
Jenkins is trying to checkout from `master` branch, but your repository uses `main` branch.

## Solution 1: Update Jenkins Job Configuration (Recommended)

1. Go to your Jenkins job
2. Click **Configure**
3. Scroll down to **Pipeline** section
4. Under **Pipeline script from SCM**, find **Branches to build**
5. Change the branch specifier from:
   - `*/master` or `master`
   - To: `*/main` or `main`
6. Click **Save**
7. Run the pipeline again

## Solution 2: Update Jenkinsfile (Already Applied)

The Jenkinsfiles have been updated to handle the `main` branch. However, you still need to update the Jenkins job configuration to use the correct branch.

## Solution 3: Create a master branch (Not Recommended)

If you must use `master` branch:
```bash
git checkout -b master
git push origin master
```

## Verification

After updating, the pipeline should:
- Successfully checkout the `main` branch
- Build the application
- Run tests
- Package the JAR

## Common Branch Names

- `main` (most common in new repositories)
- `master` (older repositories)
- `develop` (development branch)
- `trunk` (SVN-style)

Make sure your Jenkins job branch configuration matches your actual repository branch name.


