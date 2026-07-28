nouns:
- books
- members
- categories
- transactions


list all books GET /books
list all book under categories GET /categories/{id}/books
get members GET /members/{id}
borrow/return  POST /members/{id}/transactions
    {book_id: ....
    type: borrow/return}



    POST /transactions
    {book_id:..., member_id:...., type...}