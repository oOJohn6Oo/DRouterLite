package io.john6.router.drouterlite

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(RouterTest::class, ServiceTest::class)
class DRouterLiteTestSuite