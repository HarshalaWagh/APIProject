# GitHub Actions Troubleshooting

## ✅ Pre-Push Checklist

Before pushing to GitHub, verify:

- [ ] `application-local.properties` is in `.gitignore` ✅
- [ ] `application.properties` has placeholder token (not real one) ✅
- [ ] All tests pass locally: `mvn clean test` ✅
- [ ] Workflow files exist in `.github/workflows/` ✅

## 🔧 GitHub Actions Setup Steps

### Step 1: Add GOREST_TOKEN to GitHub Secrets

1. Go to: https://github.com/HarshalaWagh/APIProject/settings/secrets/actions
2. Click: **New repository secret**
3. Add:
   - Name: `GOREST_TOKEN`
   - Secret: `your_token_from_gorest`
4. Save

**Important:**
- Secret name must be EXACTLY: `GOREST_TOKEN` (all caps, no spaces)
- Use the same token that works locally

### Step 2: Push Your Code

```
git add .
git commit -m "Add REST Assured API tests with GitHub Actions"
git push origin master
```

### Step 3: Verify Workflow Runs

1. Go to: https://github.com/HarshalaWagh/APIProject/actions
2. Look for workflow run
3. Should see green checkmark ✅

## 🐛 Common Errors & Solutions

### Error: "401 Unauthorized"

**Cause:** GOREST_TOKEN secret not set or incorrect

**Fix:**
1. Go to repo Settings → Secrets → Actions
2. Check if `GOREST_TOKEN` exists
3. If not, add it
4. If exists, regenerate token at gorest.co.in and update secret

### Error: "Secret GOREST_TOKEN not found"

**Cause:** Secret name typo

**Fix:**
- Must be exactly: `GOREST_TOKEN` (not gorest_token or GoRest_Token)
- Recheck spelling in GitHub Secrets

### Error: "Tests run: 0"

**Cause:** Maven not finding test files

**Fix:**
- Check `pom.xml` has maven-surefire-plugin
- Verify test files end with `*Tests.java`

### Error: "Cannot resolve imports"

**Cause:** Missing dependencies

**Fix:**
- Verify all dependencies in `pom.xml`
- Check Spring Boot parent is present

## 🔍 How to Debug

1. **View Logs:**
   - Actions tab → Click failed run → Click "test" job → Read logs

2. **Check Token:**
   - Look for: `Authorization=Bearer YOUR_TOKEN_HERE`
   - If yes → Secret not configured
   - If shows actual token → Secret configured correctly

3. **Test Locally First:**
   - If tests fail locally, they'll fail on GitHub
   - Always run `mvn clean test` locally first

## ✅ Verification

Your GitHub Actions is working correctly when you see:

```
✅ API Tests
   ├── ✅ getAllUsers
   ├── ✅ createUser
   ├── ✅ getUserById
   ├── ✅ updateUser
   ├── ✅ partialUpdateUser
   ├── ✅ deleteUser
   └── ✅ completeUserLifecycle

Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

## 📞 Still Having Issues?

Copy the error from GitHub Actions logs and check:
1. Is GOREST_TOKEN in Secrets?
2. Does local test pass with same token?
3. Is workflow file in `.github/workflows/` folder?

---

**Most common issue: Forgot to add GOREST_TOKEN to GitHub Secrets!**

