package com.example.todo

import java.io.Serializable

class DetailTodo : Serializable {
    private var id_: Int = 0
    private var title_: String = ""
    private var isComplete_: Boolean = false
    private var content_: String = ""

    public var id:Int
        get() {
            return id_
        }
    set(value) {
        id_ = value
    }
    public var title: String
        get() {
            return title_
        }
        set(value) {
            title_ = value
        }
    public var isComplete: Boolean
        get() {
            return isComplete_
        }
        set(value) {
            isComplete_ = value
        }
    public var content: String
        get() {
            return content_
        }
        set(value) {
            content_ = value
        }
    constructor(){}
    constructor(id: Int,title:String,isComplete:Boolean = false,content:String){}
}