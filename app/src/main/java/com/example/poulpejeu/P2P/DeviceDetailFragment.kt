package com.example.poulpejeu.P2P

import android.app.Fragment
import android.app.ProgressDialog
import android.content.*
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.poulpejeu.GameHandler
import com.example.poulpejeu.R
import com.example.poulpejeu.menus.StartMenu

/**
 * A fragment that manages a particular peer and allows interaction with device
 * i.e. setting up network connection and transferring data.
 */
class DeviceDetailFragment() : Fragment(), ConnectionInfoListener {
    private lateinit var mContentView: View
    private var device: WifiP2pDevice? = null
    private var info: WifiP2pInfo? = null
    var progressDialog: ProgressDialog? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContentView = inflater.inflate(R.layout.device_detail, null)
        mContentView.findViewById<View>(R.id.btn_connect).setOnClickListener(View.OnClickListener {
            val config = WifiP2pConfig()
            config.deviceAddress = device!!.deviceAddress
            config.wps.setup = WpsInfo.PBC
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            progressDialog = ProgressDialog.show(
                activity,
                "Press back to cancel",
                "Connecting to :" + device!!.deviceAddress,
                true,
                true
            )
            (activity as DeviceListFragment.DeviceActionListener).connect(config)
        })
        mContentView.findViewById<View>(R.id.btn_disconnect).setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    (activity as DeviceListFragment.DeviceActionListener).disconnect()
                }
            })

        return mContentView
    }


    override fun onConnectionInfoAvailable(info: WifiP2pInfo) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
        this.info = info
        GameHandler.connexionMade(info)
        this.view!!.visibility = View.VISIBLE

        // The owner IP is now known.
        var view = mContentView.findViewById<View>(R.id.group_owner) as TextView
        view.text = (resources.getString(R.string.group_owner_text)
                + (if ((info.isGroupOwner == true)) resources.getString(R.string.yes) else resources.getString(
            R.string.no
        )))

        // InetAddress from WifiP2pInfo struct.
        view = mContentView.findViewById<View>(R.id.device_info) as TextView
        view.text = "Group Owner IP - " + info.groupOwnerAddress.hostAddress


        if (info.groupFormed && info.isGroupOwner) {
            GameHandler.server = Server()
            GameHandler.server!!.execute()

            val intent = Intent(activity, StartMenu::class.java)
            startActivity(intent)

        } else if (info.groupFormed) {

            val intent = Intent(activity, StartMenu::class.java)
            startActivity(intent)
        }

        // hide the connect button
        mContentView.findViewById<View>(R.id.btn_connect).visibility =
            View.GONE
    }

    /**
     * Updates the UI with device data
     *
     * @param device the device to be displayed
     */
    fun showDetails(device: WifiP2pDevice) {
        this.device = device
        this.view!!.isVisible = true
        var view = mContentView.findViewById<View>(R.id.device_address) as TextView
        view.text = device.deviceAddress
        view = mContentView.findViewById<View>(R.id.device_info) as TextView
        view.text = device.toString()

    }

    /**
     * Clears the UI fields after a disconnect or direct mode disable operation.
     */
    fun resetViews() {
        mContentView.findViewById<View>(R.id.btn_connect).visibility = View.VISIBLE
        var view = mContentView.findViewById<View>(R.id.device_address) as TextView
        view.setText(R.string.empty)
        view = mContentView.findViewById<View>(R.id.device_info) as TextView
        view.setText(R.string.empty)
        view = mContentView.findViewById<View>(R.id.group_owner) as TextView
        view.setText(R.string.empty)
        view = mContentView.findViewById<View>(R.id.status_text) as TextView
        view.setText(R.string.empty)
        mContentView.findViewById<View>(R.id.btn_start_client).visibility =
            View.GONE
        view.visibility = View.GONE
    }

}