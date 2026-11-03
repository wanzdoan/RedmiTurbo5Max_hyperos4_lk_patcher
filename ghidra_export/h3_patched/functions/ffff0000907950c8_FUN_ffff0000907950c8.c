// ffff0000907950c8  size=880  FUN_ffff0000907950c8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907950c8
              (undefined1 param_1,undefined4 param_2,long param_3,uint param_4,uint *param_5)

{
  ushort uVar1;
  uint uVar2;
  undefined4 uVar3;
  long lVar4;
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
  long local_58;
  
  if (param_3 == 0) {
    pcVar8 = s___s___RPMB_nonce_buf_pointer_NUL_ffff0000907db97b;
LAB_ffff0000907951cc:
    FUN_ffff00009077de54(pcVar8,s_rpmb_authen_read_counter_ffff0000907f5623);
    return -5;
  }
  if (param_5 == (uint *)0x0) {
    pcVar8 = s___s___RPMB_wr_cnt_pointer_NULL_ffff0000907f0709;
    goto LAB_ffff0000907951cc;
  }
  local_70 = 0;
  local_68 = 0;
  local_80 = 0;
  local_78 = 0;
  local_84 = 0;
  local_60 = 0;
  local_58 = -0xffff6f6f5b30;
  uVar6 = FUN_ffff000090792e88(param_2);
  uVar7 = FUN_ffff00009078fca8(uVar6,&local_84);
  uVar3 = local_84;
  if ((uVar7 & 1) == 0) {
    return -5;
  }
  FUN_ffff00009077eb88((long)&local_78 + 5,0,0x10);
  lVar4 = local_58;
  local_80 = CONCAT44(uVar3,0xc4);
  local_60 = CONCAT44(local_60._4_4_,0x200);
  local_68 = CONCAT26(0xc,(undefined6)local_68);
  local_78 = CONCAT17(param_1,0xecb50000000001);
  local_68 = local_68 & 0xffffffffffffff00;
  local_70 = 0x20000000001;
  if (local_58 == 0) {
    FUN_ffff00009077de54
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd45b,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000907f205b);
  }
  else if (param_4 < 0x11) {
    FUN_ffff00009077eb88(local_58,0,0x200);
    FUN_ffff00009077e858(lVar4 + 0x1e4,param_3,param_4);
    *(undefined2 *)(lVar4 + 0x1fe) = 0x200;
  }
  else {
    FUN_ffff00009077de54
              (s___s___RPMB_Fail_Nonce_data_lengt_ffff0000907dee5f,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000907f205b,0x10);
  }
  iVar5 = FUN_ffff000090790d5c(uVar6,&local_80);
  uVar3 = local_84;
  if (iVar5 == 0) {
    FUN_ffff00009077eb88((long)&local_78 + 5,0,0x10);
    local_80 = CONCAT44(uVar3,0xc4);
    local_60 = CONCAT44(local_60._4_4_,0x200);
    local_68 = CONCAT26(0xc,(undefined6)local_68);
    local_78 = CONCAT17(param_1,0xeca20000000002);
    local_68 = local_68 & 0xffffffffffffff00;
    local_70 = 0x20000000001;
    iVar5 = FUN_ffff000090790d5c(uVar6,&local_80);
    lVar4 = local_58;
    if (iVar5 == 0) {
      if (local_58 == 0) {
        FUN_ffff00009077de54
                  (s___s___RPMB_rpmb_frame_pointer_NU_ffff0000907f388c,
                   s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000907d2076);
        iVar5 = -6;
      }
      else {
        uVar1 = *(ushort *)(local_58 + 0x1fe);
        if (uVar1 == 2) {
          uVar7 = (ulong)*(byte *)(local_58 + 0x1fd) & 0x7f;
          if ((int)uVar7 == 0) {
            iVar5 = FUN_ffff00009077e820(local_58 + 0x1e4,param_3,param_4);
            if (iVar5 == 0) {
              iVar5 = FUN_ffff000090796aec(lVar4);
              if (iVar5 == 0) {
                iVar5 = FUN_ffff00009077e820(lVar4 + 0xc4,_DAT_ffff00009090a4c8,0x20);
                if (iVar5 == 0) {
                  iVar5 = 0;
                  uVar2 = (*(uint *)(lVar4 + 500) & 0xff00ff00) >> 8 |
                          (*(uint *)(lVar4 + 500) & 0xff00ff) << 8;
                  *param_5 = uVar2 >> 0x10 | uVar2 << 0x10;
                  goto LAB_ffff00009079536c;
                }
                FUN_ffff00009077de54
                          (s___s___RPMB_Fail_in_Mac_Check__ffff0000907d3a27,
                           s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000907d2076);
                FUN_ffff00009077de54(s_Device_Return_ffff0000907d8769);
                FUN_ffff00009077de54(s_Host_Recalc_ffff0000908017be);
                iVar5 = -1;
              }
              else {
                FUN_ffff00009077de54
                          (s___s___rpmb_get_mac_fail__ret__d_ffff00009080179c,
                           s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000907d2076,iVar5);
                iVar5 = -6;
              }
            }
            else {
              FUN_ffff00009077de54
                        (s___s___RPMB_Fail_in_checking_nonc_ffff0000907ffe3e,
                         s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000907d2076);
              iVar5 = -3;
            }
          }
          else {
            FUN_ffff00009077de54
                      (s___s___RPMB_Fail_in_checking_resu_ffff0000907db9c9,
                       s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000907d2076,
                       uVar7 * 0x40 + -0xffff6f7edcc4);
            iVar5 = -2;
          }
        }
        else {
          FUN_ffff00009077de54
                    (s___s___RPMB_Fail_in_checking_resp_ffff0000907eb9d4,
                     s_ufshcd_authensss_wcnt_read_rsp_c_ffff0000907d2076,uVar1 >> 8 | uVar1 << 8);
          iVar5 = -4;
        }
      }
      pcVar8 = s___s___RPMB_WriteCounter_Read_Res_ffff0000907d04e8;
    }
    else {
      pcVar8 = s___s___Get_result_response_fail__r_ffff0000907eb98f;
    }
  }
  else {
    pcVar8 = s___s___Sending_Write_counter_read_ffff0000907e3bd3;
  }
  FUN_ffff00009077de54(pcVar8,s_rpmb_authen_read_counter_ffff0000907f5623,iVar5);
LAB_ffff00009079536c:
  FUN_ffff00009078fd78(uVar6,local_84);
  return iVar5;
}


