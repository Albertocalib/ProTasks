package com.example.protasks.presenters.task

import android.util.Log
import com.example.protasks.utils.RetrofitInstance
import com.example.protasks.models.*
import com.example.protasks.restServices.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TaskModel : ITaskContract.Model {
    private val retrofitInsUser: RetrofitInstance<UserRestService> =
        RetrofitInstance("api/user/", UserRestService::class.java)
    private val retrofitInsTask: RetrofitInstance<TaskRestService> =
        RetrofitInstance("api/task/", TaskRestService::class.java)
    private val retrofitInsTag: RetrofitInstance<TagRestService> =
        RetrofitInstance("api/tag/", TagRestService::class.java)


    override fun getTasks(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        username: String
    ) {
        val task = retrofitInsTask.service.getTasksByUser(username)
        task.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {
                onFinishedListener.onFinishedSetTasks(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )

            }

        })
    }

    override fun filterTasks(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        name: String,
        username: String
    ) {
        if (name == "") {
            getTasks(onFinishedListener, username)
        } else {
            val tasks = retrofitInsTask.service.getTasksFilterByName(name, username)
            tasks.enqueue(object : Callback<List<Task>> {
                override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
                    onFinishedListener.onFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Task>>?,
                    response: Response<List<Task>>?
                ) {
                    onFinishedListener.onFinishedSetTasks(
                        response!!.isSuccessful,
                        response.code(),
                        response.body()!!
                    )

                }

            })
        }
    }

    override fun getUsers(onFinishedListener: ITaskContract.Model.OnFinishedListener, id: Long) {
        val task = retrofitInsTask.service.getUsersByTask(id)
        task.enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                onFinishedListener.onFinishedSetAssignments(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )

            }

        })
    }

    override fun getUsersInBoard(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        boardId: Long
    ) {
        val board = retrofitInsUser.service.getUsersInBoard(boardId)
        board.enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                onFinishedListener.onFinishedSetUsers(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )

            }

        })
    }

    override fun updateDate(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        id: Long,
        date: Date
    ) {
        val task = retrofitInsTask.service.updateDateEnd(id, date)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }

        })
    }

    override fun addAssignment(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        taskId: Long,
        userId: Long
    ) {
        val task = retrofitInsTask.service.addUserToTask(taskId, userId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())
            }

        })
    }

    override fun removeAssignment(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        taskId: Long,
        userId: Long,
        update: Boolean
    ) {
        val task = retrofitInsTask.service.removeUserToTask(taskId, userId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())
                if (update) {
                    getUsers(onFinishedListener, taskId)
                }
            }

        })
    }

    override fun getTags(onFinishedListener: ITaskContract.Model.OnFinishedListener, id: Long) {
        val task = retrofitInsTag.service.getTagsByTask(id)
        task.enqueue(object : Callback<List<Tag>> {
            override fun onFailure(call: Call<List<Tag>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<Tag>>?, response: Response<List<Tag>>?) {
                onFinishedListener.onFinishedSetTags(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }

        })
    }

    override fun removeTag(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        taskId: Long,
        tagId: Long,
        update: Boolean
    ) {
        val task = retrofitInsTag.service.removeTagToTask(tagId, taskId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())
                if (update) {
                    getTags(onFinishedListener, taskId)
                }

            }

        })
    }

    override fun addTag(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        taskId: Long,
        tagId: Long
    ) {
        val task = retrofitInsTag.service.addTagToTask(taskId, tagId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())
            }

        })
    }

    override fun getTagsInBoard(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        boardId: Long
    ) {
        val board = retrofitInsTag.service.getTagsInBoard(boardId)
        board.enqueue(object : Callback<List<Tag>> {
            override fun onFailure(call: Call<List<Tag>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<Tag>>?, response: Response<List<Tag>>?) {
                onFinishedListener.onFinishedSetTagsBoard(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )

            }

        })
    }

    override fun createTag(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        tag: Tag,
        boardName: String,
        username: String
    ) {
        val tagR = retrofitInsTag.service.createTag(tag, boardName, username)
        tagR.enqueue(object : Callback<Tag> {
            override fun onFailure(call: Call<Tag>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Tag>?, response: Response<Tag>?) {
                onFinishedListener.onFinishedUpdateTag(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!,
                    "Tag created"
                )
            }

        })
    }

    override fun updateTitle(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        id: Long,
        title: String
    ) {
        val task = retrofitInsTask.service.updateTitleTask(id, title)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }

        })
    }

    override fun updateDescription(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        id: Long,
        description: String
    ) {
        val task = retrofitInsTask.service.updateDescriptionTask(id, description)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }

        })
    }

    override fun addAttachedFile(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        id: Long,
        files: HashSet<File>
    ) {
        val task = retrofitInsTask.service.addAttachments(id, files)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }
        })
    }

    override fun removeAttachedFile(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        file: File,
        task: Task
    ) {
        val t = retrofitInsTask.service.removeAttachment(task.getId(), file.getId())
        t.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }
        })
    }

    override fun createSubTask(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        taskP: Task,
        subTask: Task
    ) {
        val task = retrofitInsTask.service.createSubTask(subTask, taskP.getId())
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }

        })
    }

    override fun deleteSubtasks(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        stringSubtasks: String
    ) {
        val t = retrofitInsTask.service.deleteSubTasks(stringSubtasks)
        t.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }
        })
    }

    override fun updatePriority(
        onFinishedListener: ITaskContract.Model.OnFinishedListener,
        id: Long,
        priority: Priority
    ) {
        val task = retrofitInsTask.service.updatePriorityTask(id, priority)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener.onFinishedUpdateTask(
                    response!!.isSuccessful,
                    response.code(),
                    response.body()!!
                )
            }

        })
    }


}