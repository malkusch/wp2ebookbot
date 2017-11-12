See https://www.reddit.com/r/Lightbulb/comments/7cfe6r/make_a_free_ebook_for_each_wp_from_rwritingprompts/

# Roadmap

## PoC - Generate an E-Book

- [ ] Convert [a comment](https://www.reddit.com/r/WritingPrompts/comments/7cev3m/wp_seeing_success_with_the_purchase_of_marvel_and/dppezxn/) into an EPUB book (See 
[epublib](http://search.maven.org/#search%7Cga%7C1%7Cepublib)
- [ ] Convert EPUB into AZW (See [Calibre](https://www.calibre-ebook.com/) or [KindleGen](https://www.amazon.com/gp/feature.html?docId=1000234621))


## E-Book generator

- [ ] Design model
- [ ] Build EBook from Reddit Comment
- [ ] Convert to EPUB
- [ ] Validate generated EPUB with [EpubCheck](https://github.com/IDPF/epubcheck)
- [ ] Convert to AZW
- [ ] Publish on S3
- [ ] Implement PublishRedditCommentApplicationService

## Reddit bot

- [ ] Design model
- [ ] UC: Identify new comments and ask author for permission
- [ ] UC: Understand author's "Yes" reply and start EBook generation
- [ ] UC: Publish generated EBooks

