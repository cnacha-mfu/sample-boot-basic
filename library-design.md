nouns:
- books
- members
- categories
- transactions


list all books GET /books
list all book under categories GET /categories/{id}/books
get members GET /members/{id}
borrow/return  POST /members/{id}/transactions
    {book: {id: ...},
    type: borrow/return}



    POST /transactions
    {book: {id:...}, member: {id:...}, type...}


note: the related object is sent as a nested object holding only its id
({"book": {"id": 3}}), not as book_id. That is what Jackson needs to build the
Transaction entity, which holds a Book object rather than a number.
