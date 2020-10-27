using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;
using System.Windows.Forms;

namespace LexBotSample
{
    /// <summary>
    /// LexBotSampleフォーム
    /// </summary>
    public partial class LexBotSampleForm : Form
    {
        /// <summary>region</summary>
        private string region = null;
        /// <summary>ボット名</summary>
        private string botName = null;
        /// <summary>ユーザーID</summary>
        private string userId = null;

        /// <summary>
        /// コンストラクタ
        /// </summary>
        public LexBotSampleForm()
        {
            InitializeComponent();

            this.region = ConfigurationManager.AppSettings.Get("Region");
            this.botName = ConfigurationManager.AppSettings.Get("BotName");
            this.userId = ConfigurationManager.AppSettings.Get("UserID");
        }

        /// <summary>
        /// 送信ボタン押下イベント
        /// </summary>
        /// <param name="sender">発生元オブジェクト</param>
        /// <param name="e">イベントデータ</param>
        private void ButtonSend_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(this.textBoxZipCode.Text))
            {
                MessageBox.Show("郵便番号は必須です。", this.Text);
                return;
            }

            string inputText = string.Join(" ", this.labelZipCode.Text, this.textBoxZipCode.Text);

            ProcessStartInfo processInfo = new ProcessStartInfo("cmd.exe",
                string.Format("/c aws lex-runtime post-text --region {0} --bot-name {1} --bot-alias \"$LATEST\" --user-id {2} --input-text \"{3}\"",
                this.region, this.botName, this.userId, inputText));
            processInfo.CreateNoWindow = true;
            processInfo.UseShellExecute = false;
            processInfo.RedirectStandardOutput = true;
            processInfo.RedirectStandardError = true;
            Process process = Process.Start(processInfo);

            process.WaitForExit();

            JavaScriptSerializer serializer = new JavaScriptSerializer();
            Dictionary<string, object> resultDic = serializer.Deserialize<Dictionary<string, object>>(process.StandardOutput.ReadToEnd());
            resultDic.TryGetValue("message", out var resultMessage);
            this.textBoxResult.Text = resultMessage.ToString();
        }
    }
}
