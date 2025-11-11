# Verify GitHub Actions Setup

## ✅ Checklist Before Pushing

Run these commands to verify everything:

### 1. Test Locally Works
```
mvn clean test
```
Expected: `Tests run: 7, Failures: 0, Errors: 0` ✅

### 2. Verify .gitignore Protects Token
```
git status
```
Expected: `application-local.properties` should NOT appear ✅

### 3. Verify application.properties is Safe
```
cat src/main/resources/application.properties | grep "api.auth.token"
```
Expected: `api.auth.token=TOKEN` (NOT your real token) ✅

### 4. Check Branch Name
```
git branch
```
Expected: Should be `main` or `master` ✅

---

## 🚀 After Pushing to GitHub

### Step 1: Add Secret (ONE TIME)

Go to: https://github.com/HarshalaWagh/APIProject/settings/secrets/actions

Add:
- **Name:** `GOREST_TOKEN`
- **Value:** Your token from gorest.co.in

### Step 2: Trigger Workflow

**Option A: Push code**
```
git push origin master
```

**Option B: Manual trigger**
1. Go to: https://github.com/HarshalaWagh/APIProject/actions
2. Click "API Tests" workflow
3. Click "Run workflow"
4. Select branch: master
5. Click "Run workflow"

### Step 3: View Results

Go to: https://github.com/HarshalaWagh/APIProject/actions

You should see:
- ✅ Green checkmark
- Tests run: 7
- All passed

---

## 🐛 If Tests Fail on GitHub

### Check 1: Is Secret Set?
Settings → Secrets → Actions → Should see `GOREST_TOKEN`

### Check 2: View Error Logs
Actions → Click failed run → Click "test" job → Read error

### Check 3: Common Errors

**Error: `Authorization=Bearer `** (empty)
- Fix: Secret not configured properly
- Secret name must be exactly: `GOREST_TOKEN`

**Error: `401 Unauthorized`**
- Fix: Token is invalid or not set
- Regenerate token from gorest.co.in

**Error: `application-local.properties not found`**
- This is NORMAL on GitHub (file is local only)
- Framework should use environment variable instead

---

## ✅ Success Looks Like

```
✓ Checkout code
✓ Set up JDK 17
✓ Run tests (7 tests, 0 failures) ✅
✓ Generate test report
✓ Upload test results
```

---

## 💡 Quick Debug

Copy this and paste in GitHub Actions error if tests fail:

1. Look for line with `Authorization=Bearer`
2. If it shows your token → Secret working ✅
3. If it shows `Bearer ` or `Bearer TOKEN` → Secret NOT working ❌

---

**Most likely fix: Just add GOREST_TOKEN to GitHub Secrets!**

