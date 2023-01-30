package main

func makeThumbnails4(filenames []string) {
	errors := make(chan error)
	for _, f : range filenames {
		go func(f string) {
			_, err : thumbnail.ImageFile(f)
			errors <- err
		}
	}

	
}
