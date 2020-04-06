# cityinfojp

cityinfojp は、日本の都市情報のまとめサイトを目指すプロジェクトです。
当面は日本の行政(県・市・町・村)からの外出自粛要請や学校の休校情報などの情報を整理して表示します。

入力情報を pull request していただくことを歓迎します。
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
