// ffff000090795954  size=1112  FUN_ffff000090795954

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090795954
              (undefined4 param_1,uint param_2,undefined8 param_3,undefined4 param_4,uint param_5)

{
  uint uVar1;
  long lVar2;
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
  long local_68;
  undefined4 local_48;
  undefined4 local_44;
  
  uVar1 = param_5 & 0xff;
  lVar5 = FUN_ffff000090792e88();
  FUN_ffff0000907175f4(lVar5 + 0x270,0xffffffff);
  if (2 < uVar1) {
    if (uVar1 == 4) {
      iVar4 = FUN_ffff000090795438(param_1,param_2);
      if (iVar4 != 0) {
        FUN_ffff00009077de54
                  (s___s___RPMB_rpmb_ufs_clear_wp_err_ffff0000907f8e91,
                   s_ufs_set_write_protect_ffff0000907eef24,iVar4);
      }
    }
    else if (uVar1 == 3) {
      FUN_ffff00009077de54
                (s___s___RPMB_WP_Type_Not_Support_ffff0000907d52c2,
                 s_ufs_set_write_protect_ffff0000907eef24);
      iVar4 = 0;
    }
    else {
      iVar4 = -5;
    }
    goto LAB_ffff000090795c48;
  }
  local_48 = 0;
  FUN_ffff000090795504();
  FUN_ffff00009077eb88(&DAT_ffff00009090a740,0,0x100);
  iVar4 = FUN_ffff0000907950c8(0,param_1,0xffff00009090a700,0x10,&local_48);
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
    local_68 = -0xffff6f6f5b30;
    uVar6 = FUN_ffff000090792e88(param_1);
    uVar7 = FUN_ffff00009078fca8(uVar6,&local_44);
    uVar3 = local_44;
    if ((uVar7 & 1) == 0) {
      iVar4 = -5;
    }
    else {
      FUN_ffff00009077eb88((long)&local_84 + 1,0,0x10);
      lVar2 = local_68;
      local_90 = CONCAT44(uVar3,0xc4);
      local_88 = 1;
      local_72 = 0xc;
      local_70 = CONCAT44(local_70._4_4_,0x200);
      local_84 = 0xecb500;
      uStack_80 = 1;
      uStack_7b = 2;
      if (local_68 == 0) {
        FUN_ffff00009077de54
                  (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd472,
                   s_ufshcd_swp_conf_block_read_prepa_ffff0000907f07c8);
      }
      else {
        FUN_ffff00009077eb88(local_68,0,0x200);
        *(undefined2 *)(lVar2 + 0x1fe) = 0x700;
        *(undefined4 *)(lVar2 + 0x1f8) = 0x1000000;
        FUN_ffff00009077e858(lVar2 + 0x1e4,0xffff00009090a700,0x10);
        *(char *)(lVar2 + 0xe4) = (char)param_2 + -1;
      }
      iVar4 = FUN_ffff000090790d5c(uVar6,&local_90);
      uVar3 = local_44;
      if (iVar4 == 0) {
        FUN_ffff00009077eb88((long)&local_84 + 1,0,0x10);
        local_70 = CONCAT44(local_70._4_4_,0x200);
        local_90 = CONCAT44(uVar3,0xc4);
        local_88 = 2;
        uStack_7b = 2;
        local_72 = 0xc;
        local_84 = 0xeca200;
        uStack_80 = 1;
        iVar4 = FUN_ffff000090790d5c(uVar6,&local_90);
        if (iVar4 == 0) {
          iVar4 = FUN_ffff0000907960ac(&local_90,0xffff00009090a700,&DAT_ffff00009090a740,0x700);
          if (iVar4 == 0) {
            FUN_ffff00009078fd78(uVar6,local_44);
            FUN_ffff00009077de54
                      (s___s___Read_swpcb_ffff0000907e3c21,
                       s_rpmb_authen_secure_write_protect_ffff0000907e55fe);
            FUN_ffff000090796c84(&DAT_ffff00009090a740);
            if ((param_2 - 1 & 0xff) < 2) {
              FUN_ffff00009077eb88(&DAT_ffff00009090a750,0,0x40);
              bVar9 = 0;
            }
            else {
              if (_DAT_ffff00009090a840 == 0) {
                FUN_ffff00009077eb88(&DAT_ffff00009090a750,0,0x40);
                bVar9 = 0;
              }
              else {
                if (DAT_ffff00009090a741 == 0x40) {
                  FUN_ffff00009077de54
                            (s___s___RPMB_exceed_WP_entries_siz_ffff00009080306e,
                             s_rpmb_authen_secure_write_protect_ffff0000907e55fe);
                  pcVar8 = s___s___RPMB_Old_SPWCB_check_fail__ffff0000907dba2a;
                  iVar4 = -5;
                  goto LAB_ffff000090795c28;
                }
                bVar9 = DAT_ffff00009090a741 >> 4;
              }
              _DAT_ffff00009090a840 = _DAT_ffff00009090a840 + 1;
            }
            iVar4 = FUN_ffff0000907950c8(0,param_1,0xffff00009090a700,0x10,&local_48);
            if (iVar4 == 0) {
              iVar4 = FUN_ffff0000907955d8(param_1,local_48,param_5,param_3,param_4,param_2,bVar9);
              if (iVar4 == 0) goto LAB_ffff000090795c48;
              pcVar8 = s___s___RPMB_SWPCB_Write_Fail__ret_ffff0000907fc611;
            }
            else {
              pcVar8 = s___s___RPMB_Get_Write_Counter_fai_ffff0000907db9b5;
            }
            goto LAB_ffff000090795c28;
          }
          pcVar8 = s___s___RPMB_Data_Read_Resp_Check_F_ffff0000907e6e08;
        }
        else {
          pcVar8 = s___s___Sending_request_get_result_ffff0000907f38c7;
        }
      }
      else {
        pcVar8 = s___s___Sending_Secure_Write_Prote_ffff0000907d9f50;
      }
      FUN_ffff00009077de54(pcVar8,s_rpmb_authen_secure_write_protect_ffff0000907d20c3,iVar4);
      FUN_ffff00009078fd78(uVar6,local_44);
    }
    pcVar8 = s___s___RPMB_Read_Old_SWPCB_fail__r_ffff0000907d209b;
  }
  else {
    pcVar8 = s___s___RPMB_rpmb_authen_read_coun_ffff0000907f0750;
  }
LAB_ffff000090795c28:
  FUN_ffff00009077de54(pcVar8,s_rpmb_ufs_set_wp_ffff0000907e55ee,iVar4);
  FUN_ffff00009077de54
            (s___s___RPMB_Set_WP_fail__partitio_ffff0000907eeef6,
             s_ufs_set_write_protect_ffff0000907eef24,param_2 & 0xff,iVar4);
LAB_ffff000090795c48:
  FUN_ffff00009071768c(lVar5 + 0x270);
  return iVar4;
}


