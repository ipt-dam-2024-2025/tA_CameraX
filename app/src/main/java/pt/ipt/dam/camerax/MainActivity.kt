package pt.ipt.dam.camerax

import android.content.pm.PackageManager
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.ipt.dam.camerax.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var cameraExecutor:ExecutorService
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContentView(R.layout.activity_main)
        /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        } */

        // usando o 'viewBinding'
        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        // pedir autorizações
        if(allPermissionsGranted()){
            startCamera()
        }
        else{
            requestPermissions()
        }

        // tratar do evento 'click' do botão
        viewBinding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }
        // configura o acesso à câmara
        // implementa o padrão 'Singleton'
        cameraExecutor=Executors.newSingleThreadExecutor()
    }




    //private fun requestPermissions(){}
    /**
     * ask for permissions
     */
    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    /**
     * if it is necessary to ask for permissions,
     * this will evaluate the answers provided by user
     * and start the camera, or inform user that it can not use the camera
     */
    private val activityResultLauncher =
        registerForActivityResult(  ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->

            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                // test for all types of permissions
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    R.string.texto_permissao,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startCamera()
            }
        }




    /**
     * define if all permissions has been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun startCamera() {  }

    private fun takePhoto() {   }




    companion object {
        private const val TAG = "CameraX App"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                // import android.Manifest
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

}