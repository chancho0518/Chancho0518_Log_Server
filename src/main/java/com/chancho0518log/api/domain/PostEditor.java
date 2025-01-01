package com.chancho0518log.api.domain;

import lombok.Getter;

@Getter
public class PostEditor {

    private String title = null;
    private String content = null;

    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostEditor.PostEditorBuilder builder() {
        return new PostEditor.PostEditorBuilder();
    }

    public static class PostEditorBuilder {

        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditor.PostEditorBuilder title(final String title) {
            if(title != null) {
                this.title = title;
            }
            return this;
        }

        public PostEditor.PostEditorBuilder content(final String content) {
            if(content != null) {
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        @Override
        public String toString() {
            return "PostEditor.PostEditorBuilder(" +
                    "title='" + this.title + '\'' +
                    ", content='" + this.content + '\'' +
                    ')';
        }


    }
}
