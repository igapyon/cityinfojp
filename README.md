# cityinfojp

県・市など地方自治体からの情報について、事実そのままに集約するためのサービスです。
当面の間は、自治体の情報そのままに掲載することを目指します。
入力情報の pull request は喜ばれます。
いただいた情報は Apache ライセンスのもとで扱われます。


成果物は Heroku 上でホストしてます。
https://cityinfojp.herokuapp.com/



## TBD
- レコードの明細部分を強化
- 日付の from to を含めて pref レベルの状態表示
- about.html を作成
- ジャンボトロンのボタンを有効化
- トップメニューのフラグメント化
- /pref/city/ について実装。
- https://www.open-governmentdata.org/ を調べる

# 自分用の開発メモ

## テスト実行

```sh
mvn spring-boot:run
```

## git 用の設定 (自分用)

```sh
git config user.email "igapyon@gmail.com"
git config user.name "Toshiki Iga"
```
