
int FUN_ffff0000be072afc
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
    pcVar8 = s___s___RPMB_nonce_buf_pointer_NUL_ffff0000be0abeef;
LAB_ffff0000be072c00:
    FUN_ffff0000be05fd98(pcVar8,s_rpmb_authen_read_counter_ffff0000be0bdb72);
    return -5;
  }
  if (param_5 == (uint *)0x0) {
    pcVar8 = s___s___RPMB_wr_cnt_pointer_NULL_ffff0000be0ba6f2;
    goto LAB_ffff0000be072c00;
  }
  local_70 = 0;
  local_68 = 0;
  local_80 = 0;
  local_78 = 0;
  local_84 = 0;
  local_60 = 0;
  local_58 = &DAT_ffff0000be1b4c10;
  uVar6 = FUN_ffff0000be070acc(param_2);
  uVar7 = FUN_ffff0000be06da88(uVar6,&local_84);
  uVar3 = local_84;
  if ((uVar7 & 1) == 0) {
    return -5;
  }
  FUN_ffff0000be060ae0((long)&local_78 + 5,0,0x10);
  puVar4 = local_58;
  local_80 = CONCAT44(uVar3,0xc4);
  local_60 = CONCAT44(local_60._4_4_,0x200);
  local_68 = CONCAT26(0xc,(undefined6)local_68);
  local_78 = CONCAT17(param_1,0xecb50000000001);
  local_68 = local_68 & 0xffffffffffffff00;
  local_70 = 0x20000000001;
  if (local_58 == (undefined *)0x0) {
    FUN_ffff0000be05fd98
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000be0ad049,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000be0bb795);
  }
  else if (param_4 < 0x11) {
    FUN_ffff0000be060ae0(local_58,0,0x200);
    FUN_ffff0000be0607b0(puVar4 + 0x1e4,param_3,param_4);
    *(undefined2 *)(puVar4 + 0x1fe) = 0x200;
  }
  else {
    FUN_ffff0000be05fd98
              (s___s___RPMB_Fail_Nonce_data_lengt_ffff0000be0ae076,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000be0bb795,0x10);
  }
  iVar5 = FUN_ffff0000be06eb3c(uVar6,&local_80);
  uVar3 = local_84;
  if (iVar5 == 0) {
    FUN_ffff0000be060ae0((long)&local_78 + 5,0,0x10);
    local_80 = CONCAT44(uVar3,0xc4);
    local_60 = CONCAT44(local_60._4_4_,0x200);
    local_68 = CONCAT26(0xc,(undefined6)local_68);
    local_78 = CONCAT17(param_1,0xeca20000000002);
    local_68 = local_68 & 0xffffffffffffff00;
    local_70 = 0x20000000001;
    iVar5 = FUN_ffff0000be06eb3c(uVar6,&local_80);
    puVar4 = local_58;
    if (iVar5 == 0) {
      if (local_58 == (undefined *)0x0) {
        FUN_ffff0000be05fd98
                  (s___s___RPMB_rpmb_frame_pointer_NU_ffff0000be0bc8a1,
                   s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000be0a518e);
        iVar5 = -6;
      }
      else {
        uVar1 = *(ushort *)(local_58 + 0x1fe);
        if (uVar1 == 2) {
          if ((int)((ulong)(byte)local_58[0x1fd] & 0x7f) == 0) {
            iVar5 = FUN_ffff0000be060778(local_58 + 0x1e4,param_3,param_4);
            if (iVar5 == 0) {
              iVar5 = FUN_ffff0000be074520(puVar4);
              if (iVar5 == 0) {
                iVar5 = FUN_ffff0000be060778(puVar4 + 0xc4,DAT_ffff0000be1b4c08,0x20);
                if (iVar5 == 0) {
                  iVar5 = 0;
                  uVar2 = (*(uint *)(puVar4 + 500) & 0xff00ff00) >> 8 |
                          (*(uint *)(puVar4 + 500) & 0xff00ff) << 8;
                  *param_5 = uVar2 >> 0x10 | uVar2 << 0x10;
                  goto LAB_ffff0000be072da0;
                }
                FUN_ffff0000be05fd98
                          (s___s___RPMB_Fail_in_Mac_Check__ffff0000be0a6513,
                           s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000be0a518e);
                FUN_ffff0000be05fd98(s_Device_Return_ffff0000be0a9b49);
                FUN_ffff0000be05fd98(s_Host_Recalc_ffff0000be0c5ae8);
                iVar5 = -1;
              }
              else {
                FUN_ffff0000be05fd98
                          (s___s___rpmb_get_mac_fail__ret__d_ffff0000be0c5ac6,
                           s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000be0a518e,iVar5);
                iVar5 = -6;
              }
            }
            else {
              FUN_ffff0000be05fd98
                        (s___s___RPMB_Fail_in_checking_nonc_ffff0000be0c4a0b,
                         s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000be0a518e);
              iVar5 = -3;
            }
          }
          else {
            FUN_ffff0000be05fd98
                      (s___s___RPMB_Fail_in_checking_resu_ffff0000be0abf3d,
                       s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000be0a518e,
                       ((ulong)(byte)local_58[0x1fd] & 0x7f) * 0x40 + -0xffff41f2d9d4);
            iVar5 = -2;
          }
        }
        else {
          FUN_ffff0000be05fd98
                    (s___s___RPMB_Fail_in_checking_resp_ffff0000be0b7064,
                     s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000be0a518e,uVar1 >> 8 | uVar1 << 8);
          iVar5 = -4;
        }
      }
      pcVar8 = s___s___RPMB_WriteCounter_Read_Res_ffff0000be0a3e46;
    }
    else {
      pcVar8 = s___s___Get_result_response_fail__r_ffff0000be0b701f;
    }
  }
  else {
    pcVar8 = s___s___Sending_Write_counter_read_ffff0000be0b168b;
  }
  FUN_ffff0000be05fd98(pcVar8,s_rpmb_authen_read_counter_ffff0000be0bdb72,iVar5);
LAB_ffff0000be072da0:
  FUN_ffff0000be06db58(uVar6,local_84);
  return iVar5;
}


