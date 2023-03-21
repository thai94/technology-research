package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"path/filepath"
	"sync"
	"time"
)

var sema = make(chan struct{}, 20)
var done = make(chan struct{})

func walkDir(dir string, w *sync.WaitGroup, fileSizes chan<- int64) {

	defer w.Done()
	if cancelled() {
		fmt.Println("walkDir Stop")
		return
	}
	for _, entry := range dirents(dir) {
		if entry.IsDir() {
			subDir := filepath.Join(dir, entry.Name())
			w.Add(1)
			walkDir(subDir, w, fileSizes)
		} else {
			fileSizes <- entry.Size()
		}
	}
}

func dirents(dir string) []os.FileInfo {

	select {
	case sema <- struct{}{}:
	case <-done:
		return nil
	}

	defer func() {
		<-sema
	}()

	entries, err := ioutil.ReadDir(dir)
	if err != nil {
		fmt.Fprintf(os.Stderr, "err: %v\n", err)
		return nil
	}
	return entries
}

func printDiskUsage(nfiles, nbytes int64) {
	fmt.Printf("%d files %.1f GB\n", nfiles, float64(nbytes)/1e9)
}

func cancelled() bool {
	select {
	case <-done:
		return true
	default:
		return false
	}
}

func main() {

	roots := []string{"/Users/lap14153/"}
	fileSizes := make(chan int64)
	var n sync.WaitGroup
	for _, root := range roots {
		n.Add(1)
		go walkDir(root, &n, fileSizes)
	}

	go func() {
		n.Wait()
		close(fileSizes)
	}()

	var nfiles, nbytes int64
	tick := time.Tick(3 * time.Second)

	go func() {
		os.Stdin.Read(make([]byte, 1)) // read a single byte
		close(done)
	}()

loop:
	for {
		select {
		case <-done:
			for range fileSizes {
				// do nothing
			}
		case <-tick:
			printDiskUsage(nfiles, nbytes)
		case size, ok := <-fileSizes:
			if !ok {
				fmt.Println("break")
				break loop
			}
			nfiles++
			nbytes += size
		}
	}
	printDiskUsage(nfiles, nbytes)
}
