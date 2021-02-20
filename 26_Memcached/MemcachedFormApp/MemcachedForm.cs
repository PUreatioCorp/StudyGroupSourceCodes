using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MemcachedFormApp
{
    public partial class MemcachedForm : Form
    {
        /// <summary>
        /// コンストラクタ
        /// </summary>
        public MemcachedForm()
        {
            InitializeComponent();
        }

        /// <summary>
        /// 設定ボタンクリックイベント
        /// </summary>
        /// <param name="sender">イベント発生元オブジェクト</param>
        /// <param name="e">イベントデータ</param>
        private void buttonSet_Click(object sender, EventArgs e)
        {
            string command = string.Format(@"docker exec -it my-python python /var/src/memcached.py w {0} {1}",
                this.textBoxKey.Text, this.textBoxValue.Text);
            Process process = this.GetPowershellProcess(command);

            process.Start();
            process.WaitForExit();
            process.Close();

            MessageBox.Show("設定完了しました。", this.Text, MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        /// <summary>
        /// 取得ボタンクリックイベント
        /// </summary>
        /// <param name="sender">イベント発生元オブジェクト</param>
        /// <param name="e">イベントデータ</param>
        private void buttonGet_Click(object sender, EventArgs e)
        {
            // 値の部分はクリアしておく。
            this.textBoxValue.Text = string.Empty;

            string command = string.Format(@"docker exec -it my-python python /var/src/memcached.py r {0}",
                this.textBoxKey.Text);
            Process process = this.GetPowershellProcess(command);

            process.Start();
            process.WaitForExit();

            string value = process.StandardOutput.ReadToEnd();
            this.textBoxValue.Text = value;

            process.Close();

            MessageBox.Show("取得完了しました。", this.Text, MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        /// <summary>
        /// Powershell実行用のプロセス取得
        /// </summary>
        /// <param name="command">実行コマンド</param>
        /// <returns>プロセス</returns>
        private Process GetPowershellProcess(string command)
        {
            Process process = new Process();
            ProcessStartInfo startInfo = new ProcessStartInfo();
            startInfo.FileName = "powershell.exe";
            startInfo.UseShellExecute = false;
            startInfo.RedirectStandardOutput = true;
            startInfo.RedirectStandardError = true;
            startInfo.CreateNoWindow = true;

            // 実行コマンド設定
            startInfo.Arguments = command;

            process.StartInfo = startInfo;
            return process;
        }
    }
}
