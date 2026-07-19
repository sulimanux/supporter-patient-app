package com.app.sanad.interfaces

import com.app.sanad.getLibraryContent.data.LibraryContent

interface OnItemLibraryContentClicked {

    fun onItemClicked(type: String , index:Int,category:String , currentContent: LibraryContent)
}

