#
# To build the Go examples, you need to have the Go compiler installed, and then run:
#
#    $ make build-go-examples
#

.PHONY: build-go-examples
build-go-examples:
	@find ./src/test/go-examples -mindepth 1 -type f -name "main.go" \
	| xargs -I {} bash -c 'dirname {}' \
	| xargs -I {} bash -c 'cd {} && env GOOS=wasip1 GOARCH=wasm go build -buildmode=c-shared -o main.wasm ./main.go'
