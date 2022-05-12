//
//  ButtonStyle.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/12/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI

extension Text {
    func textStyle<Style: ViewModifier>(_ style: Style) -> some View {
        ModifiedContent(content: self, modifier: style)
    }
}

struct ButtonStyle: ViewModifier {
    private var backgroundColor: Color
    private var textColor: Color
    private var buttonSize: CGFloat

    init(backgroundColor: Color = grayColor, textColor: Color = Color.black, buttonSize: CGFloat = 80.0) {
        self.backgroundColor = backgroundColor
        self.textColor = textColor
        self.buttonSize = buttonSize
    }
    
    func body(content: Content) -> some View {
        content
            .frame(width: buttonSize, height: buttonSize)
            .foregroundColor(textColor)
            .background(backgroundColor)
            .clipShape(Circle())
            .font(.largeTitle)
    }
}
