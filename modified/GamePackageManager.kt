package org.levimc.launcher.core.minecraft

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadLibraryDetailed(name: String): LibraryLoadResult {
        val fileName = toLibraryFileName(name)
        val normalizedName = normalizeLibraryName(name)
        val startedAt = SystemClock.elapsedRealtime()

        if (systemLoadedLibs.contains(fileName)) {
            val source = if (normalizedName == "gxcore") {
                "launcher bundled bootstrap"
            } else {
                "launcher bundled library"
            }
            try {
                if (normalizedName == "gxcore") {
                    if (!NativeBridgeHelper.bootstrapGxCore()) {
                        val detail = "gxcore bootstrap failed"
                        Log.e(TAG, "Failed to load $fileName from $source: $detail")
                        return LibraryLoadResult(normalizedName, fileName, source, false, elapsedSince(startedAt), detail)
                    }
                } else {
                    System.loadLibrary(normalizedName)
                }
                return LibraryLoadResult(
                    normalizedName,
                    fileName,
                    source,
                    true,
                    elapsedSince(startedAt)
                )
            } catch (e: UnsatisfiedLinkError) {
                val detail = e.message ?: e.javaClass.simpleName
                Log.e(TAG, "Failed to load $fileName from $source: $detail")
                return LibraryLoadResult(normalizedName, fileName, source, false, elapsedSince(startedAt), detail)
            } catch (e: Exception) {
                val detail = e.message ?: e.javaClass.simpleName
                Log.e(TAG, "Failed to load $fileName from $source: $detail")
                return LibraryLoadResult(normalizedName, fileName, source, false, elapsedSince(startedAt), detail)
            }
        }

        ...
    }

=====
=====

    init {
        report("GamePackageManager init started")

        if (version != null && !version.isInstalled) {
            report("Using bundled Minecraft version")

            packageContext = context

            applicationInfo = MinecraftLauncher(context)
                .createFakeApplicationInfo(
                    version,
                    MinecraftLauncher.MC_PACKAGE_NAME
                )

            nativeLibDir = applicationInfo.nativeLibraryDir
        } else {
            val packageName = detectGamePackage()
                ?: throw IllegalStateException("Minecraft not found")

            report("Detected Minecraft package: $packageName")

            packageContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE
            )

            applicationInfo = packageContext.applicationInfo
            nativeLibDir = resolveNativeLibDir()
        }

        extractLibraries()
        report("Creating AssetManager")
        assetManager = createAssetManager()
        report("AssetManager ready")
        setupSecurityProvider()
        report("GamePackageManager init finished")
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadLibraryDetailed(name: String): LibraryLoadResult {
        val fileName = toLibraryFileName(name)
        val normalizedName = normalizeLibraryName(name)
        val startedAt = SystemClock.elapsedRealtime()

        if (systemLoadedLibs.contains(fileName)) {
            val source = "launcher bundled library"
            try {
                System.loadLibrary(normalizedName)
                return LibraryLoadResult(
                    normalizedName,
                    fileName,
                    source,
                    true,
                    elapsedSince(startedAt)
                )
            } catch (e: UnsatisfiedLinkError) {
                val detail = e.message ?: e.javaClass.simpleName
                Log.e(TAG, "Failed to load $fileName from $source: $detail")
                return LibraryLoadResult(normalizedName, fileName, source, false, elapsedSince(startedAt), detail)
            } catch (e: Exception) {
                val detail = e.message ?: e.javaClass.simpleName
                Log.e(TAG, "Failed to load $fileName from $source: $detail")
                return LibraryLoadResult(normalizedName, fileName, source, false, elapsedSince(startedAt), detail)
            }
        }

        ...
    }
