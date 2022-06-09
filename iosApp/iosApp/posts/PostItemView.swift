//
//  Created by Emanuel Moecklin on 5/22/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct PostItemView: View {
    let post: Post
    let onClick: () -> Void
    
    var body: some View {
        HStack {
            AsyncImage(url: URL(string: post.avatarUrl)) { image in
                image.resizable()
            } placeholder: {
                ProgressView()
            }
            .frame(width: 40, height: 40)
            
            VStack {
                Text(post.username)
                    .font(.system(size: 14.0))
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                Text(post.title)
                    .font(.system(size: 16.0))
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
            }
        }
        .onTapGesture(perform: onClick)
    }
}
