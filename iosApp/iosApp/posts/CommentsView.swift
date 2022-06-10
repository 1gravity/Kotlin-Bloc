//
//  Created by Emanuel Moecklin on 5/24/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct CommentsView: View {
    private let post: Post

    init(_ post: Post) {
        self.post = post
    }

    var body: some View {
        VStack {
            let title = NSLocalizedString("posts_compose_comments", comment: "comments")
            Text(String(format: title, post.comments.count))
                .font(.system(size: 16.0))
                .frame(maxWidth: .infinity, alignment: .leading)
            List(post.comments, id: \.self.id) { comment in
                VStack {
                    Text(comment.name.uppercased())
                        .font(.system(size: 12.0))
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text(comment.email)
                        .font(.system(size: 10.0))
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text(comment.body)
                        .font(.system(size: 14.0))
                        .frame(maxWidth: .infinity, alignment: .leading)
                }.listRowInsets(EdgeInsets(top: 8.0, leading: 0.0, bottom: 8.0, trailing: 0.0))
            }.listStyle(PlainListStyle())
        }
    }
    
}
