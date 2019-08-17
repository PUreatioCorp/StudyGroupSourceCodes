import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Member } from './entity/member';
import { tap, catchError } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  private apiUrl = 'http://localhost:8080/api'
  private editUrl = 'http://localhost:8080/api/edit'
  private registUrl = 'http://localhost:8080/api/regist'
  private deleteUrl = 'http://localhost:8080/api/delete'

  /**
   * コンストラクタ
   * @param http DI用HttpClient
   */
  constructor(private http: HttpClient) { }

  /**
   * メンバー検索処理
   * @param searchId 検索ID
   * @returns 検索結果
   */
  searchMembers(searchId: string): Observable<Member[]> {

    // 検索用URLを作成する。
    let url = (searchId === '') ? this.apiUrl : `${this.apiUrl}/${searchId}`;

    // SpringBoot側のAPIに接続する。
    return this.http.get<Member[]>(url).pipe(
      // 取得時、ログを出力する。
      tap(members => console.log('Search Members.')),
      catchError((err, any) => {
        // エラー時、ログを出力する。
        console.error(err);
        return any;
      })
    );
  }

  /**
   * メンバー追加処理
   * @param member 追加メンバー
   */
  addMember(member: Member): Observable<Object> {

    // メンバー情報を追加する。
    return this.http.post(this.registUrl, member, httpOptions).pipe(
      // 登録時、ログを出力する。
      tap(any => console.log('Add Member.')),
      catchError((err, any) => {
        // エラー時、ログを出力する。
        console.error(err);
        return any;
      })
    );
  }

  /**
   * メンバー編集処理
   * @param member 編集メンバー
   */
  editMember(member: Member): Observable<Object> {

    // メンバー情報を更新する。
    return this.http.put(this.editUrl, member, httpOptions).pipe(
      // 更新時、ログを出力する。
      tap(any => console.log('Update Member.')),
      catchError((err, any) => {
        // エラー時、ログを出力する。
        console.error(err);
        return any;
      })
    );
  }

  /**
   * メンバー削除処理
   * @param deleteId 削除ID
   */
  deleteMember(deleteId: string): Observable<Object> {

    // メンバー情報を削除する。
    return this.http.delete(`${this.deleteUrl}/${deleteId}`).pipe(
      // 削除時、ログを出力する。
      tap(any => console.log('Delete Member.')),
      catchError((err, any) => {
        // エラー時、ログを出力する。
        console.error(err);
        return any;
      })
    );
  }
}
