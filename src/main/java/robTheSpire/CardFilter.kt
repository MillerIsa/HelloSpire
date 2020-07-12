package robTheSpire

import org.clapper.util.classutil.ClassFilter
import org.clapper.util.classutil.ClassFinder
import org.clapper.util.classutil.ClassInfo

class CardFilter : ClassFilter {
    override fun accept(classInfo: ClassInfo, classFinder: ClassFinder): Boolean {
        return classInfo.className.startsWith(PACKAGE)
    }

    companion object {
        private const val PACKAGE = "robTheSpire.cards."
    }
}