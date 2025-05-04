# 命名
- 配列を表現する変数名は複数形の名詞を使わないようにする
- XXListのように、接尾語にListをつける

# UnitTest
- DataレイヤーとDomainレイヤーにクラスを作成した際、対応するユニットテストは常に作成すること
- テストケース名は日本語とする

# Presentationレイヤー
- Screen/Content, Section, Componentという３つのレベルでレイアウトを分割する方針とする
- このレベルは記載順に高いものとなっており、低レベルのレイアウトは高レベルのレイアウトを呼び出せない
- Screen/Contentは最も大きいUI単位で、SectionとComponentを呼び出すことができる
- Sectionはその次に大きいUI単位で、Componentを呼び出すことができる
- Componentは最も小さいUI単位で他のComposable関数は呼び出せない。小さいUIをprivate関数で持つことがある
- 上記を基本として、適宜JetpackComposeデフォルトのウィジェットや、自前の小さなprivate関数を組み合わせてUIを構築する

## Screen/Contentについて
- ScreenとContentは1画面全体に相当するComposable関数である
- ScreenではViewModelの初期化とuiStateの取り出し、Content関数の呼び出しのみを行う
- 特に、ViewModelに依存したUIはPreviewが不可能になるため、ScreenでのみViewModelを参照するようにする
- Contentより下の階層にViewModel依存を生むことは絶対に避けるためにViewModelへの依存を分離するは、Screenの最も重要な責務の一つである
- state hoistingに則り、Content以下のレイヤーで起きたイベントは全てScreenに集約し、ScreenからViewModelの関数を呼び出すことでUIイベントとのインタラクトと状態の更新を実現する
- Content関数で具体的なUI実装を行う

## Componentについて
- 最も小さなレベルのUIパーツになる
- テキスト、ボタン、ラベル、リストの１要素、といった大きさのものがこれに該当する
- 上記四つ以外に該当するものもある

## Sectionについて
- Screen/Contentより一段小さな大きさのUIを構築する
- 大きさの定義が難しいが、Componentが集まることで構築されるもので、アイテムのリストやトップバー、ボトムバー、Drawerメニューといったものが該当する

## Preview関数について
- Composeで組んだ画面はScreen以外全てに対してpreview関数を実装すること
- Preview関数の命名は接頭語にPreviewを付けて、"Preview{関数名}"という形式を基本とする

# Domainレイヤー
- domainレイヤーに設置するデータを表現するクラスの接尾語は"DomainData"とする
- UseCaseが公開する関数はinvoke関数のみとする。その他privateメソッドは適宜作成しても良い
- UseCaseの返却する結果はResultでラップしない。意図せぬ結果については例外をスローする
- この時、例外クラスはシナリオに合わせて作成する

# Dataレイヤー
- Repositoryには基本インターフェースを用意しない代わりに、ApiやDBに接続するDataStoreの差し替えでテスタビリティを確保する