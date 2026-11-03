
int FUN_ffff0000b805936c
              (undefined8 param_1,uint param_2,char param_3,uint param_4,uint param_5,char param_6,
              byte param_7)

{
  long lVar1;
  long lVar2;
  byte bVar3;
  byte bVar4;
  uint uVar5;
  uint uVar6;
  undefined *puVar7;
  undefined4 uVar8;
  int iVar9;
  undefined8 uVar10;
  ulong uVar11;
  char *pcVar12;
  undefined8 local_90;
  undefined4 local_88;
  undefined4 local_84;
  undefined5 uStack_80;
  undefined4 uStack_7b;
  undefined5 uStack_77;
  undefined2 local_72;
  undefined8 local_70;
  undefined *local_68;
  undefined4 local_44;
  
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
  uVar10 = FUN_ffff0000b8056e5c();
  uVar11 = FUN_ffff0000b8053cfc(uVar10,&local_44);
  uVar8 = local_44;
  if ((uVar11 & 1) == 0) {
    return -5;
  }
  lVar2 = (long)&local_84 + 1;
  FUN_ffff0000b804d070(lVar2,0,0x10);
  puVar7 = local_68;
  local_90 = CONCAT44(uVar8,0xc4);
  local_88 = 1;
  local_72 = 0xc;
  local_70 = CONCAT44(local_70._4_4_,0x200);
  local_84 = 0xecb500;
  uStack_80 = 1;
  uStack_7b = 2;
  if (local_68 == (undefined *)0x0) {
    FUN_ffff0000b804c33c
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000b808cbec,
               s_ufshcd_swp_conf_block_write_prep_ffff0000b8099589);
  }
  else {
    FUN_ffff0000b804d070(local_68,0,0x200);
    DAT_ffff0000b81b6bc0 = param_6 - 1;
    uVar5 = (param_2 & 0xff00ff00) >> 8 | (param_2 & 0xff00ff) << 8;
    *(undefined2 *)(puVar7 + 0x1fe) = 0x600;
    *(undefined4 *)(puVar7 + 0x1f8) = 0x1000000;
    bVar4 = param_3 << 1 | 1;
    *(uint *)(puVar7 + 500) = uVar5 >> 0x10 | uVar5 << 0x10;
    if (DAT_ffff0000b81b6bc0 < 2) {
      DAT_ffff0000b81b6bd0 = 0;
      if (param_3 != '\x04') {
        DAT_ffff0000b81b6bd0 = bVar4;
      }
      DAT_ffff0000b81b6bc1 = '\x10';
      DAT_ffff0000b81b6bdc = 0;
      DAT_ffff0000b81b6bd4 = 0;
    }
    else {
      lVar1 = (ulong)param_7 * 0x10;
      bVar3 = 0;
      if (param_3 != '\x04') {
        bVar3 = bVar4;
      }
      uVar5 = (param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8;
      uVar6 = (param_5 & 0xff00ff00) >> 8 | (param_5 & 0xff00ff) << 8;
      DAT_ffff0000b81b6bc1 = param_7 * '\x10' + '\x10';
      (&DAT_ffff0000b81b6bd0)[lVar1] = bVar3;
      *(uint *)((long)&DAT_ffff0000b81b6bd4 + lVar1 + 4) = uVar5 >> 0x10 | uVar5 << 0x10;
      (&DAT_ffff0000b81b6bdc)[(ulong)param_7 * 4] = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    FUN_ffff0000b804c33c
              (s___s___Write_swpcb_ffff0000b8087ad3,
               s_ufshcd_swp_conf_block_write_prep_ffff0000b8099589);
    FUN_ffff0000b805a328(&DAT_ffff0000b81b6bc0);
    FUN_ffff0000b804cd40(puVar7 + 0xe4,&DAT_ffff0000b81b6bc0,0x100);
    iVar9 = FUN_ffff0000b805a190(puVar7);
    if (iVar9 == 0) {
      FUN_ffff0000b804cd40(puVar7 + 0xc4,DAT_ffff0000b81b6948,0x20);
    }
    else {
      FUN_ffff0000b804c33c
                (s___s___rpmb_get_mac_fail__ret__d_ffff0000b80a0a46,
                 s_ufshcd_swp_conf_block_write_prep_ffff0000b8099589,iVar9);
    }
  }
  iVar9 = FUN_ffff0000b8054db0(uVar10,&local_90);
  uVar8 = local_44;
  if (iVar9 == 0) {
    FUN_ffff0000b804d070(lVar2,0,0x10);
    puVar7 = local_68;
    local_90 = CONCAT44(uVar8,0xc4);
    local_88 = 1;
    local_72 = 0xc;
    local_70 = CONCAT44(local_70._4_4_,0x200);
    local_84 = 0xecb500;
    uStack_80 = 1;
    uStack_7b = 2;
    if (local_68 == (undefined *)0x0) {
      FUN_ffff0000b804c33c
                (s___s___RPMB_pfrm_pointer_NULL_ffff0000b808cbec,
                 s_ufshcd_authen_result_read_req_pr_ffff0000b809b100);
    }
    else {
      FUN_ffff0000b804d070(local_68,0,0x200);
      *(undefined2 *)(puVar7 + 0x1fe) = 0x500;
    }
    iVar9 = FUN_ffff0000b8054db0(uVar10,&local_90);
    uVar8 = local_44;
    if (iVar9 == 0) {
      FUN_ffff0000b804d070(lVar2,0,0x10);
      local_70 = CONCAT44(local_70._4_4_,0x200);
      local_90 = CONCAT44(uVar8,0xc4);
      local_88 = 2;
      uStack_7b = 2;
      local_72 = 0xc;
      local_84 = 0xeca200;
      uStack_80 = 1;
      iVar9 = FUN_ffff0000b8054db0(uVar10,&local_90);
      if (iVar9 == 0) {
        iVar9 = FUN_ffff0000b8059d00(&local_90,param_2,0x600);
        if (iVar9 == 0) goto LAB_ffff0000b80596b4;
        pcVar12 = s___s___RPMB_WriteCounter_Read_Res_ffff0000b8093e20;
      }
      else {
        pcVar12 = s___s___Sending_request_get_result_ffff0000b8099548;
      }
    }
    else {
      pcVar12 = s___s___Sending_result_register_re_ffff0000b8097a3a;
    }
  }
  else {
    pcVar12 = s___s___Sending_Secure_Write_Prote_ffff0000b8092dba;
  }
  FUN_ffff0000b804c33c(pcVar12,s_rpmb_authen_secure_write_protect_ffff0000b809a334,iVar9);
LAB_ffff0000b80596b4:
  FUN_ffff0000b8053dcc(uVar10,local_44);
  return iVar9;
}


