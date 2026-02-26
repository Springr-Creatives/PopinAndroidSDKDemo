//
//  Popin_SDK_DemoApp.swift
//  Popin SDK Demo
//
//  Created by Ashwin on 28/01/26.
//

import SwiftUI
import Combine
import PopinCall

class AppState: ObservableObject {
    @Published var isLoggedIn: Bool

    init() {
        isLoggedIn = UserDefaults.standard.string(forKey: "popin_user_name") != nil
    }

    func login(userName: String, contactInfo: String) {
        UserDefaults.standard.set(userName, forKey: "popin_user_name")
        UserDefaults.standard.set(contactInfo, forKey: "popin_contact_info")
        AppDelegate.initializePopin(userName: userName, contactInfo: contactInfo)
        isLoggedIn = true
    }

    func logout() {
        Popin.deinitialize()
        UserDefaults.standard.removeObject(forKey: "popin_user_name")
        UserDefaults.standard.removeObject(forKey: "popin_contact_info")
        isLoggedIn = false
    }
}

@main
struct Popin_SDK_DemoApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    @StateObject private var appState = AppState()

    var body: some Scene {
        WindowGroup {
            if appState.isLoggedIn {
                ContentView()
                    .environmentObject(appState)
            } else {
                LoginView()
                    .environmentObject(appState)
            }
        }
    }
}
