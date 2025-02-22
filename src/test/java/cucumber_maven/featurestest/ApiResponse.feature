@data
Feature: Get book by ISBN

  Scenario: User calls web service to get a book by its ISBN
    Given a book exists with an isbn of "9781451648546"
    When a user retrieves the book by isbn
    Then the status code is 200
    And response includes the following
      | totalItems          | 1                  |
      | kind                | "books#volumes"      |
    And response includes the following in any order
      | items.volumeInfo.title        | Steve Jobs            |
      | items.volumeInfo.publisher    | Simon and Schuster    |
      | items.volumeInfo.pageCount    | 656                  |
      
  Scenario: User retrieves a book and validates the publisher date field
    Given a book exists with an isbn of "9781451648546"
    When a user retrieves the book by isbn
    Then the status code is 200
    And response includes the following in any order
      | items.volumeInfo.publishedDate | 2011 |

  Scenario: User retrieves a book and validates the language field
    Given a book exists with an isbn of "9780451495081"
    When a user retrieves the book by isbn
    Then the status code is 200
    And response includes the following
      | totalItems          | 1                   |
      | kind                | books#volumes       |
    And response includes the following in any order
      | items.volumeInfo.title        | Steve Jobs         |
      | items.volumeInfo.language     | en                |

  Scenario: User retrieves a book and validates the ISBN field in response
    Given a book exists with an isbn of "9781451648546"
    When a user retrieves the book by isbn
    Then the status code is 200
    And response includes the following in any order
      | items.volumeInfo.isbn  | 9781451648546 |

  Scenario: User validates the response for an invalid ISBN (negative test)
    Given no book exists with an isbn of "0000000000000"
    When a user retrieves the book by isbn
    Then the status code is 400
    And response includes the following
      | error | "Invalid ISBN" |
