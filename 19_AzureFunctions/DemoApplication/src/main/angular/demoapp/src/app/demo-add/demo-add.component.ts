import { Component, OnInit, Input } from '@angular/core';
import { MemberService } from '../member.service';
import { Member } from '../entity/member';
import { Router } from '@angular/router';

@Component({
  selector: 'app-demo-add',
  templateUrl: './demo-add.component.html',
  styleUrls: ['./demo-add.component.css']
})
export class DemoAddComponent implements OnInit {

  constructor(private memberService: MemberService, private router: Router) { }

  ngOnInit() {
  }

  /**
   * メンバー登録処理
   * @param name 名前
   * @param age 年齢
   */
  regist(name: string, age: number): void {
    
    let member = new Member();
    member.name = name;
    member.age = age;

    // メンバー情報を登録する。
    this.memberService.addMember(member)
      .subscribe(any => this.router.navigate(['/list']));
  }
}
