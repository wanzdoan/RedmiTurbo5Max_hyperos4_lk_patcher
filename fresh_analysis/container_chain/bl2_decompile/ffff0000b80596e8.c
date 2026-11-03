
int FUN_ffff0000b80596e8
              (undefined4 param_1,uint param_2,undefined8 param_3,undefined4 param_4,uint param_5)

{
  uint uVar1;
  undefined *puVar2;
  undefined4 uVar3;
  int iVar4;
  long lVar5;
  undefined8 uVar6;
  ulong uVar7;
  char *pcVar8;
  byte bVar9;
  undefined8 local_90;
  undefined4 local_88;
  undefined4 local_84;
  undefined5 uStack_80;
  undefined4 uStack_7b;
  undefined5 uStack_77;
  undefined2 local_72;
  undefined8 local_70;
  undefined *local_68;
  undefined4 local_48;
  undefined4 local_44;
  
  uVar1 = param_5 & 0xff;
  lVar5 = FUN_ffff0000b8056e5c();
  FUN_ffff0000b800ad90(lVar5 + 0x270,0xffffffff);
  if (2 < uVar1) {
    if (uVar1 == 4) {
      iVar4 = FUN_ffff0000b80591cc(param_1,param_2);
      if (iVar4 != 0) {
        FUN_ffff0000b804c33c
                  (s___s___RPMB_rpmb_ufs_clear_wp_err_ffff0000b809bfa6,
                   s_ufs_set_write_protect_ffff0000b8096bd5,iVar4);
      }
    }
    else if (uVar1 == 3) {
      FUN_ffff0000b804c33c
                (s___s___RPMB_WP_Type_Not_Support_ffff0000b8088810,
                 s_ufs_set_write_protect_ffff0000b8096bd5);
      iVar4 = 0;
    }
    else {
      iVar4 = -5;
    }
    goto LAB_ffff0000b80599dc;
  }
  local_48 = 0;
  FUN_ffff0000b8059298();
  FUN_ffff0000b804d070(&DAT_ffff0000b81b6bc0,0,0x100);
  iVar4 = FUN_ffff0000b8058e5c(0,param_1,&DAT_ffff0000b81b6b80,0x10,&local_48);
  if (iVar4 == 0) {
    uStack_80 = 0;
    uStack_7b = 0;
    uStack_77 = 0;
    local_72 = 0;
    local_90 = 0;
    local_88 = 0;
    local_84 = 0;
    local_44 = 0;
    local_70 = 0;
    local_68 = &DAT_ffff0000b81b6950;
    uVar6 = FUN_ffff0000b8056e5c(param_1);
    uVar7 = FUN_ffff0000b8053cfc(uVar6,&local_44);
    uVar3 = local_44;
    if ((uVar7 & 1) == 0) {
      iVar4 = -5;
    }
    else {
      FUN_ffff0000b804d070((long)&local_84 + 1,0,0x10);
      puVar2 = local_68;
      local_90 = CONCAT44(uVar3,0xc4);
      local_88 = 1;
      local_72 = 0xc;
      local_70 = CONCAT44(local_70._4_4_,0x200);
      local_84 = 0xecb500;
      uStack_80 = 1;
      uStack_7b = 2;
      if (local_68 == (undefined *)0x0) {
        FUN_ffff0000b804c33c
                  (s___s___RPMB_pfrm_pointer_NULL_ffff0000b808cbec,
                   s_ufshcd_swp_conf_block_read_prepa_ffff0000b8097a73);
      }
      else {
        FUN_ffff0000b804d070(local_68,0,0x200);
        *(undefined2 *)(puVar2 + 0x1fe) = 0x700;
        *(undefined4 *)(puVar2 + 0x1f8) = 0x1000000;
        FUN_ffff0000b804cd40(puVar2 + 0x1e4,&DAT_ffff0000b81b6b80,0x10);
        puVar2[0xe4] = (char)param_2 + -1;
      }
      iVar4 = FUN_ffff0000b8054db0(uVar6,&local_90);
      uVar3 = local_44;
      if (iVar4 == 0) {
        FUN_ffff0000b804d070((long)&local_84 + 1,0,0x10);
        local_70 = CONCAT44(local_70._4_4_,0x200);
        local_90 = CONCAT44(uVar3,0xc4);
        local_88 = 2;
        uStack_7b = 2;
        local_72 = 0xc;
        local_84 = 0xeca200;
        uStack_80 = 1;
        iVar4 = FUN_ffff0000b8054db0(uVar6,&local_90);
        if (iVar4 == 0) {
          iVar4 = FUN_ffff0000b8059b40(&local_90,&DAT_ffff0000b81b6b80,&DAT_ffff0000b81b6bc0,0x700);
          if (iVar4 == 0) {
            FUN_ffff0000b8053dcc(uVar6,local_44);
            FUN_ffff0000b804c33c
                      (s___s___Read_swpcb_ffff0000b8090504,
                       s_rpmb_authen_secure_write_protect_ffff0000b80912d0);
            FUN_ffff0000b805a328(&DAT_ffff0000b81b6bc0);
            if ((param_2 - 1 & 0xff) < 2) {
              FUN_ffff0000b804d070(&DAT_ffff0000b81b6bd0,0,0x40);
              bVar9 = 0;
            }
            else {
              if (DAT_ffff0000b81b6cc0 == 0) {
                FUN_ffff0000b804d070(&DAT_ffff0000b81b6bd0,0,0x40);
                bVar9 = 0;
              }
              else {
                if (DAT_ffff0000b81b6bc1 == 0x40) {
                  FUN_ffff0000b804c33c
                            (s___s___RPMB_exceed_WP_entries_siz_ffff0000b80a171b,
                             s_rpmb_authen_secure_write_protect_ffff0000b80912d0);
                  pcVar8 = s___s___RPMB_Old_SPWCB_check_fail__ffff0000b808bf59;
                  iVar4 = -5;
                  goto LAB_ffff0000b80599bc;
                }
                bVar9 = DAT_ffff0000b81b6bc1 >> 4;
              }
              DAT_ffff0000b81b6cc0 = DAT_ffff0000b81b6cc0 + 1;
            }
            iVar4 = FUN_ffff0000b8058e5c(0,param_1,&DAT_ffff0000b81b6b80,0x10,&local_48);
            if (iVar4 == 0) {
              iVar4 = FUN_ffff0000b805936c(param_1,local_48,param_5,param_3,param_4,param_2,bVar9);
              if (iVar4 == 0) goto LAB_ffff0000b80599dc;
              pcVar8 = s___s___RPMB_SWPCB_Write_Fail__ret_ffff0000b809df8f;
            }
            else {
              pcVar8 = s___s___RPMB_Get_Write_Counter_fai_ffff0000b808bee4;
            }
            goto LAB_ffff0000b80599bc;
          }
          pcVar8 = s___s___RPMB_Data_Read_Resp_Check_F_ffff0000b8091f81;
        }
        else {
          pcVar8 = s___s___Sending_request_get_result_ffff0000b8099548;
        }
      }
      else {
        pcVar8 = s___s___Sending_Secure_Write_Prote_ffff0000b808b1a0;
      }
      FUN_ffff0000b804c33c(pcVar8,s_rpmb_authen_secure_write_protect_ffff0000b8086b4d,iVar4);
      FUN_ffff0000b8053dcc(uVar6,local_44);
    }
    pcVar8 = s___s___RPMB_Read_Old_SWPCB_fail__r_ffff0000b8086b25;
  }
  else {
    pcVar8 = s___s___RPMB_rpmb_authen_read_coun_ffff0000b80979fb;
  }
LAB_ffff0000b80599bc:
  FUN_ffff0000b804c33c(pcVar8,s_rpmb_ufs_set_wp_ffff0000b80912c0,iVar4);
  FUN_ffff0000b804c33c
            (s___s___RPMB_Set_WP_fail__partitio_ffff0000b8096ba7,
             s_ufs_set_write_protect_ffff0000b8096bd5,param_2 & 0xff,iVar4);
LAB_ffff0000b80599dc:
  FUN_ffff0000b800ae28(lVar5 + 0x270);
  return iVar4;
}


