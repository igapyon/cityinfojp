

# Google 登録

https://search.google.com/search-console/


## git 用の設定 (自分用)

```sh
git config user.email "igapyon@gmail.com"
git config user.name "Toshiki Iga"
```

```sh
cd /tmp
heroku login
```

```sh
heroku git:clone -a cityinfojp
cd cityinfojp
cp -pv ~/Documents/gitiga/cityinfojp/pom.xml .
cp -pvR ~/Documents/gitiga/cityinfojp/src .
```

```sh
git status
git add .
git commit -am "make it better"
git push heroku master
```
