package com.app.sanad.users.supporter.tools.cofe.data.repo

import android.content.Context
import com.app.sanad.R

class Repository {
      fun getPhrases(context: Context): List<String> {
          return listOf(
              context.getString(R.string.phrase1),
              context.getString(R.string.phrase2),
              context.getString(R.string.phrase3),
              context.getString(R.string.phrase4),
              context.getString(R.string.phrase5),
              context.getString(R.string.phrase6),
              context.getString(R.string.phrase7),
              context.getString(R.string.phrase8),
              context.getString(R.string.phrase9),
              context.getString(R.string.phrase10),
              context.getString(R.string.phrase11)
          )
      }

}