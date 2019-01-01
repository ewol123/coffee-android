package com.example.x.coffeetime.application.ui.barcode

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.x.coffeetime.R
import io.reactivex.disposables.Disposable
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.barcode_fragment.*


class BarcodeFragment : Fragment() {

    private var mDisposable: Disposable? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.barcode_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        startScanBtn.setOnClickListener {
            scanBarcode()

        }

    }


    private fun scanBarcode(){
        if(mDisposable == null) {
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
                                findNavController().navigate(R.id.action_barcode_to_menu, null)
                            },
                            { throwable ->
                                //handle exceptions like no available camera for selected facing
                                findNavController().navigate(R.id.action_barcode_to_menu, null)
                                Toast.makeText(context, "Oops...some error occured, please try again", Toast.LENGTH_SHORT).show()
                            })
        }
    }



    override fun onStop() {
        super.onStop()

        mDisposable?.dispose()
    }

}
