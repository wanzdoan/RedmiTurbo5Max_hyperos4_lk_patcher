
int FUN_ffff0000b8058e5c
              (undefined1 param_1,undefined4 param_2,long param_3,uint param_4,uint *param_5)

{
  ushort uVar1;
  uint uVar2;
  undefined4 uVar3;
  undefined *puVar4;
  int iVar5;
  undefined8 uVar6;
  ulong uVar7;
  char *pcVar8;
  undefined4 local_84;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined *local_58;
  
  if (param_3 == 0) {
    pcVar8 = s___s___RPMB_nonce_buf_pointer_NUL_ffff0000b808bec1;
LAB_ffff0000b8058f60:
    FUN_ffff0000b804c33c(pcVar8,s_rpmb_authen_read_counter_ffff0000b809a311);
    return -5;
  }
  if (param_5 == (uint *)0x0) {
    pcVar8 = s___s___RPMB_wr_cnt_pointer_NULL_ffff0000b80979cb;
    goto LAB_ffff0000b8058f60;
  }
  local_70 = 0;
  local_68 = 0;
  local_80 = 0;
  local_78 = 0;
  local_84 = 0;
  local_60 = 0;
  local_58 = &DAT_ffff0000b81b6950;
  uVar6 = FUN_ffff0000b8056e5c(param_2);
  uVar7 = FUN_ffff0000b8053cfc(uVar6,&local_84);
  uVar3 = local_84;
  if ((uVar7 & 1) == 0) {
    return -5;
  }
  FUN_ffff0000b804d070((long)&local_78 + 5,0,0x10);
  puVar4 = local_58;
  local_80 = CONCAT44(uVar3,0xc4);
  local_60 = CONCAT44(local_60._4_4_,0x200);
  local_68 = CONCAT26(0xc,(undefined6)local_68);
  local_78 = CONCAT17(param_1,0xecb50000000001);
  local_68 = local_68 & 0xffffffffffffff00;
  local_70 = 0x20000000001;
  if (local_58 == (undefined *)0x0) {
    FUN_ffff0000b804c33c
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000b808cbec,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000b8098740);
  }
  else if (param_4 < 0x11) {
    FUN_ffff0000b804d070(local_58,0,0x200);
    FUN_ffff0000b804cd40(puVar4 + 0x1e4,param_3,param_4);
    *(undefined2 *)(puVar4 + 0x1fe) = 0x200;
  }
  else {
    FUN_ffff0000b804c33c
              (s___s___RPMB_Fail_Nonce_data_lengt_ffff0000b808d86a,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000b8098740,0x10);
  }
  iVar5 = FUN_ffff0000b8054db0(uVar6,&local_80);
  uVar3 = local_84;
  if (iVar5 == 0) {
    FUN_ffff0000b804d070((long)&local_78 + 5,0,0x10);
    local_80 = CONCAT44(uVar3,0xc4);
    local_60 = CONCAT44(local_60._4_4_,0x200);
    local_68 = CONCAT26(0xc,(undefined6)local_68);
    local_78 = CONCAT17(param_1,0xeca20000000002);
    local_68 = local_68 & 0xffffffffffffff00;
    local_70 = 0x20000000001;
    iVar5 = FUN_ffff0000b8054db0(uVar6,&local_80);
    puVar4 = local_58;
    if (iVar5 == 0) {
      if (local_58 == (undefined *)0x0) {
        FUN_ffff0000b804c33c
                  (s___s___RPMB_rpmb_frame_pointer_NU_ffff0000b8099524,
                   s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000b8086b00);
        iVar5 = -6;
      }
      else {
        uVar1 = *(ushort *)(local_58 + 0x1fe);
        if (uVar1 == 2) {
          if ((int)((ulong)(byte)local_58[0x1fd] & 0x7f) == 0) {
            iVar5 = FUN_ffff0000b804cd08(local_58 + 0x1e4,param_3,param_4);
            if (iVar5 == 0) {
              iVar5 = FUN_ffff0000b805a190(puVar4);
              if (iVar5 == 0) {
                iVar5 = FUN_ffff0000b804cd08(puVar4 + 0xc4,DAT_ffff0000b81b6948,0x20);
                if (iVar5 == 0) {
                  iVar5 = 0;
                  uVar2 = (*(uint *)(puVar4 + 500) & 0xff00ff00) >> 8 |
                          (*(uint *)(puVar4 + 500) & 0xff00ff) << 8;
                  *param_5 = uVar2 >> 0x10 | uVar2 << 0x10;
                  goto LAB_ffff0000b8059100;
                }
                FUN_ffff0000b804c33c
                          (s___s___RPMB_Fail_in_Mac_Check__ffff0000b8087ab4,
                           s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000b8086b00);
                FUN_ffff0000b804c33c(s_Device_Return_ffff0000b808a416);
                FUN_ffff0000b804c33c(s_Host_Recalc_ffff0000b80a0a68);
                iVar5 = -1;
              }
              else {
                FUN_ffff0000b804c33c
                          (s___s___rpmb_get_mac_fail__ret__d_ffff0000b80a0a46,
                           s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000b8086b00,iVar5);
                iVar5 = -6;
              }
            }
            else {
              FUN_ffff0000b804c33c
                        (s___s___RPMB_Fail_in_checking_nonc_ffff0000b809fe7e,
                         s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000b8086b00);
              iVar5 = -3;
            }
          }
          else {
            FUN_ffff0000b804c33c
                      (s___s___RPMB_Fail_in_checking_resu_ffff0000b808bf0f,
                       s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000b8086b00,
                       ((ulong)(byte)local_58[0x1fd] & 0x7f) * 0x40 + -0xffff47f565b4);
            iVar5 = -2;
          }
        }
        else {
          FUN_ffff0000b804c33c
                    (s___s___RPMB_Fail_in_checking_resp_ffff0000b8094ebc,
                     s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000b8086b00,uVar1 >> 8 | uVar1 << 8);
          iVar5 = -4;
        }
      }
      pcVar8 = s___s___RPMB_WriteCounter_Read_Res_ffff0000b8085c3f;
    }
    else {
      pcVar8 = s___s___Get_result_response_fail__r_ffff0000b8094e77;
    }
  }
  else {
    pcVar8 = s___s___Sending_Write_counter_read_ffff0000b80904cd;
  }
  FUN_ffff0000b804c33c(pcVar8,s_rpmb_authen_read_counter_ffff0000b809a311,iVar5);
LAB_ffff0000b8059100:
  FUN_ffff0000b8053dcc(uVar6,local_84);
  return iVar5;
}


