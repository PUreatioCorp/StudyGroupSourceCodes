import { Component, OnInit, Input } from '@angular/core';
import { Member } from '../entity/member';
import { ActivatedRoute } from '@angular/router';
import { MemberService } from '../member.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-demo-edit',
  templateUrl: './demo-edit.component.html',
  styleUrls: ['./demo-edit.component.css']
})
export class DemoEditComponent implements OnInit {

  // 更新対象メンバー
  @Input() member: Member = new Member();

  constructor(private route: ActivatedRoute, private memberService: MemberService, private location: Location) { }

  ngOnInit() {

    // パラメータを利用して初期表示する。
    const id = this.route.snapshot.paramMap.get('id');
    this.memberService.searchMembers(id)
      .subscribe(members => this.member = members[0]);
  }

  /**
   * メンバー編集処理
   */
  edit(): void {
    
    // 指定されたメンバー情報を更新する。
    this.memberService.editMember(this.member)
      .subscribe(any => this.location.back());
  }
}
