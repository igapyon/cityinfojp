# cityinfojp

cityinfojp は自治体などの組織による都市情報のファクトを淡々と集積する Webサイトです。 
当面は日本の行政(県・市・町・村)からの外出自粛要請や学校の休校情報などの情報を収集して表示します。

## About

cityinfojp について詳しくは [Aboutページのソース](src/main/resources/templates/dyn/about.html)を参照してください。

## ビルド・実行環境

- Spring Boot 4を使うため、ビルドとアプリの実行にはJDK 17以上が必要です。
- cityinfojp自身のJava classは`maven.compiler.release=8`で生成し、major version 52を保ちます。Spring Boot/Spring Frameworkなどの実行時依存はJava 17以上を要求するため、アプリ全体をJava 8 runtimeで実行できるという意味ではありません。

## ビルドとローカル起動

現在の開発版は`1.2.0`です。JDK 17以上とMaven 3.6.3以上でビルドします。

```sh
mvn --batch-mode --no-transfer-progress clean verify
java -jar target/cityinfojp-1.2.0.jar --server.address=127.0.0.1 --server.port=8080
```

起動後は`http://127.0.0.1:8080/dyn/about.html`などを開きます。配布JARの内容と、ソース外からの起動・HTTP応答はPython 3で確認できます。

```sh
python3 scripts/smoke-release.py target/cityinfojp-1.2.0.jar target/cityinfojp-1.2.0-sources.jar
```

GitHub Releaseには`cityinfojp-<version>.jar`（依存込みBoot実行JAR）と`cityinfojp-sources-<version>.jar`を添付します。`v*`タグのpushで、タグとPOMの版の一致、テスト、配布JARの起動を確認して公開し、公開後にダウンロードしたJARのSHA-256と起動も確認します。

詳しいビルド・検証・公開手順は[docs/OPERATIONS.md](docs/OPERATIONS.md)を参照してください。`workplace/`はGit管理外のローカル作業領域です。

## 初期のホスティング先

Herokuで公開していたサイトは閉鎖済みです。以下のURLは過去の参照先として残しています。

- https://cityinfojp.herokuapp.com/

## ソース

ソースコード一式は GitHub 上に公開しています。Apache ライセンスで公開します。

- https://github.com/igapyon/cityinfojp

## データ

データは、クリエイティブコモンズ 表示 4.0 国際 (CC BY 4.0) で収集・公開します。

# リリース

## 1.2.0（開発中）

- Spring Boot 4.0.9、Spring Framework 7.0.10、Tomcat 11.0.27へ移行し、Commons IO 2.22.0などの更新を反映。
- 実コンテキストの起動テスト、依存込み実行JAR・sources JAR生成、タグ起点のReleaseと公開JARの起動確認を追加。
- 実行JAR内でAbout等のテンプレートを解決できるよう、コントローラが返すビュー名を修正。

## 1.1 (2020-04-25)

### 機能追加

* 都道府県ページに、ページャーを追加して別の（隣の）都道府県にジャンプ可能に
* メニューに現れないHTML画面が存在しているが、これをメニューのドロップダウンに表示されるようにする
* Data Entry 画面の種別に閉鎖を追加
* cityinfojp に登録されたデータについて、RSS v2.0 (ファイル拡張子 .rss) で提供する
* index.html (Home) の最近登録された情報一覧の箇所に、クラウドからダウンロードのアイコンを追加して統合後の JSON ファイルにリンク
* Area ページにも、そのエリアの新着情報リストを表示するようにする
* 都道府県のページに「公式サイト」のリンクを追加
* 登録内容が信頼できる情報源に基づいているか判別できるようにアイコンを追加する

### 機能調整

* Jumbotron を少し小さくする
* Navbar 左上エリアをクリックすると ./index.html を表示するように変更する
* プログラム、データに対する最初のコントリビュータについて contributor.htmlに記載を追加実施
* sitemap.xml に未登録の情報があった (エリアなど) のでこれを追加
* アイコンの大きさを 1em から 1.5emに増やす

### バグ修正

* error.html の作成・配置して、エラーの際に Spring の生ログが表示されないようにする
* Jumbotron のタイトル文字列が iPhone SE で表示すると一部かける不具合の訂正
* アイコンの (!) と (i) の混同がみられたのでこれを訂正

### リファクタリング

* Javadoc を記載する
* ソースコード C1 カバレッジを確認してデッドコードを除去
* DOCTYPEを大文字化
* 画面各所に現れる svg タグについて、Thymeleaf フラグメント化して共通化を実施する
* ソースの TODO コメントについて、解消して削除できるものはこれを対応する
* ThymVarMapAreaBuilder の AREA_INFO, AREA_PREF_CODES を廃止して JSONベースに変更

## 1.0 (2020-04-19)

* 最初のリリース
