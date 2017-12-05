See https://www.reddit.com/r/Lightbulb/comments/7cfe6r/make_a_free_ebook_for_each_wp_from_rwritingprompts/

# Installation

Register an account at AWS and create a S3 bucket with public read access. Create an identity with write access
to that bucket. Set these properties in application.properties:

- jclouds.identity: Access key ID
- jclouds.credential: Secret Access key ID
- jclouds.formats.container: bucket name


# Roadmap

## PoC - Generate an E-Book

- [x] Convert [a comment](https://www.reddit.com/r/WritingPrompts/comments/7cev3m/wp_seeing_success_with_the_purchase_of_marvel_and/dppezxn/) into an EPUB book (See 
[epublib](http://search.maven.org/#search%7Cga%7C1%7Cepublib)
- [x] Convert EPUB into AZW (See [Calibre](https://www.calibre-ebook.com/) or [KindleGen](https://www.amazon.com/gp/feature.html?docId=1000234621))


## Shared

- [ ] Integrate blocking Reddit API to respect their [rate limit](https://github.com/reddit/reddit/wiki/API#rules)

### Integration

- [ ] Choose an event handler system (e.g. Spring or Akka)
- [ ] Implement PublicationPermitted handler in PublishCommentAsEBookApplicationService.publish()
- [ ] Implement EBookPublished handler in PublishEBookApplicationService.publish()
- [ ] Implement EBookRevoked handler in UnpublishEBookApplicationService.unpublish()

## E-Book generator

- [x] Design model
- [x] Implement PublishRedditCommentApplicationService
- [x] Implement UnpublishEBookApplicationService
- [ ] Implement RedditCommentRepository
- [x] Implement EBookFactory for EPUB
- [x] Implement EBookFactory for MOBI
- [x] Implement PublishedFormatJCloudsRepository
- [ ] Implement an integration test for publishing a comment on S3

## Reddit bot

- [x] Design model
- [x] UC: Identify new comments and ask author for permission
- [x] UC: Understand author's permission reply and start EBook publication
- [x] UC: Understand author's unpublish whish and start Unpublishing
- [ ] Implement InboxMessageRepository
- [ ] Implement NLPService
- [ ] Implement WritingPromptRepository
- [ ] Implement AnswerCommentService

