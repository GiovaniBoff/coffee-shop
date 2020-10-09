# Coffee Shop #

## Build & Run ##

```sh
$ cd coffee-shop
$ sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

## Starting the semantic server

```sh
cwd: coffee-shop
$ sbt -Dsbt.semanticdb=true
```

This will open a sbt server that will help `VSCode Metals` with it's language server.