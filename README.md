# cityinfojp

cityinfojp は、日本の都市情報のまとめサイトを目指すプロジェクトです。
当面は日本の行政(県・市・町・村)からの外出自粛要請や学校の休校情報などの情報を整理して表示します。

入力情報を pull request していただくことを歓迎します。
いただいた情報は Apache ライセンスのもとで扱われます。

## 初期の実働サイト

- 成果物は Heroku 上でホストしてます。
- https://cityinfojp.herokuapp.com/

## ソースコード

- https://github.com/igapyon/cityinfojp


## TBD
- 日付の from to を含めて pref レベルの状態表示
- /pref/city/ について実装。
- https://www.open-governmentdata.org/ を調べる

# 自分用の開発メモ

## テスト実行

mvn test にて統合 JSON ファイルが更新され、静的 HTML ファイルも更新されます。当面 test は 2回実行してください。

```sh
mvn test
mvn spring-boot:run
```
