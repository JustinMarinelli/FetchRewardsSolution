This is my solution for the coding assessment given to me by Fetch Rewards.

Thank you very much for giving me this opportunity. I would like to make a couple comments on my solution.

The data is stored at the given URL with 3 fields per row - id, listId, and name. I am aware that for rows that have non-null and non-empty names, the id seems a bit redundant as it is already included in the name, for example: {"id":0, "listId": 1, "name": "Item 0"} However, I did not want to omit the ID field for a specific reason. If this software were to be used in the future for another data set, perhaps there could be a row that has another form, such as {"id":1, "listId":1, "name": "DifferentItem 0"}. Notice how the id and the number in the name are not equal. Because of this conern, I decided to include all fields in the View.

One last comment. I have added a couple of Instrumented Tests that you can feel free to run. These were testing if my sorting function worked as expected, and they all passed just before my last commit, so everything should be working properly!

Thank you once again for giving me this opportunity. I find mobile developement to be quite fun, so I really enjoyed this assessment. I really look forward to hearing back about the next steps in the process.