# Magic the Gathering API Client
This repository contains a reactive client to the [REST APIs](https://magicthegathering.io/) serving
up Magic: the Gathering card information. The intent is to allow consumers to retrieve card data in
as a Java object, and to allow them to read pages of data rather than relying on a synchronous process
that tries to retrieve all ~10k cards at once.

Planned enhancements to the client:
* Retrieval of card by name or gatherer ID
* More card information, e.g. links to images
* Retrieval of card by format legality, e.g. Standard, Modern, Commander

The ultimate goal of this client is to have it consumed by a REST service that can serve processing
for web applications.
