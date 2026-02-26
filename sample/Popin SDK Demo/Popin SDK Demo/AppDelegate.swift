//
//  AppDelegate.swift
//  Popin SDK Demo
//

import UIKit
import PopinCall

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        NSLog("[Popin] AppDelegate: didFinishLaunchingWithOptions START")

        if let userName = UserDefaults.standard.string(forKey: "popin_user_name"),
           let contactInfo = UserDefaults.standard.string(forKey: "popin_contact_info") {
            Self.initializePopin(userName: userName, contactInfo: contactInfo)
        }

        NSLog("[Popin] AppDelegate: didFinishLaunchingWithOptions END")
        return true
    }

    static func initializePopin(userName: String, contactInfo: String) {
        let config = PopinConfig.Builder()
            .userName(userName)
            .contactInfo(contactInfo)
            .secondaryProductText("Car details")
            .expertDesignation("Car Expert")
            .sandboxMode(true)
            .hideFlipCameraButton(true)
            .enableDebugMode(true)
            .enableIncomingCalls(true)
            .build()

        Popin.initialize(token: 51, config: config)
        Popin.registerForVoIPPushes()
    }
}
