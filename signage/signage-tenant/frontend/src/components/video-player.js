import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';

/**
 * `video-player`
 *
 * Video Player for DASH element.
 *
 * @customElement
 * @polymer
 */
class VideoPlayer extends PolymerElement {

    static get template() {
        return html`
            <style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
                
                video {
                	width: 770px;
                	height: 450px;
                	margin: auto;
                }
            </style>
            
            <div><video id="videoPlayer" controls></video></div>
        `;
    }

    static get is() {
        return 'video-player';
    }

    static get properties() {
        return {
            url: {
                type: String,
                notify: true,
                observer: '_changedUrl'
            }
        };
    }
    
    _changedUrl(newValue) {
        if(this.player !== null && this.player !== undefined) {
            if(!this._isEmptyString(newValue)) {
                this.player.attachSource(newValue);
                this.player.play();
            }
        }
    }

    _isEmptyString(value) {
        if(value === null || value === undefined || value.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    ready() {
        super.ready();
        this.player = dashjs.MediaPlayer().create();
        this.player.initialize(this.shadowRoot.querySelector("#videoPlayer"));
    }

    connectedCallback() {
        super.connectedCallback();
        if(!this._isEmptyString(this.url)) {
            this.player.attachSource(this.url);
            this.player.play();
        }
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        if(!this._isEmptyString(this.url) && !this.player.isPaused) {
            this.player.pause();
        }
    }
}

customElements.define(VideoPlayer.is, VideoPlayer);

