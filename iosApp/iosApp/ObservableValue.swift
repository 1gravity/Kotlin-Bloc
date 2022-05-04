//
//  ObservableValue.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/3/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

public class ObservableValue<T : AnyObject, S : AnyObject> : ObservableObject {
//    private let blocObservable: BlocObservable<T, S>
//    
//    @Published
//    var value: T
//    
//    private var observer: ((T) -> Void)?
//    
//    init(_ bloc: Bloc<T>) {
//        self.observableValue = bloc
//        self.value = observableValue.value
//        
//        self.observer = { value in
//            self.value = value
//        }
//        observableValue.subscribe(observer: observer!)
//    }
//    
//    deinit {
//        self.observableValue.unsubscribe(observer: self.observer!)
//    }
}
