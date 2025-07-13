# Dataモジュールの構成

## ディレクトリ構造
```
Data/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── neesan/
│   │               └── data/
│   │                   ├── feature/                  # 機能単位のパッケージ
│   │                   │   ├── XxxDao.kt            # Room DAO
│   │                   │   ├── XxxEntity.kt         # Room Entity
│   │                   │   └── XxxRepository.kt     # リポジトリ実装
│   │                   ├── AppDatabase.kt           # Roomデータベース定義
│   │                   ├── AppDatabaseModule.kt     # データベースDIモジュール
│   │                   ├── CoroutineDispatcherModule.kt # コルーチンディスパッチャーDIモジュール
│   │                   └── DataModule.kt            # データモジュールのDI設定
│   └── test/                                        # テストコード
```

## 命名規則
- DAOクラスは`XxxDao`という命名規則に従う
- Entityクラスは`XxxEntity`という命名規則に従う
- Repositoryクラスは`XxxRepository`という命名規則に従う

## 依存性注入
- DaggerHiltを使用して依存性注入を実装
- モジュールは`XxxModule`という命名規則に従う
- シングルトンコンポーネントにインストールするモジュールは`@InstallIn(SingletonComponent::class)`を使用

## コルーチン
- Repositoryは外部からCoroutineDispatcherを受け取る
- データベース操作は指定されたディスパッチャーで実行
- Flowの処理は`flowOn`を使用してディスパッチャーを指定
- suspend関数は`withContext`を使用してディスパッチャーを指定

## テスト
- DAOのテストはin-memoryデータベースを使用
- テスト用のディスパッチャーは`UnconfinedTestDispatcher`を使用
- テストケース名は日本語で記述
- Hilt依存性注入を使用してモックではなくプロダクションコードでテスト実行
- `@HiltAndroidTest`、`@Config(application = HiltTestApplication::class)`、`@RunWith(AndroidJUnit4::class)`を使用
- `HiltAndroidRule`を設定し、Hiltルールを注入
- `runWithDescription`関数を使用してテストケースを記述
- 必要に応じて他のUseCaseやRepositoryも`@Inject`で注入し、実際のデータ追加・削除操作を含む統合テストを実施

## データベース
- Roomを使用してローカルデータベースを実装
- データベース名は`nico_key_shifter.db`
- エンティティは`@Entity`アノテーションで定義
- DAOは`@Dao`アノテーションで定義
- データベースは`@Database`アノテーションで定義 