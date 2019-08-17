import { Component, OnInit } from '@angular/core';
import { Member } from '../entity/member';
import { Observable } from 'rxjs';
import { MemberService } from '../member.service';

@Component({
  selector: 'app-demo-list',
  templateUrl: './demo-list.component.html',
  styleUrls: ['./demo-list.component.css']
})
export class DemoListComponent implements OnInit {

  // 検索結果メンバー
  members$: Observable<Member[]>;

  /**
   * コンストラクタ
   * @param memberService DI用メンバーサービス
   */
  constructor(private memberService: MemberService) { }

  /**
   * 初期処理
   */
  ngOnInit() {
  
    // 初期メンバーを設定する。
    this.members$ = this.memberService.searchMembers('');
  }

  /**
   * メンバー検索処理
   * @param searchId 検索ID
   */
  searchMembers(searchId: string): void {

    // メンバーを検索する。
    this.members$ = this.memberService.searchMembers(searchId);
  }

  /**
   * メンバー削除処理
   * @param deleteId 削除ID
   */
  deleteMember(deleteId: string): void {

    // メンバーを削除する。
    this.memberService.deleteMember(deleteId)
      .subscribe(any => this.members$ = this.memberService.searchMembers(''));
  }
}
