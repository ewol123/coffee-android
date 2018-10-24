package com.example.x.coffeetime.application.ui

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import io.reactivex.disposables.Disposable
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.bobekos.bobek.scanner.R.attr.setBeepSound
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.barcode_fragment.*


class BarcodeFragment : Fragment() {

    private var mDisposable: Disposable? = null
    private val CAMERA_REQUEST_CODE = 100

    companion object {
        fun newInstance() = BarcodeFragment()
    }

    private lateinit var viewModel: BarcodeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.barcode_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BarcodeViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()

        //make sure to request camera permission before the subscription
        setupPermissions()

    }

    private fun setupPermissions() {
        val permission = ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("permission", "Permission to record denied")
            makeRequest()
        }
        else {
            scanBarcode()
        }
    }

    private fun makeRequest() {
        requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
    }



    private fun scanBarcode(){
        mDisposable = barcodeView
                .drawOverlay()
                .setBeepSound(true)
                .getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { barcode ->
                            //handle barcode object

                            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                            sharedPref
                                    ?.edit()
                                    ?.putString(getString(R.string.preference_file_key), barcode.rawValue)
                                    ?.apply()
                            findNavController().navigate(R.id.action_barcode_to_menu,null)
                            Log.d("barcode captured:", barcode.toString())
                        },
                        { throwable ->
                            //handle exceptions like no available camera for selected facing
                            findNavController().navigate(R.id.action_barcode_to_menu,null)
                            Toast.makeText(context,"Oops...some error occured, please try again",Toast.LENGTH_SHORT).show()
                        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("permission", "Permission has been denied by user")
                    findNavController().navigate(R.id.action_barcode_to_menu,null)
                } else {
                    Log.i("permission", "Permission has been granted by user")
                    scanBarcode()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        mDisposable?.dispose()
    }

}
