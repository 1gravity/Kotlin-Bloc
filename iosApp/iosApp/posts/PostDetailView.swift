//
//  Created by Emanuel Moecklin on 5/24/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct PostDetailView: View {
    private let post: Post

    init(_ post: Post) {
        self.post = post
    }

    var body: some View {
        AnyView(
            VStack {
                Text(post.title)
                    .font(.system(size: 32.0))
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text(post.username)
                    .font(.system(size: 18.0))
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text(post.body)
                    .font(.system(size: 18.0))
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Divider()
                
                CommentsView(post)
            }
            .padding()
            .navigationTitle(post.username)
            .navigationBarTitleDisplayMode(.inline)
        )
    }
}
